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
            //final int SIZE = Integer.valueOf(props.getProperty("msg.size.max", "500"));
            System.out.println("fichier de props chargé");

            // 1. Crétation du datagram paquet vide
            //String achatContenu = "{}";
            byte[] buffer = new byte[100];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            System.out.println("DP créé");

            Integer port = Integer.valueOf(props.getProperty("udp.port", "1023"));
            InetAddress receveur = InetAddress.getLocalHost();
            //System.out.println(receveur);

            System.out.println("numero de port et adrese ip récupérés");

            // 2. Création d'une datagram socket pour recevoir le datagram paquet
            try(DatagramSocket ds = new DatagramSocket(port, receveur)){

                System.out.println("DS créee");
                // 3. Attendre un message
                ds.receive(dp);
                System.out.println("message recu");
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