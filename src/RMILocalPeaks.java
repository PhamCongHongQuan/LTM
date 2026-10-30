import RMI.DataService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RMILocalPeaks {
    private Registry registry;
    private DataService service;

    private String studentCode = "B23DCCN682";
    private String qCode = "TgHlHN0j";

    public void connection() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("36.50.135.242", 1099);

        service = (DataService) registry.lookup("RMIDataService");
    }

    public int[] receive() throws RemoteException {
        Object data = service.requestData(studentCode, qCode);

        // Truong hop server gui mang int[]
        if (data instanceof int[]) {
            return (int[]) data;
        }

        // Truong hop server gui mang Integer[]
        if (data instanceof Integer[]) {
            Integer[] source = (Integer[]) data;
            int[] numbers = new int[source.length];

            for (int i = 0; i < source.length; i++) {
                numbers[i] = source[i];
            }

            return numbers;
        }

        throw new IllegalArgumentException(
                "Kieu du lieu nhan khong phai int[] hoac Integer[]: "
                        + (data == null ? "null" : data.getClass().getName())
        );
    }

    public List<Integer> solve(int[] numbers) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < numbers.length; i++) {
            boolean greaterThanLeft =
                    i == 0 || numbers[i] > numbers[i - 1];

            boolean greaterThanRight =
                    i == numbers.length - 1
                            || numbers[i] > numbers[i + 1];

            if (greaterThanLeft && greaterThanRight) {
                result.add(i + 1);
            }
        }

        return result;
    }

    public void send(List<Integer> result) throws RemoteException {
        service.submitData(studentCode, qCode, result);
    }

    public static void main(String[] args) {
        RMILocalPeaks client = new RMILocalPeaks();

        try {
            client.connection();

            int[] numbers = client.receive();
            System.out.println("Nhan: " + Arrays.toString(numbers));

            List<Integer> result = client.solve(numbers);
            client.send(result);

            System.out.println("Da gui: " + result);
        } catch (RemoteException | NotBoundException
                 | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}