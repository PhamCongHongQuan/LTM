import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class TCPClient {
    private Socket mySocket;
    private BufferedReader is;
    private BufferedWriter os;

    public void connection() throws IOException{
        mySocket = new Socket("36.50.135.242",2208);
        mySocket.setSoTimeout(5000);

        is = new BufferedReader(
                new InputStreamReader(
                        mySocket.getInputStream(),
                        StandardCharsets.UTF_8
                )
        );
        os= new BufferedWriter(
                new OutputStreamWriter(
                        mySocket.getOutputStream(),
                        StandardCharsets.UTF_8
                )
        );
    }
    public void send(String str) throws IOException{
        os.write(str);
        os.newLine();
        os.flush();
    }

    public String receive() throws IOException{
        return is.readLine();
    }

    public String filterEdu(String data) {
        String[] domains= data.split(",");
        StringJoiner result= new StringJoiner(", ");
        for (String item: domains) {
            String domain= item.trim();
            if (domain.endsWith(".edu")) {
                result.add(domain);
            }
        }
        return result.toString();
    }
    public void close() throws IOException {
        if (mySocket != null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPClient client= new TCPClient();
        try {
            client.connection();
            client.send("B23DCCN682;ve7H9iGy");
            String data= client.receive();
            if(data==null) {
                throw new IOException(
                        "Server dong ket noi truoc khi gui du lieu"
                );
            }
            System.out.println("Nhan tu sever: " + data);
            String result= client.filterEdu(data);
            client.send(result);
            System.out.println("Da gui: " + result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
