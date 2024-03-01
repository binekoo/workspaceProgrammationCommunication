package fr.upec.episen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Response {
    protected static Logger respLog = LogManager.getLogger(Response.class);
    protected Integer id;
    protected Integer number;
    protected String info;

    public Response(){

    }
    public Response(Integer id, Integer number, String info){
        this.id = id;
        this.number = number;
        this.info = info;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String result = mapper.writeValueAsString(this);
            return result;
        } catch(JsonProcessingException jse){
            respLog.error(jse.getMessage());
        }
        return "error";
    }
}