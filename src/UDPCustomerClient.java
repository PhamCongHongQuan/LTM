import UDP.Customer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class UDPCustomerClient {
    private DatagramSocket mySocket;
    private InetAddress serverAddress;
    private int serverPort = 2209;
    private byte[] requestID= new byte[8];

    public void connection() throws IOException{
        serverAddress = InetAddress.getByName("36.50.135.242");
        mySocket = new DatagramSocket();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(data, data.length,serverAddress,serverPort);
        mySocket.send(packet);
    }
    public Customer receive() throws IOException, ClassNotFoundException{
        byte[] buffer= new byte[65535];
        DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
        mySocket.receive(packet);
        if (packet.getLength()<=8) {
            throw new IOException("Goi tin ko du du lieu tu Cusomer");
        }
        System.arraycopy(
                packet.getData(),
                packet.getOffset(),
                requestID,
                0,8
        );
        ByteArrayInputStream byteIn= new ByteArrayInputStream(
                packet.getData(),
                packet.getOffset()+8,
                packet.getLength()-8
        );
        try(ObjectInputStream objectIn = new ObjectInputStream(byteIn)) {
            return (Customer) objectIn.readObject();
        }
    }
    public void solve(Customer customer) {
        String name= customer.getName();
        if (name==null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ten khach hang rong");
        }
        String[] words= name.trim().toLowerCase(Locale.ROOT).split("\\s+");
        String ten=words[words.length-1];
        StringBuilder res = new StringBuilder(ten.toUpperCase(Locale.ROOT));
        res.append(",");
        for (int i=0;i< words.length-1;i++) {
            res.append(" ");
            res.append(words[i].substring(0,1).toUpperCase(Locale.ROOT)+words[i].substring(1));
        }
        customer.setName(res.toString());
        String ns=customer.getDayOfBirth();
        String[] parts= ns.trim().split("-");
        StringBuilder result = new StringBuilder();
        result.append(parts[1]).append("/").append(parts[0]).append("/").append(parts[2]);
        customer.setDayOfBirth(result.toString());


        StringBuilder usna= new StringBuilder();
        for(int i=0;i< words.length-1;i++) {
            usna.append(words[i].charAt(0));
        }
        usna.append(ten);
        customer.setUserName(usna.toString());
    }
    public void send(Customer customer) throws IOException{
        byte[] objectData;
        ByteArrayOutputStream byteOut= new ByteArrayOutputStream();
        try(ObjectOutputStream objOut=new ObjectOutputStream(byteOut)) {
            objOut.writeObject(customer);
            objOut.flush();
            objectData=byteOut.toByteArray();
        }
        byte[] data = new byte[8+objectData.length];
        System.arraycopy(requestID,0,data,0,8);
        System.arraycopy(objectData,0,data,8,objectData.length);
        DatagramPacket packet= new DatagramPacket(
                data,data.length,serverAddress,serverPort
        );
        mySocket.send(packet);
    }
    public void close() {
        if(mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        UDPCustomerClient client = new UDPCustomerClient();
        try{
            client.connection();
            client.send(";B23DCCN682;6OciOWz9");
            Customer customer = client.receive();
            client.solve(customer);
            client.send(customer);
        } catch (IOException | ClassNotFoundException |IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
