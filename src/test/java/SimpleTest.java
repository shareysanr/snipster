import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTest {
    @Test
    public void testSimple() {
        int x = 1 + 1;
        Assertions.assertEquals(2, x);
    }
}
