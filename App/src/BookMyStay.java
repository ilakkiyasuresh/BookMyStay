import java.util.*;

class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        System.out.println("Services for Reservation " + reservationId + ":");

        double total = 0;

        for (AddOnService s : services) {
            System.out.println(s.getName() + " - ₹" + s.getPrice());
            total += s.getPrice();
        }

        System.out.println("Total Add-On Cost: ₹" + total);
    }
}

public class BookMyStay {
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String res1 = "SI1";
        String res2 = "DO2";

        manager.addService(res1, new AddOnService("Breakfast", 200));
        manager.addService(res1, new AddOnService("WiFi", 100));
        manager.addService(res1, new AddOnService("Airport Pickup", 500));

        manager.addService(res2, new AddOnService("Dinner", 300));

        manager.displayServices(res1);
        System.out.println();
        manager.displayServices(res2);
    }
}