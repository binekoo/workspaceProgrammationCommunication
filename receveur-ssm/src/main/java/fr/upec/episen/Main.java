package fr.upec.episen;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();
    public static void main(String[] args) {
        mainLog.warn("receveur-ssm started");
        //1. Lire les propriétés du projet
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try{
            props.load(is);
            //2. Créer les processus receveur
            mainLog.warn("process creation");
            final int NB_PROCESS = Integer.parseInt(props.getProperty("nb.process"));
            final String processCmd = props.getProperty("process.cmd");
            mainLog.warn(System.getProperty("user.dir"));
            
            for(int index = 0; index < NB_PROCESS; index++){
                String command = MessageFormat.format(processCmd, Integer.toString(index));
                String[] commands = command.split(" ");
                //Création de variables d'environnement pour partager ces infos aux processus générés
                ProcessBuilder builder = new ProcessBuilder(commands);
                builder.directory(new File(System.getProperty("user.dir")));
                Map<String, String> environment = builder.environment();
                environment.put("UDP_ADRESS", props.getProperty("udp.address"));
                environment.put("UDP_PORT", props.getProperty("udp.port"));
                File log = new File("Log" + index);
                builder.redirectErrorStream(true);
                builder.redirectError(log);
                Process process = builder.start();
                mainLog.warn("process num = " + process.pid());
              //  Thread.sleep(Duration.ofMinutes(4).toMillis());
            }
        } catch(Exception e){
            mainLog.error(e.getMessage());
        } finally{
            mainLog.warn("receveur-ssm stopped");
        }
    }
}