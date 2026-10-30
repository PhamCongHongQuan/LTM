import RMI.CharacterService;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;

public class RMIURIEncoding {
    private Registry registry;
    private CharacterService service;
    private String studentCode= "B23DCCN682";
    private String qCode="sTSUpzjh";

    public void connection() throws RemoteException, NotBoundException{
        registry= LocateRegistry.getRegistry("36.50.135.242",1099);
        service= (CharacterService) registry.lookup("RMICharacterService");
    }

    public String receive() throws RemoteException{
        return service.requestCharacter(studentCode,qCode);
    }
    public String solve(String data) throws URISyntaxException{
        URI uri= new URI(null,null,data,null);
        return uri.toASCIIString();
    }
    public void send(String result) throws RemoteException{
        service.submitCharacter(studentCode,qCode,result);
    }

    public static void main(String[] args) {
        RMIURIEncoding client= new RMIURIEncoding();
        try {
            client.connection();
            String data= client.receive();
            String result= client.solve(data);
            client.send(result);
        } catch (RemoteException| NotBoundException| URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
