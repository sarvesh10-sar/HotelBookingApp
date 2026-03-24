import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Booking Queue
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
        return rooms.getOrDefault(type, -1);
    }

    public void reduceRoom(String type) throws InvalidBookingException {
        int count = rooms.getOrDefault(type, -1);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + type);
        }

        rooms.put(type, count - 1);
    }

    public boolean isValidRoomType(String type) {
        return rooms.containsKey(type);
    }
}

// Validator
class BookingValidator {

    public static void validate(Reservation r, Inventory inventory) throws InvalidBookingException {

        // Validate guest name
        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailable(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("No rooms available for " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {
    private Inventory inventory;

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) {
        try {
            // Validation (Fail Fast)
            BookingValidator.validate(r, inventory);

            // Generate Room ID
            String roomId = r.getRoomType().charAt(0) + "" + new Random().nextInt(1000);

            // Update inventory
            inventory.reduceRoom(r.getRoomType());

            System.out.println("Booking Confirmed: " + r.getGuestName()
                    + " | Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// Main
public class Main {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();
        BookingService service = new BookingService(inventory);

        // Valid request
        queue.addRequest(new Reservation("Alice", "Single"));

        // Invalid room type
        queue.addRequest(new Reservation("Bob", "Deluxe"));

        // Empty name
        queue.addRequest(new Reservation("", "Suite"));

        // Exceed availability
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single"));

        System.out.println("\nProcessing Bookings...\n");

        while (!queue.isEmpty()) {
            service.confirmBooking(queue.getNext());
        }
    }
}