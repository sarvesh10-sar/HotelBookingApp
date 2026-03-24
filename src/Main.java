import java.util.*;

// Reservation
class Reservation {
    String id;
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.id = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory
class Inventory {
    Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 1);
    }

    public synchronized boolean allocate(String type) {
        if (rooms.getOrDefault(type, 0) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public synchronized void release(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }

    public void showInventory() {
        System.out.println("Current Inventory: " + rooms);
    }
}

// Booking + History
class BookingSystem {
    Inventory inventory;
    List<Reservation> history = new ArrayList<>();
    Map<String, Reservation> active = new HashMap<>();

    public BookingSystem(Inventory inventory) {
        this.inventory = inventory;
    }

    public void book(String name, String type) {
        Reservation r = new Reservation(name, type);

        if (inventory.allocate(type)) {
            active.put(r.id, r);
            history.add(r);
            System.out.println("Booked: " + name + " | ID: " + r.id);
        } else {
            System.out.println("Booking Failed for " + name);
        }
    }

    public void cancel(String id) {
        if (!active.containsKey(id)) {
            System.out.println("Invalid Cancel ID");
            return;
        }

        Reservation r = active.remove(id);
        inventory.release(r.roomType);

        System.out.println("Cancelled: " + r.guestName);
    }

    public void showHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r.guestName + " | " + r.roomType + " | " + r.id);
        }
    }

    public void showActive() {
        System.out.println("\n--- Active Bookings ---");
        for (Reservation r : active.values()) {
            System.out.println(r.guestName + " | " + r.roomType);
        }
    }
}

// Main
public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        BookingSystem system = new BookingSystem(inventory);

        // Bookings
        system.book("Alice", "Single");
        system.book("Bob", "Single");
        system.book("Charlie", "Single"); // fail

        // Cancel one
        String cancelId = system.history.get(0).id;
        system.cancel(cancelId);

        // Show final system state
        system.showActive();
        system.showHistory();
        inventory.showInventory();
    }
}