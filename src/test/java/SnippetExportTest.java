import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import snipster.SnippetRepository;
import org.junit.jupiter.api.Assertions;

public class SnippetExportTest {

    @Test
    public void testCSVExportFile() {
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

    @Test
    public void testCSVExportContent() throws Exception {
        String filePath = "test_snippets.csv";
        SnippetRepository.exportSnippets(filePath);

        Path path = Path.of(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            Assertions.assertEquals("ID,Title,Code,Tags", header);

            String firstSnippet = reader.readLine();
            Assertions.assertNotNull(firstSnippet);
        }

        Files.delete(path);
    }
}
