import RMI.DataService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Locale;

public class RMIStatistics {
    private Registry registry;
    private DataService service;

    private String studentCode = "B23DCCN682";
    private String qCode = "kTr2eRbz";

    public void connection() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("36.50.135.242", 1099);
        service = (DataService) registry.lookup("RMIDataService");
    }

    public String receive() throws RemoteException {
        return (String) service.requestData(studentCode, qCode);
    }

    public String solve(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Du lieu rong");
        }

        // 1. Chuyen chuoi CSV thanh mang so thuc
        String[] parts = data.split(",");
        int n = parts.length;

        double[] numbers = new double[n];
        double sum = 0;

        for (int i = 0; i < n; i++) {
            numbers[i] = Double.parseDouble(parts[i].trim());
            sum += numbers[i];
        }

        // 2. Tinh trung binh
        double average = sum / n;

        // 3. Tinh do lech chuan toan bo day
        double sumSquaredDifference = 0;

        for (double value : numbers) {
            double difference = value - average;
            sumSquaredDifference += difference * difference;
        }

        double stddev = Math.sqrt(sumSquaredDifference / n);

        // 4. Sap xep va lay phan vi 95
        Arrays.sort(numbers);

        int index = (int) Math.ceil(n * 0.95) - 1;
        double p95 = numbers[index];

        // 5. Dinh dang dung 2 chu so sau dau cham
        return String.format(
                Locale.US,
                "average=%.2f;stddev=%.2f;p95=%.2f",
                average,
                stddev,
                p95
        );
    }

    public void send(String result) throws RemoteException {
        service.submitData(studentCode, qCode, result);
    }

    public static void main(String[] args) {
        RMIStatistics client = new RMIStatistics();

        try {
            client.connection();

            String data = client.receive();
            System.out.println("Nhan: " + data);

            String result = client.solve(data);
            client.send(result);

            System.out.println("Da gui: " + result);
        } catch (RemoteException | NotBoundException
                 | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}