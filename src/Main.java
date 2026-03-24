import java.util.*;

// Reservation
class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = UUID.randomUUID().toString();
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }
}

// Booking Queue (UC5)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 1);
        rooms.put("Suite", 1);
    }

    public int getAvailable(String type) {
        return rooms.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}

// Booking History (UC8)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }
}

// Booking Service (UC6 + UC8)
class BookingService {
    private Inventory inventory;
    private Set<String> allocatedRooms = new HashSet<>();
    private BookingHistory history;

    public BookingService(Inventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void confirmBooking(Reservation r) {
        String type = r.getRoomType();

        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No room available for " + r.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.charAt(0) + "" + new Random().nextInt(1000);
        } while (allocatedRooms.contains(roomId));

        allocatedRooms.add(roomId);
        inventory.reduceRoom(type);

        // Add to history (UC8)
        history.add(r);

        System.out.println("Booking Confirmed: " + r.getGuestName()
                + " | Room ID: " + roomId
                + " | Reservation ID: " + r.getReservationId());
    }
}

// Reporting Service (UC8)
class BookingReportService {

    public void showAllBookings(List<Reservation> history) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r.getGuestName() + " -> " + r.getRoomType());
        }
    }

    public void summaryReport(List<Reservation> history) {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : history) {
            summary.put(r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n--- Summary Report ---");
        for (String type : summary.keySet()) {
            System.out.println(type + " Rooms Booked: " + summary.get(type));
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory, history);
        BookingReportService reportService = new BookingReportService();

        // Add requests (UC5)
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));

        // Process bookings (UC6 + UC8)
        while (!queue.isEmpty()) {
            bookingService.confirmBooking(queue.getNext());
        }

        // Reporting (UC8)
        reportService.showAllBookings(history.getAll());
        reportService.summaryReport(history.getAll());
    }
}