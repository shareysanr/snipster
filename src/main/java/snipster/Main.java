package snipster;

public class Main {
    public static void main(String[] args) {
        SnippetRepository.createTable();

        SnippetRepository.insertSnippet("First insertion", "print('This is first!')", "Python");
        SnippetRepository.insertSnippet("Second insertion", "print('This is second!')", "Python");
        
        System.out.println("After insertions:");
        SnippetRepository.readSnippets();

        SnippetRepository.updateSnippet(1, "Updated Title", "System.out.println('Updated Code');", "Java");
        
        System.out.println("After update:");
        SnippetRepository.readSnippets();

        SnippetRepository.deleteSnippet(1);

        System.out.println("After deletion:");
        SnippetRepository.readSnippets();
    }
    
}
