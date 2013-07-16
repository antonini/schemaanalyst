/*
 */
package org.schemaanalyst.configuration;

import org.schemaanalyst.util.PropertiesParser;

/**
 * Contains the parameters for connecting to a database.
 *
 * @author Chris J. Wright
 */
public class DatabaseConfiguration {

    private final static String PROPERTIES_LOCATION = "config/database.properties";
    /**
     * The host address of the DBMS that hosts the database.
     */
    private String db_host;
    /**
     * The port of the DBMS that hosts the database.
     */
    private String db_port;
    /**
     * The name of the DBMS hosting the database. Supported values are
     * 'Postgres', 'SQLite' and 'Hsqldb', and are case sensitive.
     */
    private String db_dbms;
    /**
     * The username to connect to the DBMS hosting the database.
     */
    private String db_username;
    /**
     * The password to connect to the DBMS hosting the database.
     */
    private String db_password;

    /**
     * Construct using the default Properties file location.
     */
    public DatabaseConfiguration() {
        PropertiesParser.parse(PROPERTIES_LOCATION, this);
    }

    /**
     * Construct using the provided Properties file location.
     * @param path The path to Properties file.
     */
    public DatabaseConfiguration(String path) {
        PropertiesParser.parse(path, this);
    }
}
