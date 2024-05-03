package icu.hacking.zenith.laravel.plus.plugins;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TemplateChooserDialog extends DialogWrapper {

    private ComboBox<String> templateComboBox;

    private JTextField filenameField;

    private final String selectedPath;

    public TemplateChooserDialog(String selectedPath) {
        super(true);
        this.selectedPath = selectedPath;
        init();
        setTitle("Template Chooser");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        String[] templates = {"Controller", "Bean", "Service", "Logic", "LogicInterface", "Repository", "RepositoryInterface", "Model"};
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
        System.out.println("this.selectedPath = " + this.selectedPath);
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

    private void filter(String enteredText, String[] list, JComboBox<String> combobox) {
        if (!enteredText.isEmpty()) {
            ArrayList<String> itemsFound = new ArrayList<>();

            for (String item : list) {
                if (item.toLowerCase().startsWith(enteredText.toLowerCase())) {
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
        return (String)templateComboBox.getSelectedItem();
    }

    public String getFilename() {
        return filenameField.getText();
    }
}
