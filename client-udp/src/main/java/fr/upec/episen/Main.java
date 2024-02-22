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
        int number = 0;
        String message = "";
        // 1. Traiter les paramètres éventuels sur la ligne de commande
            //tester 4 params car modif du plugin
        if(args.length == 4){ //machine du serveur et port
            //s'il y en a, les utiliser
            adrValue = args[0];
            String portString = args[1];
            portValue = Integer.parseInt(portString);
            String numberString = args[2];
            number = Integer.parseInt(numberString);
            message = args[3];
        } else { //Sinon utiliser les propriétés par défaut du fichier properties
            adrValue = props.getProperty("udp.adress");
            portValue = Integer.parseInt(props.getProperty("udp.port"));
            number = Integer.parseInt(props.getProperty("msg.number"));
            message = props.getProperty("msg.message");
        }

            // 2. Créer un client
            Main client = new Main(adrValue, portValue, number, message, SIZE);

            // 3.
            client.run();

        } catch(IOException ioe){
            mainLog.error((ioe.getMessage()));
        }
    }

    protected Integer port;
    protected Integer packetSize;
    protected InetAddress address;
    protected  Message message;


    public Main(final String adrValue, final int portValue, final int number, final String info, final int size) throws UnknownHostException{
        this.port = portValue; //port coté serveur
        this.packetSize = size;
        this.address = InetAddress.getByName(adrValue); //adrersse serveur
        this.message = new Message(number, info);
        //Informations pour répondre.
        this.message.setFromIP(InetAddress.getLocalHost().getHostAddress());
        this.message.setFromPort(3232);
    }

    public void run(){
        //Faire un aller retour -> faire un MEP : request / response
        // 1. Faire la requête :
        ObjectMapper mapper = new ObjectMapper();
        byte[] body = new byte[this.packetSize];
        DatagramSocket socket = null;

        try{
        body = mapper.writeValueAsBytes(this.message); //transcris le message en binaire
            //Dependance pour faire du json : jackson-databind qui permet de créer un objet mapper de type object mapper.
            //object mapper : permet d'encoder une classe qui a des get et des set en json automatiquement
        DatagramPacket packet = new DatagramPacket(body, body.length, this.address, this.port);
            socket = new DatagramSocket();
            socket.send(packet);
            mainLog.info("request : " + this.message.toString());
            // 2. TODO : attendre la future response
            DatagramSocket responseSocket = new DatagramSocket(3232, InetAddress.getLocalHost());
            byte[] bodyResponse = new byte[this.packetSize];
            DatagramPacket responsePacket = new DatagramPacket(bodyResponse, this.packetSize);
            responseSocket.receive(responsePacket);
            mainLog.info(responsePacket.getData());
            responseSocket.close();
            mainLog.info("client-udp stopped");
        }  catch(IOException ioe){
            mainLog.error(ioe.getMessage());
        } finally {
            socket.close();
        }

    }
}