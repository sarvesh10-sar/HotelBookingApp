import java.util.*;

// Room Model
class Room {
    private String roomType;
    private double price;
    private String amenities;

    public Room(String roomType, double price, String amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 0);
        roomAvailability.put("Suite", 3);
    }

    // Read-only access
    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

// Search Service (UC4 Logic)
class SearchService {
    private Inventory inventory;

    public SearchService(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Room> searchAvailableRooms(List<Room> rooms) {
        List<Room> availableRooms = new ArrayList<>();

        Map<String, Integer> availability = inventory.getAvailability();

        for (Room room : rooms) {
            int count = availability.getOrDefault(room.getRoomType(), 0);

            // Validation → only available rooms
            if (count > 0) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {

        // Inventory setup
        Inventory inventory = new Inventory();

        // Room data (Domain Model)
        List<Room> rooms = Arrays.asList(
                new Room("Single", 1000, "WiFi, TV"),
                new Room("Double", 2000, "WiFi, TV, AC"),
                new Room("Suite", 5000, "WiFi, TV, AC, Jacuzzi")
        );

        // Search Service
        SearchService searchService = new SearchService(inventory);

        // Perform Search
        List<Room> availableRooms = searchService.searchAvailableRooms(rooms);

        // Display Results
        System.out.println("Available Rooms:");
        for (Room room : availableRooms) {
            System.out.println(room.getRoomType() +
                    " - ₹" + room.getPrice() +
                    " | Amenities: " + room.getAmenities());
        }
    }
}