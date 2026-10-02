import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPMinDistance {
    Socket mySocket;
    private InputStream is;
    private OutputStream os;

    public void connection() throws IOException {
        mySocket= new Socket("36.50.135.242",2206);
        mySocket.setSoTimeout(5000);

        is=mySocket.getInputStream();
        os=mySocket.getOutputStream();
    }
    public void send(String str) throws IOException {
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        os.write(data);
        os.flush();
    }
    public String receive() throws IOException{
        byte[] buffer= new byte[65536];
        int count= is.read(buffer);
        if(count==-1) {
            throw new IOException("Server dong ket noi");
        }
        return new String(
                buffer,0, count,StandardCharsets.UTF_8
        ).trim();
    }
    public String solve(String data) {
        String[] parts= data.split(",");
        if(parts.length<2){
            throw new IllegalArgumentException("can it nhat hai so de co khoang cach");
        }
        int[] numbers= new int[parts.length];
        for (int i=0; i< parts.length;i++) {
            numbers[i]= Integer.parseInt(parts[i].trim());
        }
        Arrays.sort(numbers);
        long minDistance= Long.MAX_VALUE;
        int first=0;
        int second=0;
        for(int i=0; i< numbers.length-1;i++) {
            long distance= (long) numbers[i+1]-numbers[i];
            if(distance <=minDistance) {
                minDistance=distance;
                first=numbers[i];
                second=numbers[i+1];
            }
        }
        return minDistance + "," + first + "," + second;
    }
    public void close() throws IOException {
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPMinDistance client = new TCPMinDistance();

        try{
            client.connection();
            client.send("B23DCCN682;iWv77SgM");
            String data = client.receive();
            System.out.println("Nhan tu server: "+data);
            String result = client.solve(data);
            client.send(result);
            System.out.println("Da gui: " + result);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
