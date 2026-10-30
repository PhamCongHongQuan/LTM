import RMI.ObjectService;
import RMI.TicketSla;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMITicketSlaClient {
    private Registry registry;
    private ObjectService service;

    private String studentCode = "B23DCCN682";
    private String qCode = "kyGSzhLD";

    public void connection() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("36.50.135.242", 1099);

        service = (ObjectService)
                registry.lookup("RMIObjectService");
    }

    public TicketSla receive() throws RemoteException {
        return (TicketSla) service.requestObject(studentCode, qCode);
    }

    public void solve(TicketSla ticket) {
        String priority = ticket.getPriority();
        int hours = ticket.getOpenedHoursAgo();

        // 1. Xac dinh nguong theo muc uu tien
        int limit;

        if ("CRITICAL".equals(priority)) {
            limit = 2;
        } else if ("HIGH".equals(priority)) {
            limit = 8;
        } else if ("MEDIUM".equals(priority)) {
            limit = 24;
        } else if ("LOW".equals(priority)) {
            limit = 72;
        } else {
            throw new IllegalArgumentException(
                    "Priority khong hop le: " + priority
            );
        }

        // 2. Xac dinh co vi pham hay khong
        boolean breached = hours > limit;
        ticket.setBreached(breached);

        // 3. Chon hanh dong
        String action;

        if (!breached) {
            action = "MONITOR";
        } else if ("CRITICAL".equals(priority) || hours > 96) {
            action = "ESCALATE_L2";
        } else {
            action = "ESCALATE_L1";
        }

        ticket.setAction(action);
    }

    public void send(TicketSla ticket) throws RemoteException {
        service.submitObject(studentCode, qCode, ticket);
    }

    public static void main(String[] args) {
        RMITicketSlaClient client = new RMITicketSlaClient();

        try {
            client.connection();

            TicketSla ticket = client.receive();

            System.out.println(
                    "Nhan: priority=" + ticket.getPriority()
                            + "; openedHoursAgo=" + ticket.getOpenedHoursAgo()
            );

            client.solve(ticket);
            client.send(ticket);

            System.out.println(
                    "Da gui: breached=" + ticket.isBreached()
                            + "; action=" + ticket.getAction()
            );
        } catch (RemoteException | NotBoundException
                 | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}