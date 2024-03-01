package fr.upec.episen;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    public static void main(String[] args) {
        mainLog.warn("emetteur-tcp started");
        //1. Lire les propriétés du projet
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try{
            props.load(is);
            //2. Créer un serveur TCP
            final int PORT = Integer.parseInt(props.getProperty("tcp.prot"));
            final String ADDR = props.getProperty("tcp.address");
            InetAddress  address = InetAddress.getByName(ADDR);
            //On se connecte au receveur ici :
            Socket socket = new Socket(address, PORT);
            mainLog.warn("emetteur-tcp connected");
        } catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        } finally {
            mainLog.warn("emetteur tcp stopped");
        }
    }
}