package fr.upec.episen;

import org.apache.logging.log4j.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;

public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();

    public static void main(String[] args) {
        mainLog.info("serveur-udp started");

        try{
            // 0. Chargement du fichier de properties
            InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            props.load(iStream);
            final int SIZE = Integer.valueOf(props.getProperty("msg.size.max", "500"));


            // 1. Traitement des paramètres de la ligne de commande
            int portValue = 0;
            if(args.length == 1){
                String portString = args[0];
                portValue = Integer.parseInt(portString);
            } else {
                //En absence de paramètres, on utilise les propriétés de l'application
                portValue = Integer.parseInt(props.getProperty("udp.port"));
            }

            // 2. Création du serveur UDP
            Main server = new Main(portValue, SIZE);

            // 3. Démarrage du serveur
            server.run();

        } catch(Exception e){
            e.getMessage();
        }
    }

    protected Integer receptionPort;
    protected Integer packetSize;
    protected boolean active;
    protected DatagramSocket socket;

    public Main(final int port, final int packetSize)throws SocketException{
        this.receptionPort = port;
        this.active = true;
        this.socket = new DatagramSocket(receptionPort);
        this.packetSize = packetSize;
    }

    public void run() throws ClassNotFoundException{
        byte[] body = new byte[this.packetSize];
        while(this.active){
            DatagramPacket packet = new DatagramPacket(body, body.length);
            try{
                this.socket.receive(packet);
                processRequest(packet.getData());
                //TODO processResponse()
            } catch(IOException ioe){
                mainLog.error(ioe.getMessage());
            }
        }
    }

    public void processRequest(final byte[] data){
        //Lire les donées pour les afficher dans le logger
        mainLog.info("data received : " + new String(data)); //Affichage de bytes transformés en String (objet de classe Message)
         //on a encodé coté client et mtn on veut décoder coté serveur.

//        ByteArrayInputStream bais = new ByteArrayInputStream(data);
//        try{
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            String body = ois.readObject().toString();
//            mainLog.info("body received : " + body);
//            //TODO : sauvegarde du body
//        } catch(Exception ioe){
//            mainLog.error(ioe.getMessage());
//        }
    }
}