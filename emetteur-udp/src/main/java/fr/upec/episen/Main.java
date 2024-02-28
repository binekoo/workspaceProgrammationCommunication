package fr.upec.episen;
import org.apache.logging.log4j.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    public static void main(String[] args) {
        mainLog.info("emetteur-udp started");

        try {
            // 0. Chagement du fichier de properties
            InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            props.load(iStream);
            
            // 1. Création d'un datagram packet
            //TODO : encoder en JSON une commande
            String achatContenu = "{tu me recois}";
            byte[] buffer = achatContenu.getBytes();
            Integer port = Integer.valueOf(props.getProperty("udp.port", "1024"));


            InetAddress emetteur = InetAddress.getByName("localhost");

            DatagramPacket dp = new DatagramPacket(buffer, buffer.length, emetteur,port);
            mainLog.info("ip : " + emetteur);
            mainLog.info("port : " + port);
            mainLog.info("msg");

            // 2. Création d'un datagramSocket
            try{
                DatagramSocket ds = new DatagramSocket();
                // 3. Emettre et attendre
                ds.send(dp);
                mainLog.info("Un datagram paquet est émis");
            }catch(IOException e){
                mainLog.error(e.getMessage());
            }

        } catch(IOException ioe) {
            mainLog.error(ioe.getMessage());
        }
    }
}