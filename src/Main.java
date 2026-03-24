import java.util.*;

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

// Thread-Safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNext() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory (Shared Resource)
class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 1);
    }

    // Critical Section
    public synchronized boolean allocateRoom(String type) {
        int available = rooms.getOrDefault(type, 0);

        if (available > 0) {
            rooms.put(type, available - 1);
            return true;
        }
        return false;
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingQueue queue, Inventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;

            // Get request safely
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNext();
            }

            if (r == null) continue;

            // Allocate room safely
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName()
                        + " booked for " + r.getGuestName()
                        + " (" + r.getRoomType() + ")");
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " FAILED for " + r.getGuestName()
                        + " (" + r.getRoomType() + ")");
            }
        }
    }
}

// Main
public class Main {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();

        // Simulate multiple requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // extra
        queue.addRequest(new Reservation("David", "Double"));

        // Multiple threads (guests)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll bookings processed safely.");
    }
}