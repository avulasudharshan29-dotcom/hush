import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class MultiLevelCacheSystem {

    // L1 Cache (Memory) - LRU using LinkedHashMap
    LinkedHashMap<String, VideoData> L1 = new LinkedHashMap<>(10000, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
            return size() > 10000;
        }
    };

    // L2 Cache (SSD simulated)
    HashMap<String, VideoData> L2 = new HashMap<>();

    // L3 Database
    HashMap<String, VideoData> L3 = new HashMap<>();

    // Access tracking
    HashMap<String, Integer> accessCount = new HashMap<>();

    int L1Hits = 0, L2Hits = 0, L3Hits = 0;

    // Get video
    void getVideo(String videoId) {

        long start = System.currentTimeMillis();

        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            System.out.println("Video Content: " + L1.get(videoId).content);

        } else if (L2.containsKey(videoId)) {

            L2Hits++;

            System.out.println("L1 Cache MISS");
            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, video);
            System.out.println("Promoted to L1");

        } else if (L3.containsKey(videoId)) {

            L3Hits++;

            System.out.println("L1 Cache MISS");
            System.out.println("L2 Cache MISS");
            System.out.println("L3 Database HIT (150ms)");

            VideoData video = L3.get(videoId);

            L2.put(videoId, video);

            System.out.println("Added to L2");

        } else {

            System.out.println("Video not found in database.");
            return;
        }

        accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);

        long end = System.currentTimeMillis();
        System.out.println("Total Time: " + (end - start) + " ms");
    }

    // Add video to database
    void addVideo(String videoId, String content) {

        L3.put(videoId, new VideoData(videoId, content));
        System.out.println("Video added to database.");
    }

    // Invalidate cache
    void invalidateVideo(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // Statistics
    void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        if (total == 0) {
            System.out.println("No requests yet.");
            return;
        }

        double L1Rate = (L1Hits * 100.0) / total;
        double L2Rate = (L2Hits * 100.0) / total;
        double L3Rate = (L3Hits * 100.0) / total;

        System.out.println("L1 Hit Rate: " + L1Rate + "%");
        System.out.println("L2 Hit Rate: " + L2Rate + "%");
        System.out.println("L3 Hit Rate: " + L3Rate + "%");

        double overall = ((L1Hits + L2Hits) * 100.0) / total;

        System.out.println("Overall Cache Hit Rate: " + overall + "%");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MultiLevelCacheSystem cache = new MultiLevelCacheSystem();

        int choice;

        do {

            System.out.println("\n--- Multi-Level Cache System ---");
            System.out.println("1. Add Video to Database");
            System.out.println("2. Get Video");
            System.out.println("3. Invalidate Cache");
            System.out.println("4. Get Statistics");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Video ID: ");
                    String vid = sc.nextLine();

                    System.out.print("Enter Video Content: ");
                    String content = sc.nextLine();

                    cache.addVideo(vid, content);
                    break;

                case 2:

                    System.out.print("Enter Video ID: ");
                    String id = sc.nextLine();

                    cache.getVideo(id);
                    break;

                case 3:

                    System.out.print("Enter Video ID to invalidate: ");
                    String inv = sc.nextLine();

                    cache.invalidateVideo(inv);
                    break;

                case 4:

                    cache.getStatistics();
                    break;

                case 5:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}