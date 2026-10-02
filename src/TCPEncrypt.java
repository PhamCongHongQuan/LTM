import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPEncrypt {
    private Socket mySocket;
    private DataInputStream is;
    private DataOutputStream os;

    public void connection() throws IOException{
        mySocket = new Socket("36.50.135.242",2207);
        is= new DataInputStream(mySocket.getInputStream());
        os= new DataOutputStream(mySocket.getOutputStream());
    }
    public void send(String str) throws IOException{
        os.writeUTF(str);
        os.flush();
    }
    private String solve(String data, int s) {
        int shift = (s % 26 + 26) % 26;
        StringBuilder result = new StringBuilder();
        for(char c:data.toCharArray()) {
            if (c>='A' && c<='Z') {
                int position = c-'A';
                int newPosition = (position - shift + 26) %26;
                char decode=(char) ('A'+newPosition);
                result.append(decode);
            }
            else if(c>='a' && c<='z') {
                int position = c-'a';
                int newPosition = (position - shift +26) %26;
                char decode=(char) ('a'+newPosition);
                result.append(decode);
            } else {
                result.append(c);
            }

        }
        return result.toString();
    }
    public void close() throws IOException{
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPEncrypt client = new TCPEncrypt();
        try{
            client.connection();
            client.send("B23DCCN682;TdaDEc6V");
            String data= client.is.readUTF();
            int s= client.is.readInt();
            System.out.println("Chuoi nhan: "+data);
            System.out.println("Dich chuyen: "+s);
            String result=client.solve(data,s);
            client.send(result);
            System.out.println("Da gui: " + result);

        }catch (IOException e) {
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
