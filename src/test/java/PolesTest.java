import game_logic_exceptions.GameLogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PolesTest {
    Poles poles;

    @BeforeEach
    void setUp() {
        poles = new Poles();
    }

    @Test
    void addPole() {
        Exception exception = assertThrows(GameLogicException.class, () ->
                poles.addPole("a2_w"));
        assertEquals("white cell", exception.getMessage());
    }

    @Test
    void checkBusyCell() {
        try {
            poles.addPole("a1_w");
            assertTrue(poles.checkBusyCell(new Pole("a1_w")));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCheckBusyCell() {
        try {
            poles.addPole("a1_w");
            assertTrue(poles.checkBusyCell(Pole.pos(new Pole("a1_w"))));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isCanCut() {
        try {
            poles.addPole("a1_W");
            poles.addPole("d4_b");
            assertTrue(poles.isCanCut(new Pole("a1_W"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_W");
            poles.addPole("c3_w");
            poles.addPole("d4_b");
            assertFalse(poles.isCanCut(new Pole("a1_W"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_W");
            poles.addPole("d4_b");
            poles.addPole("e5_w");
            assertFalse(poles.isCanCut(new Pole("a1_W"), new HashSet<>()));

            poles.addPole("a1_W");
            poles.addPole("d4_b");
            HashSet<Integer> cut_poles = new HashSet<>();
            cut_poles.add(44);
            assertFalse(poles.isCanCut(new Pole("a1_W"), cut_poles));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void normalCut() {
        try {
            poles.addPole("a1_W");
            poles.addPole("d4_b");
            assertTrue(poles.normalCut(new Pole("a1_W"), new Pole("e5_Wb"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_W");
            poles.addPole("d4_b");
            assertTrue(poles.normalCut(new Pole("a1_W"), new Pole("f6_Wb"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_W");
            poles.addPole("d4_b");
            poles.addPole("f6_w");
            assertFalse(poles.normalCut(new Pole("a1_W"), new Pole("g7_Wb"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_w");
            poles.addPole("d4_b");
            assertFalse(poles.normalCut(new Pole("a1_w"), new Pole("g7_wb"), new HashSet<>()));

            poles = new Poles();
            poles.addPole("a1_wwwbbw");
            poles.addPole("b2_b");
            assertTrue(poles.normalCut(new Pole("a1_wwwbbw"),
                    new Pole("c3_wwwbbwb"),
                    new HashSet<>()));

            poles = new Poles();
            poles.addPole("b6_wwwbbw");
            poles.addPole("c7_b");
            assertTrue(poles.normalCut(new Pole("b6_wwwbbw"),
                    new Pole("d8_Wwwbbwb"),
                    new HashSet<>()));
        } catch (GameLogicException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkNewPoleAndDoMove() {
        try {
            Pole pole1 = new Pole("a1_w");
            Pole pole2 = new Pole("b2_w");
            poles.addPole("a1_w");
            poles.checkNewPoleAndDoMove(pole1, pole2, new HashSet<>());
            assertFalse(poles.getPoles().containsKey(11));
            assertTrue(poles.getPoles().containsKey(22));

            poles = new Poles();
            Pole pole3 = new Pole("a1_w");
            Pole pole4 = new Pole("b2_w");
            poles.addPole("b2_w");
            Exception exception = assertThrows(GameLogicException.class, () ->
                    poles.checkNewPoleAndDoMove(pole3, pole4, new HashSet<>()));
            assertEquals("busy cell", exception.getMessage());

            poles = new Poles();
            Pole pole5 = new Pole("a3_w");
            Pole pole6 = new Pole("b2_b");
            poles.addPole("a3_w");
            poles.addPole("b2_b");
            Pole new_pole = new Pole("b4_w");
            exception = assertThrows(GameLogicException.class, () ->
                    poles.checkNewPoleAndDoMove(pole5, new_pole, new HashSet<>()));
            assertEquals("invalid move", exception.getMessage());
        } catch (GameLogicException e) {
            e.printStackTrace();
        }

    }
}