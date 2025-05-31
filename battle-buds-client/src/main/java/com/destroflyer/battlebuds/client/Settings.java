package com.destroflyer.battlebuds.client;

import com.destroflyer.battlebuds.shared.FileManager;

import java.util.HashMap;

public class Settings {

    private static final String SETTINGS_FILE_PATH = "settings.ini";

    public static HashMap<String, String> readSettings() {
        HashMap<String, String> settings = new HashMap<>();
        if (FileManager.existsFile(SETTINGS_FILE_PATH)) {
            String[] lines = FileManager.getFileLines(SETTINGS_FILE_PATH);
            for (String line : lines) {
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] keyValue = line.split("=", 2);
                    if (keyValue.length != 2) {
                        throw new RuntimeException("Invalid setting: " + line);
                    }
                    settings.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return settings;
    }
}
