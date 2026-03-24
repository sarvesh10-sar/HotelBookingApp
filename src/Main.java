import java.util.*;

// Reservation Class (Represents booking request)
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

// Booking Request Queue Service
class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue (FIFO)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all queued requests
    public void showQueue() {
        System.out.println("\nCurrent Booking Queue:");
        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " -> " + r.getRoomType());
        }
    }

    // Get next request (for future UC6 processing)
    public Reservation getNextRequest() {
        return queue.peek(); // only view, not remove
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        // Guest booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double"));

        // Display queue (FIFO order)
        bookingQueue.showQueue();

        // Show next request (without removing)
        Reservation next = bookingQueue.getNextRequest();
        System.out.println("\nNext request to process: "
                + next.getGuestName() + " -> " + next.getRoomType());
    }
}