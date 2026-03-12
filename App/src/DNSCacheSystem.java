import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long timestamp;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, int ttl) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.timestamp = System.currentTimeMillis();
        this.expiryTime = this.timestamp + (ttl * 1000);
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSCacheSystem {

    HashMap<String, DNSEntry> cache = new HashMap<>();
    LinkedList<String> lruList = new LinkedList<>();

    int maxSize = 5;
    int hits = 0;
    int misses = 0;

    Scanner sc = new Scanner(System.in);

    // Simulate upstream DNS query
    String queryUpstream(String domain) {
        Random r = new Random();
        return "172.217.14." + (200 + r.nextInt(50));
    }

    void resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                lruList.remove(domain);
                lruList.addLast(domain);

                System.out.println("Cache HIT → " + entry.ipAddress + " (retrieved in <1ms)");
                return;
            }
            else {
                System.out.println("Cache EXPIRED → Query upstream");
                cache.remove(domain);
                lruList.remove(domain);
            }
        }

        misses++;

        String ip = queryUpstream(domain);
        int ttl = 300;

        if (cache.size() >= maxSize) {
            String oldest = lruList.removeFirst();
            cache.remove(oldest);
            System.out.println("LRU Evicted: " + oldest);
        }

        DNSEntry newEntry = new DNSEntry(domain, ip, ttl);
        cache.put(domain, newEntry);
        lruList.add(domain);

        System.out.println("Cache MISS → Query upstream → " + ip + " (TTL: " + ttl + "s)");
    }

    void getCacheStats() {

        int total = hits + misses;

        if (total == 0) {
            System.out.println("No lookups yet.");
            return;
        }

        double hitRate = (hits * 100.0) / total;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    void removeExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                lruList.remove(entry.getKey());
                it.remove();
                System.out.println("Expired entry removed: " + entry.getKey());
            }
        }
    }

    public static void main(String[] args) {

        DNSCacheSystem dns = new DNSCacheSystem();
        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n--- DNS Cache System ---");
            System.out.println("1. Resolve Domain");
            System.out.println("2. Show Cache Stats");
            System.out.println("3. Clean Expired Entries");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter domain: ");
                    String domain = sc.nextLine();
                    dns.resolve(domain);
                    break;

                case 2:
                    dns.getCacheStats();
                    break;

                case 3:
                    dns.removeExpiredEntries();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 4);
    }
}