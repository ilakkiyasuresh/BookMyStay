
import java.util.HashMap;
import java.util.Map;

// Inventory Class (Single Source of Truth)
class BookMyStay {

    private Map<String, Integer> inventory;

    // Constructor - Initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase or decrease)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\n===== Room Inventory =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("==========================");
    }
}

// Main Application
public class HotelBookingApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking System - UC3");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Simulate booking (reduce availability)
        System.out.println("\nBooking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);

        // Simulate cancellation (increase availability)
        System.out.println("Cancelling 1 Suite Room...");
        inventory.updateAvailability("Suite Room", +1);

        // Display updated inventory
        inventory.displayInventory();
    }
}
