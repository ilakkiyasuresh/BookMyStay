import java.util.*;

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

    public void reduceRoom(String type) throws Exception {
        int available = inventory.getOrDefault(type, 0);
        if (available <= 0) throw new Exception("No rooms available for " + type);
        inventory.put(type, available - 1);
    }

    public void increaseRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private Map<String, Reservation> confirmedReservations;
    private Stack<String> rollbackStack;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
        confirmedReservations = new HashMap<>();
        rollbackStack = new Stack<>();
    }

    private String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 2).toUpperCase() + idCounter++;
        } while (allocatedRoomIds.contains(id));
        return id;
    }

    public Reservation bookRoom(String guestName, String roomType) throws Exception {
        if (inventory.getAvailability(roomType) <= 0) {
            throw new Exception("Cannot book " + roomType + " for " + guestName + " (No availability)");
        }

        String roomId = generateRoomId(roomType);
        allocatedRoomIds.add(roomId);
        roomAllocations.putIfAbsent(roomType, new HashSet<>());
        roomAllocations.get(roomType).add(roomId);
        inventory.reduceRoom(roomType);

        Reservation r = new Reservation(guestName, roomType, roomId);
        confirmedReservations.put(roomId, r);
        rollbackStack.push(roomId);

        System.out.println("Confirmed: " + guestName + " -> " + roomType + " | Room ID: " + roomId);
        return r;
    }

    public void cancelReservation(String roomId) {
        if (!confirmedReservations.containsKey(roomId)) {
            System.out.println("Cancellation Failed: Reservation ID " + roomId + " does not exist");
            return;
        }

        Reservation r = confirmedReservations.get(roomId);
        allocatedRoomIds.remove(roomId);
        roomAllocations.get(r.getRoomType()).remove(roomId);
        inventory.increaseRoom(r.getRoomType());
        confirmedReservations.remove(roomId);
        rollbackStack.remove(roomId);

        System.out.println("Cancelled: " + r.getGuestName() + " -> " + r.getRoomType() + " | Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("Current Allocations: " + roomAllocations);
    }
}

public class BookMyStay {
    public static void main(String[] args) throws Exception {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        Reservation r1 = service.bookRoom("Arun", "Single Room");
        Reservation r2 = service.bookRoom("Priya", "Double Room");
        Reservation r3 = service.bookRoom("Ravi", "Suite Room");

        System.out.println();

        service.cancelReservation(r2.getRoomId());
        service.cancelReservation("INVALID_ID");

        System.out.println();

        inventory.displayInventory();
        service.displayAllocations();
    }
}