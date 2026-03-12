import java.util.*;

public class SocialMediaUsernameAvailabilityChecker {

    HashMap<String, Integer> userMap = new HashMap<>();
    HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Check username availability
    public boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        if (userMap.containsKey(username)) {
            return false;
        } else {
            return true;
        }
    }

    // Register user
    public void registerUser(String username, int userId) {
        if (checkAvailability(username)) {
            userMap.put(username, userId);
            System.out.println("Username registered successfully.");
        } else {
            System.out.println("Username already taken.");
        }
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!userMap.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String modified = username.replace("_", ".");
        if (!userMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Show popularity
    public void showPopularity() {
        System.out.println("Username Attempt Frequency:");
        for (String user : attemptFrequency.keySet()) {
            System.out.println(user + " -> " + attemptFrequency.get(user));
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SocialMediaUsernameAvailabilityChecker system = new SocialMediaUsernameAvailabilityChecker();

        // Sample existing users
        system.userMap.put("john_doe", 101);
        system.userMap.put("alex_king", 102);

        int choice;

        do {
            System.out.println("\n--- Social Media Username Checker ---");
            System.out.println("1. Check Username Availability");
            System.out.println("2. Register Username");
            System.out.println("3. Suggest Alternatives");
            System.out.println("4. Show Username Popularity");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();

                    if (system.checkAvailability(username)) {
                        System.out.println("Username is available.");
                    } else {
                        System.out.println("Username already taken.");
                    }
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String user = sc.nextLine();
                    System.out.print("Enter user ID: ");
                    int id = sc.nextInt();
                    system.registerUser(user, id);
                    break;

                case 3:
                    System.out.print("Enter username: ");
                    String name = sc.nextLine();
                    System.out.println("Suggestions: " + system.suggestAlternatives(name));
                    break;

                case 4:
                    system.showPopularity();
                    break;

                case 5:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}