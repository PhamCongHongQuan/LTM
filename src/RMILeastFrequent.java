import RMI.ByteService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMILeastFrequent {
    private Registry registry;
    private ByteService service;

    private String studentCode= "B23DCCN682";
    private String qCode= "1hzvF8lm";

    public void connection() throws RemoteException, NotBoundException{
        registry= LocateRegistry.getRegistry("36.50.135.242",1099);
        service=(ByteService) registry.lookup("RMIByteService");
    }

    public byte[] receive() throws RemoteException{
        return service.requestData(studentCode,qCode);
    }
    public byte[] solve(byte[] data) {
        if(data==null || data.length==0) {
            throw new IllegalArgumentException("Mang du lieu rong");
        }
        int[] cnt = new int[256];
        for(byte value: data) {
            cnt[value+128]++;
        }
        byte selected= data[0];
        int minCount= cnt[selected+128];
        for(byte value: data) {
            int frequency = cnt[value+128];
            if(frequency>minCount) {
                selected=value;
                minCount=frequency;
            }
        }
        return new byte[] {selected,(byte) minCount};
    }
    public void send(byte[] result) throws RemoteException {
        service.submitData(studentCode,qCode,result);
    }

    public static void main(String[] args) {
        RMILeastFrequent client= new RMILeastFrequent();
        try {
            client.connection();
            byte[] data= client.receive();
            byte[] result= client.solve(data);
            client.send(result);
        } catch (RemoteException|NotBoundException|IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
