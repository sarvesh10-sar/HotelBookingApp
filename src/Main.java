abstract class Room {
    String roomType;
    int beds;
    double price;

    Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    void displayInfo() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
        System.out.println("------------------------");
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 1500);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 2500);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 4500);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("🏨 Hotel Room Types & Availability (UC2)");

        // Static availability
        int availableSingle = 5;
        int availableDouble = 3;
        int availableSuite = 2;

        // Create room objects
        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        // Display Info
        r1.displayInfo();
        System.out.println("Availability: " + availableSingle);

        r2.displayInfo();
        System.out.println("Availability: " + availableDouble);

        r3.displayInfo();
        System.out.println("Availability: " + availableSuite);
    }
}/