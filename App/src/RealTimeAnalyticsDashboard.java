import java.util.*;

public class RealTimeAnalyticsDashboard {

    HashMap<String, Integer> pageViews = new HashMap<>();
    HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process page view event
    void processEvent(String url, String userId, String source) {

        // Page view count
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Traffic source count
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);

        System.out.println("Event processed successfully.");
    }

    // Display dashboard
    void getDashboard() {

        System.out.println("\nTop Pages:");

        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 1;

        for (Map.Entry<String, Integer> entry : list) {

            if (count > 10)
                break;

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(count + ". " + url + " - " + views +
                    " views (" + unique + " unique)");

            count++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int val : trafficSources.values())
            total += val;

        for (String source : trafficSources.keySet()) {

            int countSource = trafficSources.get(source);
            double percent = (countSource * 100.0) / total;

            System.out.printf("%s: %.1f%%\n", source, percent);
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RealTimeAnalyticsDashboard system = new RealTimeAnalyticsDashboard();

        int choice;

        do {

            System.out.println("\n--- Real-Time Analytics Dashboard ---");
            System.out.println("1. Process Page View Event");
            System.out.println("2. Show Dashboard");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Page URL: ");
                    String url = sc.nextLine();

                    System.out.print("Enter User ID: ");
                    String userId = sc.nextLine();

                    System.out.print("Enter Traffic Source: ");
                    String source = sc.nextLine();

                    system.processEvent(url, userId, source);

                    break;

                case 2:

                    system.getDashboard();
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