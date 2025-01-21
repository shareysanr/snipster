package snipster;

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
        SnippetRepository.readSnippets();

        System.out.println("Testing out Lucene Indexer");
        try {
            LuceneIndexer indexer = new LuceneIndexer("index");
            // indexer.indexSnippet("For loop", "for(int i = 0; i < 10; i++) {}", "Java");
            // indexer.indexSnippet("Hello World", "System.out.println(\"Hello World\");", "Java");

            System.out.println("Searching 'Hello':");
            indexer.searchSnippets("Hello");

            System.out.println("Searching 'i++':");
            indexer.searchSnippets("i++");

            indexer.close();
        } catch (Exception e) {
            System.out.println("Error with Lucene Indexer");
            e.printStackTrace();
        }
    }
    
}
