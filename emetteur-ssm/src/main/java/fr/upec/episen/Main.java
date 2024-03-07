package fr.upec.episen;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.upec.episen.generated.Message;

public class Main {

    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
       mainLog.warn("emetteur-ssm started");
        //1. Lire les propriétés du projet
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try{
            props.load(is);
            //2. Etablir une connexion TCP :
            final String ADDR = props.getProperty("udp.address");
            final int PORT = Integer.parseInt(props.getProperty("udp.port"));
            //Adresse IP pour le groupe.
            InetAddress group = InetAddress.getByName(ADDR);
            MulticastSocket socket = new MulticastSocket(PORT);
            socket.joinGroup(group);
            //3. Préparation du message à diffuser
            Message msg = new Message();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            msg.setCreation(dateFormat.format(new Date()));
            msg.setInfo("Encore une heure à attendre la pause");
            msg.setOrder(1);

            //Convertir le message en binaire
            ObjectMapper mapper = new ObjectMapper();
            byte[] content = mapper.writeValueAsBytes(msg);
            DatagramPacket dp = new DatagramPacket(content, content.length, group,PORT);
            socket.send(dp);
            mainLog.warn("Emission de : " + new String(content));
            Thread.sleep(Duration.ofMinutes(1).toMillis());
            //4.Fin de la participation au groupe
            Thread.sleep(Duration.ofMinutes(1).toMillis());
            socket.leaveGroup(group);
        } catch(Exception ioe){
            mainLog.error(ioe.getMessage());
        }
    }
}