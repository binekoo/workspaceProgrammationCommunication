package fr.upec.episen;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    
    public static void main(String[] args) {
        mainLog.warn("receveur-tcp started");
        //1. Lire les propriétés du projet
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try{
            props.load(is);
            //2. Créer un serveur TCP
            final int PORT = Integer.parseInt(props.getProperty("tcp.port"));
            try (ServerSocket ss = new ServerSocket(PORT)){
                //La socket se créera quand quelque chose sera recç, pas avant.
                Socket socket = ss.accept();
                mainLog.warn("one more connection");
            }
        } catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        } finally {
            mainLog.warn("receveur-tcp stopped");
        }
    }
}