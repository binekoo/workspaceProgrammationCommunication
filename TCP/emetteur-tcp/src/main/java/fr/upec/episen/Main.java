package fr.upec.episen;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.upec.episen.generated.Message;


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
            final int PORT = Integer.parseInt(props.getProperty("tcp.port"));
            final String ADDR = props.getProperty("tcp.address");
            InetAddress  address = InetAddress.getByName(ADDR);
            //On se connecte au receveur ici :
            Socket socket = new Socket(address, PORT);
            mainLog.warn("emetteur-tcp connected");
            OutputStream os = socket.getOutputStream();
            //TODO : remplacer par un objet d'une classe générée (pas réussi du coup classe Message en dur)
            Message msg = new Message();
            msg.setInfo("Message du vendredi");
            msg.setCreation("03-01-2024T11:03:03");
            msg.setOrder(1);
            ObjectMapper mapper = new ObjectMapper();
            os.write(mapper.writeValueAsBytes(msg));
            Thread.sleep(100000);
        } catch(Exception e){
            mainLog.error(e.getMessage());
        } finally {
            mainLog.warn("emetteur-tcp stopped");
        }
    }
}