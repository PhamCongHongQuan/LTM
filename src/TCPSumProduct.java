import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSumProduct {
    private Socket mySocket;
    private DataInputStream is;
    private DataOutputStream os;

    public void connection() throws IOException {
        mySocket = new Socket("36.50.135.242", 2207);
        mySocket.setSoTimeout(5000);

        is = new DataInputStream(mySocket.getInputStream());
        os = new DataOutputStream(mySocket.getOutputStream());
    }

    // Gui chuoi ma sinh vien va ma cau hoi
    public void sendCode(String str) throws IOException {
        os.writeUTF(str);
        os.flush();
    }

    // Nhan mot so nguyen
    public int receiveInt() throws IOException {
        return is.readInt();
    }

    // Gui tong truoc, tich sau
    public void sendResult(int sum, int product) throws IOException {
        os.writeInt(sum);
        os.writeInt(product);
        os.flush();
    }

    public void close() throws IOException {
        if (mySocket != null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPSumProduct client = new TCPSumProduct();

        try {
            client.connection();

            // a. Thay MA_CAU_HOI bang ma sao chep tren trang
            client.sendCode("B23DCCN682;L0oZA6rA");

            // b. Nhan lan luot a va b
            int a = client.receiveInt();
            int b = client.receiveInt();

            System.out.println("Nhan a = " + a + ", b = " + b);

            // c. Tinh va gui lan luot tong, tich
            int sum = a + b;
            int product = a * b;

            client.sendResult(sum, product);

            System.out.println("Da gui tong: " + sum);
            System.out.println("Da gui tich: " + product);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // d. Dong ket noi
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}