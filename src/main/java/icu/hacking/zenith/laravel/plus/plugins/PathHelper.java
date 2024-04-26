package icu.hacking.zenith.laravel.plus.plugins;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathHelper {

    public static String convertToNamespace(String fullPath, String projectRoot) {
        // Convert to Path objects
        Path filePath = Paths.get(fullPath);
        System.out.println("filePath = " + filePath);
        Path rootPath = Paths.get(projectRoot);
        System.out.println("rootPath = " + rootPath);
        // Remove root path
        Path relativePath = rootPath.relativize(filePath);

        // Replace all file separator chars with namespace separator
        String namespace = relativePath.toString().replace(relativePath.getFileSystem().getSeparator(), "\\");

        // Capitalize the first letter of each part
        String[] parts = namespace.split("\\\\");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append("\\");
        }
        return sb.substring(0, sb.length() - 1);  // Remove trailing backslash
    }
}