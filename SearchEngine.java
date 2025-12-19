import java.io.*;
import java.util.*;

public class SearchEngine {

    public static Map<String, String> readFiles(String folderPath) throws IOException {
        Map<String, String> documents = new HashMap<>();
        File folder = new File(folderPath);

        for (File file : folder.listFiles()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
            br.close();
            documents.put(file.getName(), content.toString());
        }
        return documents;
    }

    public static String[] cleanText(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("[^a-z ]", "");
        return text.split("\\s+");
    }

    public static Map<String, Set<String>> buildIndex(Map<String, String> documents) {
        Map<String, Set<String>> index = new HashMap<>();

        for (String file : documents.keySet()) {
            String[] words = cleanText(documents.get(file));

            for (String word : words) {
                if (!index.containsKey(word)) {
                    index.put(word, new HashSet<>());
                }
                index.get(word).add(file);
            }
        }
        return index;
    }

    public static Set<String> search(String keyword, Map<String, Set<String>> index) {
        keyword = keyword.toLowerCase();
        return index.getOrDefault(keyword, new HashSet<>());
    }

    public static void main(String[] args) throws IOException {

        Map<String, String> documents = readFiles("documents");
        Map<String, Set<String>> index = buildIndex(documents);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Search word (or exit): ");
            String query = sc.nextLine();

            if (query.equalsIgnoreCase("exit")) {
                break;
            }

            Set<String> result = search(query, index);

            if (result.isEmpty()) {
                System.out.println("No results found.");
            } else {
                System.out.println("Found in files: " + result);
            }
        }
        sc.close();
    }
}