import RMI.CharacterService;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;

public class RMIRoman {
    private Registry registry;
    private CharacterService service;
    private String studentCode= "B23DCCN682";
    private String qCode="pXMRKKOI";

    public void connection() throws RemoteException, NotBoundException{
        registry= LocateRegistry.getRegistry("36.50.135.242",1099);
        service= (CharacterService) registry.lookup("RMICharacterService");
    }

    public String receive() throws RemoteException{
        return service.requestCharacter(studentCode,qCode);
    }
    public String solve(String data) {
        int number= Integer.parseInt(data.trim());
        if (number<0) {
            throw new IllegalArgumentException(
                    "so <0"
            );
        }
        int[] values= {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] symbols= { "M", "CM", "D", "CD",
                "C", "XC", "L", "XL",
                "X", "IX", "V", "IV", "I"};
        StringBuilder result= new StringBuilder();
        for (int i=0; i<values.length;i++) {
            while (number>=values[i]) {
                result.append(symbols[i]);
                number-=values[i];
            }
        }
        return result.toString();
    }
    public void send(String result) throws RemoteException{
        service.submitCharacter(studentCode,qCode,result);
    }

    public static void main(String[] args) {
        RMIRoman client= new RMIRoman();
        try {
            client.connection();
            String data= client.receive();
            String result= client.solve(data);
            client.send(result);
        } catch (RemoteException| NotBoundException| IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
