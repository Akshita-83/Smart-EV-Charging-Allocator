
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EVChargingSimulation simulation = new EVChargingSimulation();

        int choice=-1;

        do {
            System.out.println("\n================================");
            System.out.println(" SMART EV CHARGING ALLOCATOR");
            System.out.println("================================");
            System.out.println("1. Display Vehicles");
            System.out.println("2. Run FIFO");
            System.out.println("3. Run Shortest Charge First");
            System.out.println("4. Run Lowest Battery First");
            System.out.println("5. Run Emergency + Aging");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    simulation.displayVehicles();
                    break;
                case 2:
                    simulation.runFIFO();
                    break;
                case 3:
                    simulation.runShortestFirst();
                    break;
                case 4:
                    simulation.runLowestBatteryFirst();
                    break;
                case 5:
                    simulation.runEmergencyAging();
                    break;
                case 0:
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
