import java.util.*;

public class FlashSaleInventoryManager {

    // HashMap for product stock (O(1) lookup)
    HashMap<String, Integer> inventory = new HashMap<>();

    // LinkedHashMap for FIFO waiting list
    LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    // Check stock availability
    public void checkStock(String productId) {
        if (inventory.containsKey(productId)) {
            System.out.println(productId + " -> " + inventory.get(productId) + " units available");
        } else {
            System.out.println("Product not found");
        }
    }

    // Purchase item (synchronized to handle concurrent requests safely)
    public synchronized void purchaseItem(String productId, int userId) {

        if (!inventory.containsKey(productId)) {
            System.out.println("Product not found");
            return;
        }

        int stock = inventory.get(productId);

        if (stock > 0) {
            stock--;
            inventory.put(productId, stock);

            System.out.println("purchaseItem(\"" + productId + "\", userId=" + userId +
                    ") → Success, " + stock + " units remaining");
        }
        else {
            waitingList.put(userId, productId);

            System.out.println("purchaseItem(\"" + productId + "\", userId=" + userId +
                    ") → Added to waiting list, position #" + waitingList.size());
        }
    }

    // Show waiting list
    public void showWaitingList() {

        if (waitingList.isEmpty()) {
            System.out.println("Waiting list empty");
            return;
        }

        int pos = 1;
        for (Map.Entry<Integer, String> entry : waitingList.entrySet()) {
            System.out.println("Position #" + pos + " -> UserID: " + entry.getKey());
            pos++;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        FlashSaleInventoryManager system = new FlashSaleInventoryManager();

        // Initial stock (100 units)
        system.inventory.put("IPHONE15_256GB", 100);

        int choice;

        do {
            System.out.println("\n--- Flash Sale Inventory Manager ---");
            System.out.println("1. Check Stock");
            System.out.println("2. Purchase Item");
            System.out.println("3. Show Waiting List");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter product ID: ");
                    String productId = sc.nextLine();
                    system.checkStock(productId);
                    break;

                case 2:
                    System.out.print("Enter product ID: ");
                    String pId = sc.nextLine();
                    System.out.print("Enter user ID: ");
                    int userId = sc.nextInt();

                    system.purchaseItem(pId, userId);
                    break;

                case 3:
                    system.showWaitingList();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 4);

        sc.close();
    }
}