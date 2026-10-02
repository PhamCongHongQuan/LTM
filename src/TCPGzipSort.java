import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCPGzipSort {
    private Socket mySocket;
    private BufferedWriter os;
    private GZIPInputStream gzipIn;
    private GZIPOutputStream gzipOut;

    public void connection() throws IOException {
        mySocket = new Socket("36.50.135.242", 2210);
        mySocket.setSoTimeout(5000);
        mySocket.setTcpNoDelay(true);

        gzipOut = new GZIPOutputStream(
                mySocket.getOutputStream(), true
        );

        os = new BufferedWriter(
                new OutputStreamWriter(gzipOut, StandardCharsets.UTF_8)
        );
    }

    public void send(String str) throws IOException {
        os.write(str);
        os.write('\n');
        os.flush();
    }

    public String receive() throws IOException {
        if (gzipIn == null) {
            gzipIn = new GZIPInputStream(mySocket.getInputStream());
        }

        ByteArrayOutputStream lineBytes = new ByteArrayOutputStream();
        int b;

        while ((b = gzipIn.read()) != -1) {
            if (b == '\n') {
                break;
            }

            lineBytes.write(b);
        }

        if (b == -1 && lineBytes.size() == 0) {
            throw new IOException(
                    "Server dong ket noi truoc khi gui du lieu."
            );
        }

        return lineBytes.toString(StandardCharsets.UTF_8);
    }

    public String solve(String data) {
        char[] characters = data.toCharArray();
        Arrays.sort(characters);
        return new String(characters);
    }

    public void close() {
        try {
            if (mySocket != null && !mySocket.isClosed()) {
                mySocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPGzipSort client = new TCPGzipSort();

        try {
            client.connection();
            client.send("B23DCCN682;Z92xLzeS");

            String data = client.receive();
            System.out.println("Nhan: " + data);

            String result = client.solve(data);
            client.send(result);

            System.out.println("Da gui: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}