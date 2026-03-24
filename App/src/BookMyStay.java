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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    public void generateReport(List<Reservation> reservations) {
        System.out.println("===== Booking History Report =====");

        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : reservations) {
            System.out.println("Guest: " + r.getGuestName() + " | Room: " + r.getRoomType() + " | ID: " + r.getRoomId());

            countByType.put(r.getRoomType(),
                    countByType.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n--- Summary ---");
        for (String type : countByType.keySet()) {
            System.out.println(type + " Bookings: " + countByType.get(type));
        }

        System.out.println("Total Bookings: " + reservations.size());
        System.out.println("==================================");
    }
}

public class BookMyStay {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("Arun", "Single Room", "SI1"));
        history.addReservation(new Reservation("Priya", "Double Room", "DO2"));
        history.addReservation(new Reservation("Karthik", "Single Room", "SI3"));
        history.addReservation(new Reservation("Divya", "Suite Room", "SU4"));

        BookingReportService reportService = new BookingReportService();

        reportService.generateReport(history.getAllReservations());
    }
}