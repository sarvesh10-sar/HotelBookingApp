import java.util.*;

class Room {
    int number;
    boolean isBooked;

    Room(int number) {
        this.number = number;
        this.isBooked = false;
    }
}

class BookingRequest {
    String customerName;
    int roomNumber;

    BookingRequest(String customerName, int roomNumber) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
    }
}

public class Main {

    static List<Room> rooms = new ArrayList<>();
    static Queue<BookingRequest> requestQueue = new LinkedList<>();
    static Map<Integer, String> bookings = new HashMap<>();

    public static void main(String[] args) {

        // Create 5 rooms
        for (int i = 1; i <= 5; i++) {
            rooms.add(new Room(i));
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Show Bookings");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Name: ");
                String name = sc.nextLine();

                System.out.print("Enter Room Number: ");
                int room = sc.nextInt();

                requestQueue.add(new BookingRequest(name, room));
                processRequests();

            } else if (choice == 2) {
                showBookings();

            } else {
                break;
            }
        }

        sc.close();
    }

    static void processRequests() {
        while (!requestQueue.isEmpty()) {
            BookingRequest req = requestQueue.poll();

            if (!bookings.containsKey(req.roomNumber)) {
                bookings.put(req.roomNumber, req.customerName);
                System.out.println("Room " + req.roomNumber + " booked for " + req.customerName);
            } else {
                System.out.println("Room already booked!");
            }
        }
    }

    static void showBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            for (int room : bookings.keySet()) {
                System.out.println("Room " + room + " -> " + bookings.get(room));
            }
        }
    }
}