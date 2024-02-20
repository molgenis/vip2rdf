package org.molgenis.vip2rdf.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final String PROPERTIES_FILE_NAME = "application.properties";

    private PropertiesLoader() {
    }

    public static void loadProperties() throws IOException {
        Properties properties = new Properties();

        try ( InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME) ) {
            // Load properties.
            properties.load(inputStream);

            // Set variables.
            AppProperties.APP_NAME.setValue(properties.getProperty("app.name"));
            AppProperties.APP_VERSION.setValue(properties.getProperty("app.version"));
        }
    }
}
