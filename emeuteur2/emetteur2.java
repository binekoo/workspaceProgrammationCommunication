import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class emetteur2 {
    private int port = 2000;
    private String ip = "localhost";

    public void emetteur2m(){
        try {
            System.out.println("juste un test");
            String msg = "yo";
        byte[] byter = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(byter, byter.length, InetAddress.getByName(ip), port);

        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        System.out.println("messaage envoyé");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public static void main(String[] args) {
        emetteur2 emetteur = new emetteur2();
        emetteur.emetteur2m();
    }

    
}
