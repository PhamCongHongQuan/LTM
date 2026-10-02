import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TCPMaxSecond {
    private Socket mySocket;
    private InputStream is;
    private OutputStream os;

    public void connection() throws IOException{
        mySocket= new Socket("36.50.135.242",2206);
        is= mySocket.getInputStream();
        os= mySocket.getOutputStream();
    }
    public void send(String str) throws IOException{
        byte[] data= str.getBytes(StandardCharsets.UTF_8);
        os.write(data);
        os.flush();
    }
    public String receive() throws IOException{
        byte[] buffer= new byte[66536];
        int count = is.read(buffer);
        if (count==-1) {
            throw new IOException("Server dong ket noi");
        }
        return new String(buffer,0,count,StandardCharsets.UTF_8);
    }
    public String solve(String data) {
        String[] parts = data.split(",");

        if (parts.length < 2) {
            throw new IllegalArgumentException("Phai co tu hai so tro len");
        }

        int[] numbers = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Integer.parseInt(parts[i].trim());
        }

        // Sap xep ban sao de giu nguyen vi tri trong mang goc
        int[] sorted = numbers.clone();
        Arrays.sort(sorted);

        int max = sorted[sorted.length - 1];
        int i = sorted.length - 2;

        // Bo qua cac phan tu bang gia tri lon nhat
        while (i >= 0 && sorted[i] == max) {
            i--;
        }

        if (i < 0) {
            throw new IllegalArgumentException(
                    "Khong co gia tri lon thu hai phan biet"
            );
        }

        int secondMax = sorted[i];

        // Tim vi tri xuat hien dau tien trong mang ban dau
        int index = 0;

        while (numbers[index] != secondMax) {
            index++;
        }

        return secondMax + "," + index;
    }
    public void close() throws IOException{
        if(mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPMaxSecond client= new TCPMaxSecond();
        try{
            client.connection();
            client.send("B23DCCN682;VRFcK0d2");
            String data= client.receive();
            System.out.println("da nhan: " + data);
            String result=client.solve(data);
            client.send(result);
            System.out.println("da gui: " + result);
        }catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
