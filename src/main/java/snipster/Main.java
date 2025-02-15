package snipster;
import java.util.List;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        SnippetRepository.exportSnippets("snippets.csv");
        // try {
        //     System.out.println("Clearing database and Lucene index");
        //     SnippetRepository.clearTable();
        //     System.out.println("Database and Lucene index cleared\n");

        //     System.out.println("Printing database contents after clearing");
        //     SnippetRepository.printSnippets();
            
        //     System.out.println("Printing Lucene index after clearing");
        //     try (LuceneIndexer indexer = LuceneIndexer.getInstance()) {
        //         indexer.printAllSnippets();
        //     }
            
        // } catch (Exception e) {
        //     System.out.println("Error during clearing");
        //     e.printStackTrace();
        // }

        // System.out.println("Inserting snippets");
        // SnippetRepository.insertSnippet("Hello World", "print('Hello World')", "Python");
        // SnippetRepository.insertSnippet("Loop", "for i in range(10): print(i)", "Python");
        // SnippetRepository.insertSnippet("Java Example", "System.out.println(\"Hello Java\");", "Java");

        // try (LuceneIndexer indexer = LuceneIndexer.getInstance()) {
        //     System.out.println("Printing database after insertions:");
        //     SnippetRepository.printSnippets();
            
        //     System.out.println("Printing Lucene indexes after insertions:");
        //     indexer.printAllSnippets();
        // } catch (Exception e) {
        //     System.out.println("Error initializing LuceneIndexer");
        //     e.printStackTrace();
        // }

        // Snippet details currently go back to view snippet page no matter what
        // Change parameters to store page
    }
}
