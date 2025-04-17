package com.kfiatek.otsw.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;

public class ConfigLoader {
    public static TestConfig load(String path) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
            return yaml.loadAs(inputStream, TestConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Error loading YAML config: " + e.getMessage());
        }

    }
}
