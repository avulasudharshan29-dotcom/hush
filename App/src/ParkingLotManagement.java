import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    String status;

    ParkingSpot() {
        status = "EMPTY";
    }
}

public class ParkingLotManagement {

    static final int SIZE = 500;
    ParkingSpot[] table = new ParkingSpot[SIZE];

    int totalProbes = 0;
    int totalParks = 0;

    public ParkingLotManagement() {
        for (int i = 0; i < SIZE; i++)
            table[i] = new ParkingSpot();
    }

    // Hash function
    int hashFunction(String plate) {

        int hash = 0;

        for (char c : plate.toCharArray())
            hash += c;

        return hash % SIZE;
    }

    // Park vehicle using linear probing
    void parkVehicle(String plate) {

        int index = hashFunction(plate);
        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {

            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    void exitVehicle(String plate) {

        int index = hashFunction(plate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                    table[index].licensePlate.equals(plate)) {

                long exitTime = System.currentTimeMillis();

                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5;

                table[index].status = "DELETED";

                System.out.printf("exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        plate, index, hours, fee);

                return;
            }

            index = (index + 1) % SIZE;
            probes++;
        }

        System.out.println("Vehicle not found.");
    }

    // Statistics
    void getStatistics() {

        int occupied = 0;

        for (ParkingSpot spot : table) {
            if (spot.status.equals("OCCUPIED"))
                occupied++;
        }

        double occupancy = (occupied * 100.0) / SIZE;

        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ParkingLotManagement system = new ParkingLotManagement();

        int choice;

        do {

            System.out.println("\n--- Smart Parking Lot System ---");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Exit Vehicle");
            System.out.println("3. Get Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter License Plate: ");
                    String plate = sc.nextLine();

                    system.parkVehicle(plate);
                    break;

                case 2:

                    System.out.print("Enter License Plate: ");
                    String exitPlate = sc.nextLine();

                    system.exitVehicle(exitPlate);
                    break;

                case 3:

                    system.getStatistics();
                    break;

                case 4:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }
}