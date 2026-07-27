package com.finastra.integrationapi.tool.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public enum FileUtility {
    INSTANCE;

    public static void writeToFile(String filePath, String content) {
        File file = new File(filePath);

        try {
            // Create parent directories if they don't exist
            file.getParentFile().mkdirs();

            // Write content to the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                System.out.println("Content written successfully to: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
