package snipster;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // SnippetRepository.createTable();

        // SnippetRepository.insertSnippet("First insertion", "print('This is first!')", "Python");
        // SnippetRepository.insertSnippet("Second insertion", "print('This is second!')", "Python");
        
        // System.out.println("After insertions:");
        // SnippetRepository.readSnippets();

        // SnippetRepository.updateSnippet(1, "Updated Title", "System.out.println('Updated Code');", "Java");
        
        // System.out.println("After update:");
        // SnippetRepository.readSnippets();

        // SnippetRepository.deleteSnippet(1);

        // System.out.println("After deletion:");
        // SnippetRepository.readSnippets();

        System.out.println("Testing out Lucene Indexer");
        try {
            LuceneIndexer indexer = new LuceneIndexer("index");
            // indexer.indexSnippet("For loop", "for(int i = 0; i < 10; i++) {}", "Java");
            // indexer.indexSnippet("Hello World", "System.out.println(\"Hello World\");", "Java");
            indexer.clearIndex();
            indexer.printAllSnippets();
            indexer.close();
        } catch (Exception e) {
            System.out.println("Error with Lucene Indexer");
            e.printStackTrace();
        }
        // SnippetRepository.clearTable();
        // SnippetRepository.insertSnippet("First insertion", "print('This is first!')", "Python");
        // SnippetRepository.insertSnippet("Second insertion", "print('This is second!')", "Python");
        SnippetRepository.readSnippets();
        System.out.println("SNIPPET TITLES\n\n");
        List<String> titles = SnippetRepository.readSnippetTitles();
        for (String title: titles) {
            System.out.println(title);
        }

        List<Integer> ids = SnippetRepository.readSnippetIds();
        System.out.println("Snippet IDs: " + ids);
    }
}
