package icu.hacking.zenith.laravel.plus.plugins;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.lang.String;

public class TemplateChooserDialog extends DialogWrapper {

    private ComboBox<String> templateComboBox;
    private JTextField filenameField;
    private final String selectedPath;
    private final Project project; // 获取项目实例
    private static final String TEMPLATES_DIRECTORY_RELATIVE_PATH = "templates";

    public TemplateChooserDialog(Project project, String selectedPath) {
        super(true);
        this.selectedPath = selectedPath;
        this.project = project; // 初始化项目实例
        init();
        setTitle("Template Chooser");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        String[] templates = getTemplateNames();
        templateComboBox = new ComboBox<>(templates);
        templateComboBox.setEditable(true);
        JTextField textField = (JTextField) templateComboBox.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> filter(textField.getText(), templates, templateComboBox));
            }
        });

        JTextField readOnlyField = new JTextField();
        readOnlyField.setEditable(false);
        readOnlyField.setText(this.selectedPath);
        readOnlyField.setPreferredSize(new Dimension(200, readOnlyField.getPreferredSize().height));

        JPanel readOnlyPanel = new JPanel();
        readOnlyPanel.add(new JBLabel("Selected path"), BorderLayout.NORTH);
        readOnlyPanel.add(readOnlyField, BorderLayout.CENTER);
        dialogPanel.add(readOnlyPanel);

        JPanel templatePanel = new JPanel();
        templatePanel.add(new JBLabel("Choose a Template"), BorderLayout.NORTH);
        templatePanel.add(templateComboBox, BorderLayout.CENTER);
        dialogPanel.add(templatePanel);

        filenameField = new JTextField();
        filenameField.setPreferredSize(new Dimension(200, filenameField.getPreferredSize().height));

        JPanel filenamePanel = new JPanel();
        filenamePanel.add(new JBLabel("Filename"), BorderLayout.NORTH);
        filenamePanel.add(filenameField, BorderLayout.CENTER);
        dialogPanel.add(filenamePanel);

        return dialogPanel;
    }

    private void filter(@NotNull String enteredText, String[] list, JComboBox<String> combobox) {
        if (!enteredText.isEmpty()) {
            ArrayList<String> itemsFound = new ArrayList<>();

            for (String item : list) {
                if (item.toLowerCase().contains(enteredText.toLowerCase())) {
                    itemsFound.add(item);
                }
            }

            if (!itemsFound.isEmpty()) {
                combobox.setModel(new DefaultComboBoxModel<>(itemsFound.toArray(new String[0])));
                combobox.setSelectedItem(enteredText);
                combobox.showPopup();
            } else {
                combobox.hidePopup();
            }
        } else {
            combobox.setModel(new DefaultComboBoxModel<>(list));
            combobox.hidePopup();
            combobox.setSelectedItem("");
        }
    }

    public String getSelectedTemplate() {
        String template = (String) templateComboBox.getSelectedItem();
        System.out.println("selected template = " + template);
        return template;
    }

    public String getFilename() {
        return filenameField.getText();
    }

    private String[] getTemplateNames() {
        try {
            // 获取项目根目录路径
            Path projectBasePath = Paths.get(Objects.requireNonNull(project.getBasePath()));
            System.out.println("projectBasePath = " + projectBasePath);

            // 构建模板目录路径
            Path templatesPath = projectBasePath.resolve(TEMPLATES_DIRECTORY_RELATIVE_PATH);
            System.out.println("templatesPath = " + templatesPath);

            try (Stream<Path> walk = Files.walk(templatesPath)) {
                List<String> result = walk.filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(name -> name.endsWith(".tpl"))
                        .map(name -> name.substring(0, name.length() - 4))
                        .toList();
                return result.toArray(new String[0]);
            }
        } catch (IOException e) {
            return new String[]{};
        }
    }
}