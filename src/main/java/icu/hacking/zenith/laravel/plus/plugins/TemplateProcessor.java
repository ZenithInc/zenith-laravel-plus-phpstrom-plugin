package icu.hacking.zenith.laravel.plus.plugins;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TemplateProcessor {

    public String processTemplate(String selectedPath, String filename, String projectDir, String template) throws IOException {
        Path templatePath = Paths.get(projectDir + "/templates/", template + ".tpl");
        System.out.println("templatePath = " + templatePath);
        Path destPath = Paths.get(selectedPath, filename + ".php");
        System.out.println("destPath = " + destPath);
        List<String> lines = Files.readAllLines(templatePath, StandardCharsets.UTF_8);

        List<String> processedLines = new ArrayList<>();
        for (String line : lines) {
            String namespace = PathHelper.convertToNamespace(selectedPath, projectDir);
            String processedLine = line.replace("${NAMESPACE}", namespace)
                    .replace("${NAME}", filename);
            processedLines.add(processedLine);
        }
        Files.write(destPath, processedLines, StandardCharsets.UTF_8);
        return destPath.toString();
    }
}
