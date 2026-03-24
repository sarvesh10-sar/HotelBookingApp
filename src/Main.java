abstract class Room {
    protected String name;
    protected int price;
    protected int beds;

    public Room(String name, int price, int beds) {
        this.name = name;
        this.price = price;
        this.beds = beds;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getBeds() {
        return beds;
    }

    public void showDetails() {
        System.out.println("Room: " + name + " | Beds: " + beds + " | Price: ₹" + price);
    }
}

// UC2 – Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1500, 1);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2500, 2);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 5000, 3);
    }
}

// UC3 – Central Inventory
import java.util.HashMap;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public HashMap<String, Integer> getAll() {
        return inventory;
    }
}

// UC4 – Room Search Service (READ ONLY)
class RoomSearchService {

    public void showAvailableRooms(RoomInventory inventory) {
        System.out.println("\n----- Available Rooms -----");

        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        for (Room r : rooms) {
            int available = inventory.getAvailability(r.getName());

            if (available > 0) {
                r.showDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

// MAIN APP
public class Main {
    public static void main(String[] args) {

        System.out.println("=== Hotel Booking App ===");
        System.out.println("Version 1.0\n");

        RoomInventory inventory = new RoomInventory();

        // **UC4 Search**
        RoomSearchService searchService = new RoomSearchService();
        searchService.showAvailableRooms(inventory);
    }
}