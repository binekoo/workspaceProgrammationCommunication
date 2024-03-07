package fr.upec.episen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//Classe principale du process
public class Receveur {
        protected static Logger mainLog = LogManager.getLogger(Receveur.class);
        public static void main(String[] args) {
            mainLog.warn("Receveur started");
            final String UDP_ADDR = System.getenv("UDP_ADDRESS");
            final String UDP_PORT = System.getenv("UDP_PORT");
            mainLog.warn("adresse : " +  UDP_ADDR);
            mainLog.warn("port : " + UDP_PORT);
        }

}
