import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UDPMinMax {
    private DatagramSocket mySocket;
    private InetAddress serverAddress;
    private int serverPort= 2207;

    public void connection() throws IOException{
        serverAddress = InetAddress.getByName("36.50.135.242");
        mySocket= new DatagramSocket();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet= new DatagramPacket(data,data.length,serverAddress,serverPort);
        mySocket.send(packet);
    }
    public String receive() throws IOException{
        byte[] buffer= new byte[66535];
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
        String[] part= data.split(";");
        if (part.length!=2) {
            throw new IllegalArgumentException("Du lieu ko hop le");
        }
        String requestId= part[0];
        int min= Integer.MAX_VALUE;
        int max= Integer.MIN_VALUE;
        if(!part[1].trim().isEmpty()) {
            String[] numbers= part[1].trim().split(",");
            for(String item : numbers) {
                int value = Integer.parseInt(item.trim());
                if (value >max) max=value;
                if (value<min) min=value;
            }
        }
        return requestId + ";"+max+","+min;
    }
    public void close() {
        if (mySocket!=null) {
            mySocket.close();

        }
    }

    public static void main(String[] args) {
        UDPMinMax client = new UDPMinMax();
        try {
            client.connection();
            client.send(";B23DCCN682;2LnaJ4vW");
            String data= client.receive();
            String result = client.solve(data);
            client.send(result);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
