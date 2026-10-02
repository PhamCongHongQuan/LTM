import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TCPGzip {
    private Socket mySocket;
    private BufferedWriter os;
    private GZIPInputStream gzipIn;
    private GZIPOutputStream gzipOut;

    public void connection() throws IOException {
        mySocket = new Socket("36.50.135.242", 2210);
        mySocket.setSoTimeout(5000);
        mySocket.setTcpNoDelay(true);

        gzipOut = new GZIPOutputStream(mySocket.getOutputStream(), true);
        os = new BufferedWriter(new OutputStreamWriter(gzipOut, StandardCharsets.UTF_8));
    }

    public void send(String str) throws IOException {
        os.write(str);
        os.write('\n');
        os.flush();
    }

    // Doc THU CONG tung byte, dung ngay khi gap '\n'
    // -> khong bao gio doi doc them sau khi da du 1 dong,
    //    tranh trigger loi "Unexpected end of ZLIB input stream"
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
            throw new IOException("Server dong ket noi truoc khi gui du lieu.");
        }
        return lineBytes.toString(StandardCharsets.UTF_8);
    }

    public String solve(String data) {
        String reversed = new StringBuilder(data).reverse().toString();
        String encoded = Base64.getEncoder().encodeToString(
                reversed.getBytes(StandardCharsets.UTF_8)
        );
        return reversed + "|" + encoded;
    }

    public void close() {
        try {
            if (mySocket != null && !mySocket.isClosed()) mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TCPGzip client = new TCPGzip();
        try {
            client.connection();

            client.send("B23DCCN682;DSMvkEla");
            String data = client.receive();
            System.out.println("Nhan: " + data);

            String result = client.solve(data);
            client.send(result);
            System.out.println("Da gui: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}