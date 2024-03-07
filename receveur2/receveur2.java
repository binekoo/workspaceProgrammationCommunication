import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class receveur2 {
    private int port = 2000;

    public receveur2(){
        
        try {
            System.out.println("Receveur lancéeeee");
        byte[] byter = new byte[25];

        DatagramPacket packet = new DatagramPacket(byter, byter.length);
        DatagramSocket socket = new DatagramSocket(port);

        socket.receive(packet);
        byter = packet.getData();
        String message = new String(byter, 0, byter.length);

        System.out.println(message);
        System.out.println("je suis passé pr là");
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("une erreur");
        }

    }

    public static void main(String[] args) {

       receveur2 receveur = new receveur2();
    }

}

