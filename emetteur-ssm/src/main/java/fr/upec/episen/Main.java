package fr.upec.episen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    public static void main(String[] args) {
       mainLog.warn("emetteur-ssm started");
        //1. Lire les propriétés du projet
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try{
            props.load(is);
            //2. Etablir une connexion TCP :
            final String address = props.getProperty("tcp.address");
            mainLog.warn("address : " + address);
        } catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        }
    }
}