import java.io.*;
import java.net.Socket;

public class TCPCount {
    private Socket mySocket;
    private BufferedReader is;
    private BufferedWriter os;

    public void connection() throws IOException{
        mySocket = new Socket("36.50.135.242",2208);
        is= new BufferedReader(
                new InputStreamReader(mySocket.getInputStream())
            );
        os = new BufferedWriter(
                new OutputStreamWriter(mySocket.getOutputStream())
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
    public String solve(String data) {
        String buffer = data.replace(" ", "");
        int[] cnt = new int[Character.MAX_VALUE+1];
        for (char c: buffer.toCharArray()) {
            cnt[c]++;
        }
        StringBuilder result = new StringBuilder();
        for(char c:buffer.toCharArray()) {
            if(cnt[c] > 1) {
                result.append(c).append(":").append(cnt[c]).append(",");
                cnt[c]=0;
            }
        }

        return result.toString();
    }
    public void close() throws IOException{
        mySocket.close();
    }

    public static void main(String[] args) {
        TCPCount client = new TCPCount();
        try{
            client.connection();
            client.send("B23DCCN682;QmmurRCe");
            String data= client.receive();
            System.out.println("da nhan: " + data);
            String result = client.solve(data);
            client.send(result);
            System.out.println("da gui: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
