import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public void displayQueue() {
        System.out.println("===== Booking Requests Queue =====");

        for (Reservation r : queue) {
            System.out.println("Guest: " + r.getGuestName() + " | Room: " + r.getRoomType());
        }

        System.out.println("==================================");
    }
}

public class BookMyStay {
    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        bookingQueue.addRequest(new Reservation("Arun", "Single Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Double Room"));
        bookingQueue.addRequest(new Reservation("Karthik", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Divya", "Single Room"));

        bookingQueue.displayQueue();
    }
}