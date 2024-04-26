package icu.hacking.zenith.laravel.plus.plugins;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TemplateChooserDialog extends DialogWrapper {

    private ComboBox<String> templateComboBox;

    private JTextField filenameField;

    public TemplateChooserDialog() {
        super(true);
        init();
        setTitle("Template Chooser");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        String[] templates = {"Controller", "Service", "Logic", "LogicInterface", "Repository", "RepositoryInterface", "Model"};
        templateComboBox = new ComboBox<>(templates);

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

    public String getSelectedTemplate() {
        return (String)templateComboBox.getSelectedItem();
    }

    public String getFilename() {
        return filenameField.getText();
    }
}
