import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class UDPMissingNumbers {
    private DatagramSocket mySocket;
    private InetAddress serverAddress;
    private int serverPort = 2207;

    public void connection() throws IOException {
        serverAddress= InetAddress.getByName("36.50.135.242");
        mySocket= new DatagramSocket();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet= new DatagramPacket(data,data.length,serverAddress,serverPort);
        mySocket.send(packet);
    }
    public String receive() throws IOException{
        byte[] buffer= new byte[66535];
        DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
        mySocket.receive(packet);
        return new String(
                packet.getData(),
                packet.getOffset(),
                packet.getLength(),
                StandardCharsets.UTF_8
        );
    }
    public String solve(String data) {
        String[] parts= data.split(";",3);
        if (parts.length!=3) {
            throw new IllegalArgumentException("Du lieu khong dung dinh dang");
        }
        String requestId= parts[0];
        int n= Integer.parseInt(parts[1].trim());
        boolean[] appeared= new boolean[n+1];
        if (!parts[2].trim().isEmpty()) {
            String[] numbers= parts[2].split(",");
            for (String item : numbers) {
                int value= Integer.parseInt(item.trim());
                if(value>=1 && value <=n) {
                    appeared[value]= true;
                }
            }
        }
        StringJoiner result= new StringJoiner(",");
        for (int i=1;i<=n;i++) {
            if (!appeared[i]) {
                result.add(String.valueOf(i));
            }
        }
        return requestId + ";" + result.toString();
    }
    public void close() {
        if(mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        UDPMissingNumbers client = new UDPMissingNumbers();
        try{
            client.connection();
            client.send(";B23DCCN682;An19ltlF");
            String data= client.receive();
            System.out.println("Nhan" + data);
            String result= client.solve(data);
            client.send(result);
            System.out.println("Gui " + result);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

}
