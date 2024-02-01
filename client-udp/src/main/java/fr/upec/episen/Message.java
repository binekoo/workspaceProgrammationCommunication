package fr.upec.episen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    protected Integer number;
    protected String info;

    public Message(Integer number, String info){
        this.number = number;
        this.info = info;
    }

    public Integer getNumber(){
        return number;
    }

    public void setNumber(Integer n){
        this.number = n;
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String i){
        this.info = i;
    }

    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
       String result = "?";
       try{
        result = mapper.writeValueAsString(this);
       } catch(JsonProcessingException jpe){
            ;
       }
       return result;
    }
}
