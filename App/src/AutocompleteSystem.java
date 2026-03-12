import java.util.*;

public class AutocompleteSystem {

    HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Add or update search query frequency
    void updateFrequency(String query) {

        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);

        System.out.println("updateFrequency(\"" + query + "\") → Frequency: "
                + queryFrequency.get(query));
    }

    // Search suggestions based on prefix
    void search(String prefix) {

        List<Map.Entry<String, Integer>> results = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            if (entry.getKey().startsWith(prefix)) {
                results.add(entry);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No suggestions found.");
            return;
        }

        results.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Suggestions for \"" + prefix + "\":");

        int count = 1;

        for (Map.Entry<String, Integer> entry : results) {

            if (count > 10)
                break;

            System.out.println(count + ". \"" + entry.getKey() + "\" ("
                    + entry.getValue() + " searches)");

            count++;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AutocompleteSystem system = new AutocompleteSystem();

        int choice;

        do {

            System.out.println("\n--- Autocomplete Search System ---");
            System.out.println("1. Add/Update Search Query");
            System.out.println("2. Search Suggestions");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter search query: ");
                    String query = sc.nextLine();

                    system.updateFrequency(query);
                    break;

                case 2:

                    System.out.print("Enter search prefix: ");
                    String prefix = sc.nextLine();

                    system.search(prefix);
                    break;

                case 3:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 3);

        sc.close();
    }
}