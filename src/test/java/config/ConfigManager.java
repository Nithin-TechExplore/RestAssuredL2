package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();
    private static String environment = System.getProperty("environment", "qa");
    private static String bearerToken = System.getProperty("bearer.token", "");

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            System.out.println("environment "+environment);
            System.out.println("bearerToken "+bearerToken);

            String configFile = "src/test/resources/config/" + environment + ".properties";
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration file for environment: " + environment);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getBearerToken() {
        return bearerToken.isEmpty() ? properties.getProperty("bearer.token", "") : bearerToken;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getEnvironment() {
        return environment;
    }
}