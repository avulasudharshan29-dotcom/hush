import java.util.*;

class TokenBucket {

    int tokens;
    int maxTokens;
    int refillRate;
    long lastRefillTime;

    TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    void refill() {

        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - lastRefillTime) / 1000;

        int tokensToAdd = (int) (elapsedSeconds * refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }
}

public class DistributedRateLimiter {

    HashMap<String, TokenBucket> clients = new HashMap<>();

    int LIMIT = 1000;
    int REFILL_RATE = LIMIT / 3600;

    // Check rate limit
    void checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(LIMIT, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            System.out.println("checkRateLimit(clientId=\"" + clientId + "\") → Allowed ("
                    + bucket.tokens + " requests remaining)");
        }
        else {

            System.out.println("checkRateLimit(clientId=\"" + clientId + "\") → Denied (0 requests remaining)");
        }
    }

    // Get rate limit status
    void getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId)) {
            System.out.println("Client not found");
            return;
        }

        TokenBucket bucket = clients.get(clientId);

        int used = LIMIT - bucket.tokens;

        System.out.println("getRateLimitStatus(\"" + clientId + "\") → {used: "
                + used + ", limit: " + LIMIT + "}");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DistributedRateLimiter system = new DistributedRateLimiter();

        int choice;

        do {

            System.out.println("\n--- Distributed Rate Limiter ---");
            System.out.println("1. Check Rate Limit");
            System.out.println("2. Get Rate Limit Status");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Client ID: ");
                    String clientId = sc.nextLine();

                    system.checkRateLimit(clientId);
                    break;

                case 2:

                    System.out.print("Enter Client ID: ");
                    String id = sc.nextLine();

                    system.getRateLimitStatus(id);
                    break;

                case 3:

                    System.out.println("Exiting...");
                    break;

                default:

                    System.out.println("Invalid choice");
            }

        } while (choice != 3);

        sc.close();
    }
}