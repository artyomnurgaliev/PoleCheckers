import game_logic_exceptions.GameLogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoleTest {
    Pole pole;

    @BeforeEach
    void setUp() {
        try {
            pole = new Pole("b6_wwbb");
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getX() {
        assertEquals(pole.getX(), 2);
    }

    @Test
    void getY() {
        assertEquals(pole.getY(), 6);
    }

    @Test
    void isEmpty() {
        assertFalse(pole.isEmpty());
    }

    @Test
    void testToString() {
        assertEquals(pole.toString(), "b6_wwbb");
    }

    @Test
    void isSameKindCheckers() {
        try {
            assertTrue(pole.isSameKindCheckers(new Pole("b6_wwbb")));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }

        try {
            Pole pole1 = new Pole("a7_w");
            Pole pole2 = new Pole("b8_W");
            assertTrue(pole1.isSameKindCheckers(pole2));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLastCutChecker() {
        try {
            assertEquals(pole.getLastCutChecker(), new Checker('b'));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addChecker() {
        try {
            Pole pole = new Pole("a1_w");
            pole.addChecker(new Checker('b'));
            assertEquals(pole.toString(), new Pole("a1_wb").toString());
        } catch (GameLogicException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getColor() {
        assertEquals(pole.getColor(), Color.WHITE);
    }

    @Test
    void isKing() {
        assertFalse(pole.isKing());
    }

    @Test
    void isOnSameDiagonal() {
        try {
            Pole pole1 = new Pole("a5_w");
            Pole pole2 = new Pole("a4_w");
            assertTrue(pole.isOnSameDiagonal(pole1));
            assertFalse(pole.isOnSameDiagonal(pole2));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getDistance() {
        try {
            Pole pole1 = new Pole("a5_w");
            assertEquals(pole.getDistance(pole1), 1);
            Pole pole2 = new Pole("d4_w");
            assertEquals(pole.getDistance(pole2), 2);
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeChecker() {
        try {
            Pole pole1 = new Pole("a5_w");
            pole1.removeChecker();
            assertTrue(pole1.isEmpty());
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void direction() {
        try {
            Pole pole1 = new Pole("a5_w");
            assertEquals(pole.direction(pole1), -11);
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void pos() {
        assertEquals(Pole.pos(pole), 62);
    }
}