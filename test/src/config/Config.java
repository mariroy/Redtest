package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final static Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        load();
    }

    private static void load() {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String name) {
        return PROPERTIES.getProperty(name);
    }
}
