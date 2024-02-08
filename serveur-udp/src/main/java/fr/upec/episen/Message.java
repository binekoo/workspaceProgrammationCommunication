package fr.upec.episen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Message {
    protected static Logger msgLog = LogManager.getLogger(Message.class);
    protected Integer number;
    protected String info;

    public Message(){
        this.number = 0;
        this.info = "unknown";
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    //Écrire le message qui contient le JSON coté serveur
    //Présence de Json databind dans le pom : ok donc on peut utiliser ObjetMapper
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(this);
        } catch(JsonProcessingException jpe){
            msgLog.error(jpe.getMessage());
        }
        return result;
    }
}
