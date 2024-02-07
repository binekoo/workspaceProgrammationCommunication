package fr.upec.episen;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.logging.log4j.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    public static void main(String[] args) {
        mainLog.info("client-udp started");
        try{
            // 0. Chagement du fichier de properties
            InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            props.load(iStream);
            final int SIZE = Integer.parseInt(props.getProperty("msg.size.max"));

        String adrValue = "";
        int portValue = 0;
        // 1. Traiter les paramètres éventuels sur la ligne de commande
        if(args.length == 2){ //machine du serveur et port
            //s'il y en a, les utiliser
            adrValue = args[0];
            String portString = args[1];
            portValue = Integer.parseInt(portString);
        } else {
            //utiliser les propriétés par défaut du fichier properties
            adrValue = props.getProperty("udp.adress");
            portValue = Integer.parseInt(props.getProperty("udp.port"));
            }

            // 2. Créer un client
            Main client = new Main(adrValue, portValue, SIZE);

            // 3.
            client.run();

        } catch(IOException ioe){
            mainLog.error((ioe.getMessage()));
        }
    }

    protected Integer port;
    protected Integer packetSize;
    protected InetAddress address;

    public Main(final String adrValue, final int portValue, final int size) throws UnknownHostException{
        this.port = portValue;
        this.packetSize = size;
        this.address = InetAddress.getByName(adrValue);
    }

    public void run(){
        //Faire un aller retour -> faire un MEP : request / response
        // 1. Faire la requête :
        Message msg = new Message(3, "Nous sommes en TD3");
        ObjectMapper mapper = new ObjectMapper();
        byte[] body = new byte[this.packetSize];
        DatagramSocket socket = null;

        try{
        body = mapper.writeValueAsBytes(msg);
        DatagramPacket packet = new DatagramPacket(body, body.length, this.address, this.port);
            socket = new DatagramSocket();
            socket.send(packet);
            mainLog.info("request : " + msg.toString());
            // 2. TODO : attendre la future response
        }  catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        } finally {
            socket.close();
        }

    }
}