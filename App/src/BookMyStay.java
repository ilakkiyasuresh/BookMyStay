import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) throws InvalidBookingException {
        int available = inventory.getOrDefault(type, 0);
        if (available <= 0) {
            throw new InvalidBookingException("No available rooms for " + type);
        }
        inventory.put(type, available - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    private String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 2).toUpperCase() + idCounter++;
        } while (allocatedRoomIds.contains(id));
        return id;
    }

    public Reservation bookRoom(String guestName, String roomType) throws InvalidBookingException {
        if (!inventory.getAvailability(roomType).equals(0)) {
            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            roomAllocations.putIfAbsent(roomType, new HashSet<>());
            roomAllocations.get(roomType).add(roomId);
            inventory.reduceRoom(roomType);
            System.out.println("Confirmed: " + guestName + " -> " + roomType + " | Room ID: " + roomId);
            return new Reservation(guestName, roomType, roomId);
        } else {
            throw new InvalidBookingException("Cannot book " + roomType + " for " + guestName + " (No availability)");
        }
    }

    public void displayAllocations() {
        System.out.println("Room Allocations: " + roomAllocations);
    }
}

public class BookMyStay {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        try {
            service.bookRoom("Arun", "Single Room");
            service.bookRoom("Priya", "Single Room");
            service.bookRoom("Karthik", "Single Room"); // Will fail
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            service.bookRoom("Divya", "Double Room");
            service.bookRoom("Ravi", "Suite Room");
            service.bookRoom("Anita", "Penthouse"); // Invalid type
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        inventory.displayInventory();
        service.displayAllocations();
    }
}