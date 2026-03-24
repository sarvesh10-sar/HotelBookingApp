import java.util.*;

// Reservation (Request)
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

// Booking Queue (UC5)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request added: " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // remove (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public Inventory() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void reduceRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Service (UC6)
class BookingService {

    private Inventory inventory;

    // Track allocated room IDs (no duplicates)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void processRequest(Reservation r) {

        String type = r.getRoomType();

        // Check availability
        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No rooms available for " + r.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.substring(0, 1).toUpperCase() + new Random().nextInt(1000);
        } while (allocatedRoomIds.contains(roomId));

        // Add to set (uniqueness)
        allocatedRoomIds.add(roomId);

        // Map allocation
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory (IMPORTANT)
        inventory.reduceRoom(type);

        // Confirmation
        System.out.println("Booking Confirmed for " + r.getGuestName() +
                " | Room Type: " + type +
                " | Room ID: " + roomId);
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();
        BookingService bookingService = new BookingService(inventory);

        // Add requests (UC5)
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single")); // extra (to test no availability)

        System.out.println("\nProcessing Booking Requests...\n");

        // Process queue (UC6)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processRequest(r);
        }
    }
}