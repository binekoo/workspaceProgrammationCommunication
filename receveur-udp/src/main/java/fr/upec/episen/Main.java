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
        mainLog.info("receveur-udp started");
    
        try{
            // 0. Chagement du fichier de properties
            InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            props.load(iStream);

            // 1. Crétation du datagram paquet vide

            byte[] buffer = new byte[25];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);

            Integer port = Integer.valueOf(props.getProperty("udp.port", "1024"));
            InetAddress receveur = InetAddress.getLocalHost();

            // 2. Création d'une datagram socket pour recevoir le datagram paquet
            try(DatagramSocket ds = new DatagramSocket(port, receveur)){
                // 3. Attendre un message
                ds.receive(dp);
                buffer = dp.getData();
            }

            mainLog.info("Un datagram paquet est reçu");

            // 4. Affichage du message dans un logger local
            String msgRecu = new String(dp.getData(), 0, dp.getLength());
            mainLog.info("Message reçu : " + msgRecu);

        } catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        }
    }
}