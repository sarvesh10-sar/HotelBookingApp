import java.util.HashMap;

public class Main {

    // Centralized Inventory Class (Inner Class so everything is in 1 file)
    static class RoomInventory {

        private HashMap<String, Integer> inventory;

        // Constructor
        public RoomInventory() {
            inventory = new HashMap<>();
        }

        // Add room type
        public void addRoomType(String roomType, int count) {
            inventory.put(roomType, count);
        }

        // Check availability
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Book a room
        public void bookRoom(String roomType) {
            int available = inventory.getOrDefault(roomType, 0);

            if (available > 0) {
                inventory.put(roomType, available - 1);
                System.out.println("✔ Room booked: " + roomType);
            } else {
                System.out.println("❌ No rooms available for: " + roomType);
            }
        }

        // Display inventory
        public void displayInventory() {
            System.out.println("\n--- Current Room Inventory ---");
            for (String type : inventory.keySet()) {
                System.out.println(type + " : " + inventory.get(type));
            }
        }
    }

    // MAIN PROGRAM
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Initialize room types
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Suite", 2);

        // Display inventory
        inventory.displayInventory();

        // Book rooms
        inventory.bookRoom("Single");
        inventory.bookRoom("Suite");
        inventory.bookRoom("Suite");
        inventory.bookRoom("Suite");  // No rooms left

        // Display again
        inventory.displayInventory();
    }
}