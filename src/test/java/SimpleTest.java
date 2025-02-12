import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import snipster.SnippetRepository;
import snipster.Snippet;

public class SimpleTest {
    @Test
    public void testSimple() {
        int x = 1 + 1;
        Assertions.assertEquals(2, x);
    }

    @Test
    public void testInsert() {
        List<Snippet> snippets = SnippetRepository.readSnippets();
        int size = snippets.size();

        SnippetRepository.insertSnippet("Test", "print(\"Test\")", "Python");
        snippets = SnippetRepository.readSnippets();

        Assertions.assertEquals(size + 1, snippets.size());
        Assertions.assertEquals("Test", snippets.getLast().getTitle());
    }
}
