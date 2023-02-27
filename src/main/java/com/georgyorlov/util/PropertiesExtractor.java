package com.georgyorlov.util;

import com.georgyorlov.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesExtractor {

    private final static Properties properties;

    static {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find application.properties");
            }

            properties = new Properties();

            //load a properties file from class path, inside static method
            properties.load(input);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Properties get() {
        return properties;
    }

}
