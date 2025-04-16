package edu.jsu.mcis.cs310.tas_sp25.dao;

import java.io.*;
import java.util.Properties;

/**
 * Loads database configuration properties from the
 * {@code dao.properties} file using a prefix.
 * 
 */
public class DAOProperties {

    private static final String PROPERTIES_FILE = "dao.properties";
    private static final Properties PROPERTIES = new Properties();
    private final String prefix;

    static {
        try {
            InputStream file = DAOProperties.class.getResourceAsStream(PROPERTIES_FILE);
            PROPERTIES.load(file);
        } catch (IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Initializes DAOProperties with the given prefix.
     *
     * @param prefix the prefix used for property key lookup
     */
    public DAOProperties(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Retrieves a property value using the prefix and key.
     *
     * @param key the property key (e.g., "url")
     * @return the property value, or {@code null} if not found
     */
    public String getProperty(String key) {
        String fullKey = prefix + "." + key;
        String property = PROPERTIES.getProperty(fullKey);

        if (property == null || property.trim().length() == 0) {
            property = null;
        }

        return property;
    }

}
