package icu.hacking.zenith.laravel.plus.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CreateTemplateFileAction extends AnAction  {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        final Project project = e.getProject();
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();

        LocalFileSystem fileSystem = LocalFileSystem.getInstance();

        assert project != null;
        String projectBasePath = project.getBasePath();

        if (projectBasePath == null) {
            Messages.showWarningDialog("Invalid project base path.", "Error");
            return ;
        }

        String appDirPath = projectBasePath + "/app";
        VirtualFile toSelect = fileSystem.findFileByPath(appDirPath);
        if (toSelect == null) {
            Messages.showWarningDialog("Cannot find app directory", "Error");
        }
        descriptor.setRoots(toSelect);
        descriptor.withTreeRootVisible(true);

        VirtualFile chosenFile = FileChooser.chooseFile(descriptor, project, null);
        if (chosenFile != null) {
            TemplateChooserDialog templateChooserDialog = new TemplateChooserDialog(chosenFile.getPath());
            templateChooserDialog.showAndGet();
            if (templateChooserDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
                String selectedTemplate = templateChooserDialog.getSelectedTemplate();
                String inputFilename = templateChooserDialog.getFilename();
                TemplateProcessor templateProcessor = new TemplateProcessor();
                try {
                    String filepath = templateProcessor.processTemplate(chosenFile.getPath(), inputFilename, projectBasePath, selectedTemplate);
                    VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByPath(filepath);
                    if (file == null) {
                        Messages.showErrorDialog("Cannot find created file", "Error");
                        return;
                    }
                    FileEditorManager.getInstance(project).openFile(file, true);
                } catch (IOException ex) {
                    Messages.showErrorDialog("Cannot find template file", "Error");
                }
            }
        }
    }
}
