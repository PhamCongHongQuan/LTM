import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class UDPTitleCase {
    private DatagramSocket mySocket;
    private InetAddress serverAddress;
    private int serverPort=2208;

    public void connection() throws IOException{
        serverAddress= InetAddress.getByName("36.50.135.242");
        mySocket = new DatagramSocket();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet= new DatagramPacket(data, data.length,serverAddress,serverPort);
        mySocket.send(packet);
    }
    public String receive() throws IOException{
        byte[] buffer= new byte[66535];
        DatagramPacket packet= new DatagramPacket(buffer, buffer.length);
        mySocket.receive(packet);
        return new String(
                packet.getData(),
                packet.getOffset(),
                packet.getLength(),
                StandardCharsets.UTF_8
        );
    }
    public String solve(String message) {
        String[] parts= message.split(";",2);
        if (parts.length!=2) {
            throw new IllegalArgumentException("Du lieu ko hop le");
        }
        String requestId= parts[0];
        String[] data= parts[1].split("\\s+");
        StringJoiner res= new StringJoiner(" ");
        for (String item:data) {
            String a="";
            a+=item.substring(0,1).toUpperCase()+item.substring(1).toLowerCase();
            res.add(a);
        }
        return requestId + ";" + res.toString();
    }
    public void close() {
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        UDPTitleCase client= new UDPTitleCase();
        try{
            client.connection();
            client.send(";B23DCCN682;JG85wp5z");
            String data= client.receive();
            System.out.println("da nhan " + data);
            String res= client.solve(data);
            client.send(res);
            System.out.println("da gui "+ res);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
