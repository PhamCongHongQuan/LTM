import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPMostFrequent {
    private DatagramSocket  mySocket;
    private InetAddress serverAddress;
    private int serverPort=2208;

    public void connection() throws IOException{
        serverAddress = InetAddress.getByName("36.50.135.242");
        mySocket = new DatagramSocket();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,serverPort);
        mySocket.send(packet);
    }
    public String receive() throws IOException{
        byte[] buffer = new byte[66535];
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
        mySocket.receive(packet);
        return new String(
               packet.getData(),
               packet.getOffset(),
               packet.getLength(),
                StandardCharsets.UTF_8
        );
    }
    public String solve(String data) {
        String[] part= data.split(";",2);
        if (part.length!=2) {
            throw new IllegalArgumentException("Du lieu loi");
        }
        String requestId= part[0];
        int[] cnt = new int[Character.MAX_VALUE+1];
        for(char c: part[1].toCharArray()) {
            cnt[c]++;
        }
        char most= part[1].charAt(0);
        for (char c:part[1].toCharArray()) {
            if(cnt[c] > cnt[most]) {
                most=c;
            }
        }
        StringBuilder result= new StringBuilder();
        result.append(requestId).append(";").append(most).append(":");
        for (int i=0;i<part[1].length();i++) {
            if (part[1].charAt(i)==most) {
                result.append(i+1).append(",");
            }
        }
        return result.toString();
    }
    public void close() {
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        UDPMostFrequent client = new UDPMostFrequent();
        try{
            client.connection();
            client.send(";B23DCCN682;r5v2vebs");
            String data= client.receive();
            String result= client.solve(data);
            client.send(result);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
    }
}
