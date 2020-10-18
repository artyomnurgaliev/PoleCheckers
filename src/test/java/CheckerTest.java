import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {
    Checker checker;

    @BeforeEach
    void setUp() {
        checker = new Checker('w');
    }

    @Test
    void getColor() {
        assertEquals(checker.getColor(), Color.WHITE);
    }

    @Test
    void isKing() {
        assertFalse(checker.isKing());
    }

    @Test
    void testToString() {
        assertEquals("w", checker.toString());
    }
}