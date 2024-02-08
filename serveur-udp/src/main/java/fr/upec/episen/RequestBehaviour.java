package fr.upec.episen;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class RequestBehaviour implements Callable<Boolean> {
    protected static Logger rbLog = LogManager.getLogger(RequestBehaviour.class);
    protected Message message;

    public RequestBehaviour(DatagramPacket packet){
        byte[] body = packet.getData();
        ObjectMapper mapper = new ObjectMapper();
        this.message = new Message();
        try{
            this.message = mapper.readValue(body, Message.class);
        } catch(IOException ioe){
            rbLog.error(ioe.getMessage());
        }
    }

    @Override
    public Boolean call() throws Exception {
        processRequest();
        saveMessage();
        return true;
    }

    public void processRequest(){
            rbLog.info("msg received : " + message.toString());
    }

    //Écriture en base
    public Boolean saveMessage(){
        //Effectuer une requête sur messagedb :
        rbLog.info("connexion : " + Connexion.instance.toString());
        //pas encore de toString dans connexion donc affichage pas ouf mais ok.
        return true;
    }
}
