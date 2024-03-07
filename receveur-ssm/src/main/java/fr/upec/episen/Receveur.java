package fr.upec.episen;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.upec.episen.generated.Message;

//Classe principale du process
//Ne s'execute que depuis le jar
public class Receveur {
        protected static Logger mainLog = LogManager.getLogger(Receveur.class);
        @SuppressWarnings("deprecation")
        public static void main(String[] args) {
            mainLog.warn("Receveur started");
            final String UDP_ADDR = System.getenv("UDP_ADDRESS");
            final int UDP_PORT = Integer.parseInt(System.getenv("UDP_PORT"));
            
            mainLog.warn("adresse : " +  UDP_ADDR);
            mainLog.warn("port : " + UDP_PORT);
            
            //2. Rejoindre le groupe de diffusion
            try{
                InetAddress group = InetAddress.getByName(UDP_ADDR);
                MulticastSocket socket = new MulticastSocket(UDP_PORT);
                mainLog.warn("Receveur : attente de reception");
                byte[] content = new byte[255];
                DatagramPacket dp = new DatagramPacket(content, content.length);
                socket.receive(dp);
                byte[] body = dp.getData();
                ObjectMapper mapper = new ObjectMapper();
                Message msg = mapper.readValue(body, Message.class);
                mainLog.warn("receveur : " + msg.toString());
                Thread.sleep(Duration.ofMinutes(1).toMillis());
                socket.joinGroup(group);
            } catch(Exception e){
                mainLog.error(e.getMessage());
            } finally{
                mainLog.warn("receveur stopped");
            }
        }

}
