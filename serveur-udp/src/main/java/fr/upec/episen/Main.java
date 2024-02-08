package fr.upec.episen;

import org.apache.logging.log4j.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    protected static Logger mainLog = LogManager.getLogger(Main.class);
    protected static Properties props = new Properties();

    public static void main(String[] args) {
        mainLog.info("serveur-udp started");

        try{
            // 0. Chargement du fichier de properties
            InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
            props.load(iStream);
            final int SIZE = Integer.valueOf(props.getProperty("msg.size.max", "500"));


            // 1. Traitement des paramètres de la ligne de commande
            int portValue = 0;
            if(args.length == 1){
                String portString = args[0];
                portValue = Integer.parseInt(portString);
            } else {
                //En absence de paramètres, on utilise les propriétés de l'application
                portValue = Integer.parseInt(props.getProperty("udp.port"));
            }

            // 2. Création du serveur UDP
            Main server = new Main(portValue, SIZE);

            // 3. Démarrage du serveur
            server.run();

        } catch(Exception e){
            e.getMessage();
        }
    }

    protected Integer receptionPort;
    protected Integer packetSize;
    protected boolean active;
    protected DatagramSocket socket;
    protected ExecutorService threadPool;

    public Main(final int port, final int packetSize)throws SocketException{
        this.receptionPort = port;
        this.active = true;
        this.socket = new DatagramSocket(receptionPort);
        this.packetSize = packetSize;
        Executors.newFixedThreadPool(10);
    }

//    public void run() throws ClassNotFoundException{
//        byte[] body = new byte[this.packetSize];
//        while(this.active){
//            DatagramPacket packet = new DatagramPacket(body, body.length);
//            try{
//                this.socket.receive(packet);
//                //Déléguer le travail utile pour chaque client  à un thread.
//                //Créer une instance de RequestBehaviour avec le paquet recu (requestBehaviour(packet)
//                //Boolean result = this.threadPool.submit(requestBehaviour)
//                RequestBehaviour rb = new RequestBehaviour(packet);
//                //on retourne un futur boolen : quand la tache sera terminée seulement, tant que ce n'est pas le cas, on a rien
//                Future<Boolean> result = this.threadPool.submit(rb);
//                //le bool permet de tester pour savoir si le result s'est bien passé :
//                //while : juste pour attendre que le processus se termine
//                //while(!result.isDone());
//                //mainLog.info("result = " + result.get());
//            } catch(Exception ioe){
//                mainLog.error(ioe.getMessage());
//            }
//        }
//    }

    //Méthode de Nawres
    public void run() throws ClassNotFoundException {
        byte[] body = new byte[this.packetSize];
        while (this.active) { // ne s'art plus car on a fait cette boucle
            DatagramPacket packet = new DatagramPacket(body, body.length);
            try {
                this.socket.receive(packet);
                //Deleguer le travail util pour chaque client à un thread
                RequestBehaviour rb = new RequestBehaviour(packet);
                Future<Boolean> result = this.threadPool.submit(rb); // future boolean car retroune un future boolean quand la tache sera terminée
                //juste pour tester
//                    while(!result.isDone());
//                    mainLog.info("result= "+ result.get());

            } catch (IOException ioe) {
                mainLog.error(ioe.getMessage());
            }
        }
    }
}