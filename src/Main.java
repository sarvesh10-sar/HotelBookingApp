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

// Booking Service (UC6)
class BookingService {
    private Inventory inventory;
    private Set<String> allocatedRooms = new HashSet<>();

    public BookingService(Inventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation r) {
        String type = r.getRoomType();

        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No room available for " + r.getGuestName());
            return;
        }

        String roomId;
        do {
            roomId = type.charAt(0) + "" + new Random().nextInt(1000);
        } while (allocatedRooms.contains(roomId));

        allocatedRooms.add(roomId);
        inventory.reduceRoom(type);

        System.out.println("Booking Confirmed: " + r.getGuestName()
                + " | Room ID: " + roomId
                + " | Reservation ID: " + r.getReservationId());
    }
}

// Add-On Service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() { return name; }
    public double getCost() { return cost; }
}

// Add-On Manager (UC7)
class AddOnManager {
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Service added: " + service.getName()
                + " to Reservation " + reservationId);
    }

    // Calculate total cost
    public double calculateCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }

    // Show services
    public void showServices(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null) {
            System.out.println("No services added.");
            return;
        }

        System.out.println("Services for Reservation " + reservationId + ":");
        for (AddOnService s : services) {
            System.out.println(s.getName() + " - ₹" + s.getCost());
        }
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();
        BookingService bookingService = new BookingService(inventory);
        AddOnManager addOnManager = new AddOnManager();

        // Create reservation
        Reservation r1 = new Reservation("Alice", "Single");

        queue.addRequest(r1);

        // Process booking (UC6)
        while (!queue.isEmpty()) {
            bookingService.confirmBooking(queue.getNext());
        }

        // UC7 Add-On Services
        addOnManager.addService(r1.getReservationId(), new AddOnService("Breakfast", 200));
        addOnManager.addService(r1.getReservationId(), new AddOnService("Spa", 500));

        addOnManager.showServices(r1.getReservationId());

        double totalCost = addOnManager.calculateCost(r1.getReservationId());
        System.out.println("Total Add-On Cost: ₹" + totalCost);
    }
}