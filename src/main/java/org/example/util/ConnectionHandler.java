package org.example.util;

import org.example.controller.PokedexController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionHandler {

    private static Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(PokedexController.class);

    static {
        if (connection == null){
            Properties properties = new Properties();

            try (InputStream input = ConnectionHandler.class.getClassLoader().getResourceAsStream("database.properties")){

                if(input == null){
                    throw new Exception("Unable to find database.properties");
                }else{
                    properties.load(input);
                }//if else

                // Load JDBC Driver
                Class.forName(properties.getProperty("db.driver"));

                connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.username"),
                        properties.getProperty("db.password")
                );

            }catch(IOException | ClassNotFoundException e){
                logger.debug("Failed to load database configuration.");
                throw new RuntimeException("Failed to load database configuration");
            }catch(Exception e){
                logger.debug("Another exception occured while trying to load database configuration.");
                throw new RuntimeException(e);
            }//try catch
        }//if conn==null
    }//static

    public static Connection getConnection() throws RuntimeException {
        if(connection == null){
            logger.info("Connection was NULL.");
            throw new RuntimeException("Connection failed to setup correctly");
        }//if no conn

        return connection;
    }//getconn method

}//connection handler class
