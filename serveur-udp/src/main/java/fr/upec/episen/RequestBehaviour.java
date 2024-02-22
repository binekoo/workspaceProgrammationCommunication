package fr.upec.episen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class RequestBehaviour implements Callable<Boolean> {
    protected static Logger rbLog = LogManager.getLogger(RequestBehaviour.class);
    protected Message message;
    protected Integer messageId;

    public RequestBehaviour(DatagramPacket packet){
        byte[] body = packet.getData();
        ObjectMapper mapper = new ObjectMapper();
        this.message = new Message();
        try{
            this.message = mapper.readValue(body, Message.class);
        } catch(IOException ioe){
            rbLog.error(ioe.getMessage());
        }
        this.messageId = 0;
    }

    @Override
    public Boolean call() throws Exception {
        processRequest();
        saveMessage();
        processResponse();
        return true;
        //Todo: modifier le return
    }

    public void processRequest(){
            rbLog.info("msg received : " + message.toString());
    }

    //Écriture en base
    public Boolean saveMessage(){
        //Effectuer une requête sur messagedb :
        //Connexion SQL :
        Connection connection = Connexion.instance.getConnection();
        String insertSQL = Main.props.getProperty("messages.insert");
        try{
            PreparedStatement pstmt = connection.prepareStatement(insertSQL, ResultSet.TYPE_SCROLL_SENSITIVE);
            //Initialisation de la requête (remplissage de la requete preparée des props) :
            pstmt.setInt(1, this.message.getNumber());
            pstmt.setString(2, this.message.getInfo());
            //Execution de la requete :
            ResultSet rs = pstmt.executeQuery();
            //next renvoie un boolean donc le log s'affiche si le resultset est non vide.
            if(rs != null && rs.first()) { this.messageId = rs.getInt(messageId);}
        } catch(SQLException sqle){
            rbLog.error(sqle.getMessage());
            return false;
        }
        return true;
    }

    public Boolean processResponse(){
        // 1.Créer une réponse
        Response response = new Response(messageId, message.getNumber(), message.getInfo());
        ObjectMapper mapper = new ObjectMapper();
        //Besoin de transformer la réponse en binaire
        try{
            byte[] body = mapper.writeValueAsBytes(response);
            // 2.Créer un DP
            DatagramPacket packet = new DatagramPacket(body, body.length);
            // 3. Emettre sur un DS
            DatagramSocket socket = new DatagramSocket(message.getFromPort(), InetAddress.getByAddress(message.getFromIP().getBytes()));
            socket.send(packet);
            return Boolean.TRUE;
        }catch(Exception e){
            rbLog.error(e.getMessage());
        }
        return Boolean.FALSE;
    }
}
