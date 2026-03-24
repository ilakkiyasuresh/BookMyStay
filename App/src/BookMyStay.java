import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
}

class RoomInventory implements Serializable {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String type) { return inventory.getOrDefault(type, 0); }

    public void reduceRoom(String type) throws Exception {
        int available = inventory.getOrDefault(type, 0);
        if (available <= 0) throw new Exception("No rooms available for " + type);
        inventory.put(type, available - 1);
    }

    public void increaseRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() { System.out.println("Inventory: " + inventory); }
}

class BookingService implements Serializable {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private Map<String, Reservation> confirmedReservations;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
        confirmedReservations = new HashMap<>();
    }

    private String generateRoomId(String type) {
        String id;
        do { id = type.substring(0, 2).toUpperCase() + idCounter++; }
        while (allocatedRoomIds.contains(id));
        return id;
    }

    public Reservation bookRoom(String guestName, String roomType) throws Exception {
        if (inventory.getAvailability(roomType) <= 0) throw new Exception("Cannot book " + roomType);
        String roomId = generateRoomId(roomType);
        allocatedRoomIds.add(roomId);
        roomAllocations.putIfAbsent(roomType, new HashSet<>());
        roomAllocations.get(roomType).add(roomId);
        inventory.reduceRoom(roomType);
        Reservation r = new Reservation(guestName, roomType, roomId);
        confirmedReservations.put(roomId, r);
        System.out.println("Confirmed: " + guestName + " -> " + roomType + " | Room ID: " + roomId);
        return r;
    }

    public void displayAllocations() { System.out.println("Current Allocations: " + roomAllocations); }

    public void saveState(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    public static BookingService loadState(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (BookingService) in.readObject();
        } catch (Exception e) {
            System.out.println("No previous state found or file corrupted. Starting fresh.");
            return null;
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) throws Exception {
        String filename = "booking_state.ser";

        BookingService service = BookingService.loadState(filename);
        RoomInventory inventory;
        if (service == null) {
            inventory = new RoomInventory();
            service = new BookingService(inventory);
        } else {
            inventory = service.inventory;
        }

        try {
            service.bookRoom("Arun", "Single Room");
            service.bookRoom("Priya", "Double Room");
            service.bookRoom("Ravi", "Suite Room");
        } catch (Exception e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        System.out.println();
        inventory.displayInventory();
        service.displayAllocations();

        service.saveState(filename);
        System.out.println("\nSystem state saved successfully.");
    }
}