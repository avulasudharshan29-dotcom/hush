import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    int time; // minutes since start of day

    Transaction(int id, int amount, String merchant, String account, int time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class FinancialTransactionAnalyzer {

    static List<Transaction> transactions = new ArrayList<>();

    // Add transaction
    static void addTransaction(int id, int amount, String merchant, String account, int time) {
        transactions.add(new Transaction(id, amount, merchant, account, time));
        System.out.println("Transaction added.");
    }

    // Classic Two Sum
    static void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                System.out.println("Pair Found → (id:" + t2.id + ", id:" + t.id + ")");
                return;
            }

            map.put(t.amount, t);
        }

        System.out.println("No pair found.");
    }

    // Two Sum within 1 hour (60 minutes)
    static void findTwoSumTimeWindow(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                if (Math.abs(t.time - t2.time) <= 60) {
                    System.out.println("Pair within 1 hour → (id:" + t2.id + ", id:" + t.id + ")");
                    return;
                }
            }

            map.put(t.amount, t);
        }

        System.out.println("No pair found within time window.");
    }

    // Duplicate detection
    static void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.println("Duplicate detected → Amount: "
                        + list.get(0).amount + ", Merchant: " + list.get(0).merchant);

                for (Transaction t : list)
                    System.out.println("Account: " + t.account + " (id:" + t.id + ")");
            }
        }
    }

    // K Sum (simple recursive)
    static void findKSum(int k, int target) {

        List<Integer> result = new ArrayList<>();

        kSumHelper(0, k, target, result);
    }

    static void kSumHelper(int index, int k, int target, List<Integer> result) {

        if (k == 0 && target == 0) {

            System.out.println("K-Sum Found → " + result);
            return;
        }

        if (index >= transactions.size() || k < 0 || target < 0)
            return;

        Transaction t = transactions.get(index);

        result.add(t.id);
        kSumHelper(index + 1, k - 1, target - t.amount, result);

        result.remove(result.size() - 1);
        kSumHelper(index + 1, k, target, result);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n--- Financial Transaction Analyzer ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. Find Two-Sum");
            System.out.println("3. Find Two-Sum (1 Hour Window)");
            System.out.println("4. Detect Duplicates");
            System.out.println("5. Find K-Sum");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Amount: ");
                    int amount = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Merchant: ");
                    String merchant = sc.nextLine();

                    System.out.print("Enter Account: ");
                    String account = sc.nextLine();

                    System.out.print("Enter Time (minutes): ");
                    int time = sc.nextInt();

                    addTransaction(id, amount, merchant, account, time);
                    break;

                case 2:

                    System.out.print("Enter target amount: ");
                    int target = sc.nextInt();

                    findTwoSum(target);
                    break;

                case 3:

                    System.out.print("Enter target amount: ");
                    int target2 = sc.nextInt();

                    findTwoSumTimeWindow(target2);
                    break;

                case 4:

                    detectDuplicates();
                    break;

                case 5:

                    System.out.print("Enter K value: ");
                    int k = sc.nextInt();

                    System.out.print("Enter target sum: ");
                    int target3 = sc.nextInt();

                    findKSum(k, target3);
                    break;

                case 6:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }
}