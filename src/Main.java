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

    public void increaseRoom(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }
}

// Booking Service
class BookingService {
    private Inventory inventory;

    // Track allocations
    private Map<String, String> reservationToRoom = new HashMap<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) {
        String type = r.getRoomType();

        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No room available for " + r.getGuestName());
            return;
        }

        String roomId = type.charAt(0) + "" + new Random().nextInt(1000);

        inventory.reduceRoom(type);
        reservationToRoom.put(r.getReservationId(), roomId);

        System.out.println("Booking Confirmed: " + r.getGuestName()
                + " | Room ID: " + roomId
                + " | Reservation ID: " + r.getReservationId());
    }

    public Map<String, String> getReservationMap() {
        return reservationToRoom;
    }
}

// Cancellation Service (UC10)
class CancellationService {

    private Inventory inventory;
    private Map<String, String> reservationToRoom;

    // Stack for rollback (LIFO)
    private Stack<String> releasedRooms = new Stack<>();

    // Track cancelled reservations
    private Set<String> cancelled = new HashSet<>();

    public CancellationService(Inventory inventory, Map<String, String> reservationToRoom) {
        this.inventory = inventory;
        this.reservationToRoom = reservationToRoom;
    }

    public void cancel(String reservationId, String roomType) {

        // Validate existence
        if (!reservationToRoom.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        // Prevent duplicate cancellation
        if (cancelled.contains(reservationId)) {
            System.out.println("Cancellation Failed: Already cancelled");
            return;
        }

        // Get room ID
        String roomId = reservationToRoom.get(reservationId);

        // Push to stack (rollback tracking)
        releasedRooms.push(roomId);

        // Restore inventory
        inventory.increaseRoom(roomType);

        // Mark cancelled
        cancelled.add(reservationId);

        System.out.println("Cancellation Successful: " + reservationId
                + " | Room Released: " + roomId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent First): " + releasedRooms);
    }
}

// Main
public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        BookingService bookingService = new BookingService(inventory);

        // Create reservations
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Suite");

        // Confirm bookings
        bookingService.confirmBooking(r1);
        bookingService.confirmBooking(r2);

        // Cancellation service
        CancellationService cancelService =
                new CancellationService(inventory, bookingService.getReservationMap());

        System.out.println("\n--- Cancelling Bookings ---");

        // Valid cancellation
        cancelService.cancel(r1.getReservationId(), r1.getRoomType());

        // Duplicate cancellation
        cancelService.cancel(r1.getReservationId(), r1.getRoomType());

        // Invalid cancellation
        cancelService.cancel("invalid-id", "Single");

        // Show rollback stack
        cancelService.showRollbackStack();
    }
}