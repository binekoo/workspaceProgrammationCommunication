package fr.upec.episen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public enum Connexion {
    instance;
    protected Logger conLog = LogManager.getLogger(Connexion.class);
    protected Connection connection;

    Connexion(){
        try{
            Class<?> driverClass = Class.forName(Main.props.getProperty("database.driver"));
            DriverManager.registerDriver((Driver) driverClass.getConstructor().newInstance());
            this.connection = DriverManager.getConnection(Main.props.getProperty("database.url"),
                    Main.props.getProperty("database.username"),
                    Main.props.getProperty("database.password")
            );
        } catch(Exception e){
            conLog.error(e.getMessage());
        } finally {
            conLog.info("Connexion = " + connection.toString());
        }
    }

    //pas besoin de getInstance() -> faire connexion.Instance().

}
