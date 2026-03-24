import java.util.*;
import java.util.concurrent.*;

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
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public synchronized boolean reduceRoom(String type) {
        int available = inventory.getOrDefault(type, 0);
        if (available <= 0) return false;
        inventory.put(type, available - 1);
        return true;
    }

    public synchronized void increaseRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public synchronized void displayInventory() {
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
        allocatedRoomIds = Collections.synchronizedSet(new HashSet<>());
        roomAllocations = new ConcurrentHashMap<>();
    }

    private synchronized String generateRoomId(String type) {
        String id;
        do {
            id = type.substring(0, 2).toUpperCase() + idCounter++;
        } while (allocatedRoomIds.contains(id));
        return id;
    }

    public Reservation bookRoom(String guestName, String roomType) {
        synchronized (this) {
            if (!inventory.reduceRoom(roomType)) {
                System.out.println("Booking Failed: " + guestName + " -> " + roomType + " (No availability)");
                return null;
            }

            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            roomAllocations.putIfAbsent(roomType, Collections.synchronizedSet(new HashSet<>()));
            roomAllocations.get(roomType).add(roomId);

            System.out.println("Confirmed: " + guestName + " -> " + roomType + " | Room ID: " + roomId);
            return new Reservation(guestName, roomType, roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("Current Allocations: " + roomAllocations);
    }
}

public class BookMyStay {
    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        String[][] requests = {
                {"Arun", "Single Room"},
                {"Priya", "Single Room"},
                {"Karthik", "Single Room"},
                {"Divya", "Double Room"},
                {"Ravi", "Suite Room"},
                {"Anita", "Double Room"},
                {"Suresh", "Suite Room"},
                {"Maya", "Single Room"}
        };

        for (String[] req : requests) {
            executor.submit(() -> service.bookRoom(req[0], req[1]));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println();
        inventory.displayInventory();
        service.displayAllocations();
    }
}