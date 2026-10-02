import TCP.Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringJoiner;

public class TCPFixCustomer {
    private Socket mySocket;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public void connection() throws IOException{
        mySocket = new Socket("36.50.135.242",2209);

        os= new ObjectOutputStream(mySocket.getOutputStream());
        os.flush();
        is=new ObjectInputStream(mySocket.getInputStream());
    }
    public void send(Object obj) throws IOException{
        os.writeObject(obj);
        os.flush();
    }
    public Customer receive() throws IOException,ClassNotFoundException{
        return (Customer) is.readObject();
    }
    public void solve (Customer customer) {
        String[] names= customer.getName().trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        result.append(names[names.length-1].toUpperCase());
        result.append(", ");
        for(int i=0;i< names.length-1;i++) {
            String s= names[i].substring(0,1).toUpperCase()+names[i].substring(1).toLowerCase();
            result.append(s);
            if (i!=names.length-2) result.append(" ");
        }
        customer.setName(result.toString());

        String[] dof = customer.getDayOfBirth().trim().split("-");
        customer.setDayOfBirth(dof[1]+"/"+dof[0]+"/"+dof[2]);


        StringBuilder res= new StringBuilder();
        for(int i=0;i<names.length-1;i++){
            res.append(names[i].substring(0,1).toLowerCase());
        }
        res.append(names[names.length-1].toLowerCase());
        customer.setUserName(res.toString());
    }
    public void close() throws IOException{
        if (mySocket!=null) {
            mySocket.close();
        }
    }

    public static void main(String[] args) {
        TCPFixCustomer client = new TCPFixCustomer();
        try{
            client.connection();
            client.send("B23DCCN682;e3fLVLlz");
            Customer customer= client.receive();
            System.out.println("Truoc sua: "+ customer.getName() +" "+customer.getDayOfBirth()+" "+customer.getUserName());
            client.solve(customer);
            client.send(customer);
            System.out.println("Sau sua: "+ customer.getName() +" "+customer.getDayOfBirth()+" "+customer.getUserName());

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
