import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import snipster.SnippetRepository;
import org.junit.jupiter.api.Assertions;

public class SnippetExportTest {

    @Test
    public void testCSVExportCreatesFile() {
        String fileName = "test_snippets.csv";

        SnippetRepository.exportSnippets(fileName);

        Path path = Path.of(fileName);
        Assertions.assertTrue(Files.exists(path));

        try {
            Files.delete(path);
        } catch (Exception e) {
            System.out.println("Failed deleting file ");
            System.out.println(e.getMessage());
        }
    }
}
