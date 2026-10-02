import TCP.Laptop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPFix {
    private Socket mySocket;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public void connection() throws IOException{
        mySocket = new Socket("36.50.135.242",2209);
        os= new ObjectOutputStream(mySocket.getOutputStream());
        os.flush();
        is = new ObjectInputStream(mySocket.getInputStream());
    }
    public void send(Object obj) throws IOException{
        os.writeObject(obj);
        os.flush();
    }
    public Laptop receive() throws IOException, ClassNotFoundException{
        return (Laptop) is.readObject();
    }
    public void sovle(Laptop laptop) {
        String[] words = laptop.getName().trim().split("\\s+");
        String temp = words[0];
        words[0]=words[words.length-1];
        words[words.length-1]=temp;
        laptop.setName(String.join(" ",words));

        int quantity= laptop.getQuantity();
        int reserved=0;
        while (quantity>0) {
            int digit= quantity%10;
            reserved=reserved*10+digit;
            quantity/=10;
        }
        laptop.setQuantity(reserved);
    }
    public void close() throws IOException{
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPFix client= new TCPFix();
        try{
            client.connection();
            client.send("B23DCCN682;skFrsiLK");
            Laptop laptop= client.receive();
            System.out.println("Truoc sua: " + laptop.getName() + "|" +laptop.getQuantity()) ;
            client.sovle(laptop);
            client.send(laptop);
            System.out.println(
                    "Sau sua: " + laptop.getName() + "|" + laptop.getQuantity()
            );
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
