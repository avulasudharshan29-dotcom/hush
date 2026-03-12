import java.util.*;

public class PlagiarismDetectionSystem {

    HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    HashMap<String, String> documents = new HashMap<>();

    int N = 5;

    // Generate n-grams
    List<String> generateNGrams(String text) {

        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add document
    void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> grams = generateNGrams(text);

        for (String gram : grams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        System.out.println("Document " + docId + " indexed with " + grams.size() + " n-grams");
    }

    // Analyze document
    void analyzeDocument(String docId, String text) {

        List<String> grams = generateNGrams(text);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String otherDoc : ngramIndex.get(gram)) {

                    matchCount.put(otherDoc,
                            matchCount.getOrDefault(otherDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + doc + "\"");

            System.out.printf("Similarity: %.1f%% ", similarity);

            if (similarity > 50)
                System.out.println("(PLAGIARISM DETECTED)");
            else if (similarity > 10)
                System.out.println("(suspicious)");
            else
                System.out.println("(low similarity)");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        PlagiarismDetectionSystem system = new PlagiarismDetectionSystem();

        int choice;

        do {

            System.out.println("\n--- Plagiarism Detection System ---");
            System.out.println("1. Add Document");
            System.out.println("2. Analyze Document");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Document ID: ");
                    String docId = sc.nextLine();

                    System.out.println("Enter Document Text:");
                    String text = sc.nextLine();

                    system.addDocument(docId, text);
                    break;

                case 2:
                    System.out.print("Enter Document ID: ");
                    String newDocId = sc.nextLine();

                    System.out.println("Enter Document Text:");
                    String newText = sc.nextLine();

                    system.analyzeDocument(newDocId, newText);
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