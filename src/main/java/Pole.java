import game_logic_exceptions.GameLogicException;
import game_logic_exceptions.WhiteCellException;

import java.util.ArrayList;

/**
 * Pole of checkers
 */
public class Pole {
    private final ArrayList<Checker> checkers = new ArrayList<>();
    private final int x;
    private final int y;

    /**
     * Creating pole by processing input
     *
     * @param pole_string - input
     * @throws GameLogicException (if the moves are against the rules)
     */
    public Pole(String pole_string) throws GameLogicException {
        int x1;
        int x_coord = pole_string.charAt(0);
        int offset = 'a';
        int upper_offset = 'A';
        x1 = x_coord - offset + 1;
        if (x1 > 8 || x1 < 1)
            x1 = x_coord - upper_offset + 1;
        this.x = x1;
        this.y = Character.getNumericValue(pole_string.charAt(1));
        for (int i = 3; i < pole_string.length(); ++i) {
            addChecker(new Checker(pole_string.charAt(i)));
        }
        if ((x + y) % 2 == 1) {
            throw new WhiteCellException("white cell");
        }
        if (x > 8 || x < 1 || y > 8 || y < 1) {
            throw new GameLogicException("error");
        }

    }

    /**
     * Creates empty pole from it position on checkerboard
     *
     * @param x - x coordinate (x in [1 - 8])
     * @param y - y coordinate (y in [1 - 8])
     */
    Pole(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Mark upper checker as a king
     */
    public void setKing() {
        checkers.get(0).setKing(true);
    }

    /**
     * Returns x pos on checkerboard
     *
     * @return x (x in [1 - 8])
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y pos on checkerboard
     *
     * @return y (y in [1 - 8])
     */
    public int getY() {
        return y;
    }

    /**
     * Checks whether pole is empty
     *
     * @return true if pole is empty
     */
    public boolean isEmpty() {
        return checkers.isEmpty();
    }

    /**
     * Returns string representation of a pole
     *
     * @return string
     */
    @Override
    public String toString() {
        int offset = 'a';
        offset += x - 1;
        char str_x = (char) offset;
        char str_y = (char) (y + '0');
        StringBuilder stringBuilder = new StringBuilder(35);
        stringBuilder.append(str_x);
        stringBuilder.append(str_y);
        stringBuilder.append("_");
        for (Checker checker : checkers)
            stringBuilder.append(checker.toString());
        return stringBuilder.toString();
    }

    /**
     * Checks that the checkers in this pole and another match (with the check for king)
     *
     * @param another - pole to compare with
     * @return - true if matches
     */
    public boolean isSameKindCheckers(Pole another) {
        if (checkers.size() != another.checkers.size()) {
            return false;
        }
        if (this.isKing() && !another.isKing()) {
            if (this.getColor() == Color.WHITE && y != 8) {
                return false;
            }
            if (this.getColor() == Color.BLACK && y != 1) {
                return false;
            }
        }
        if (this.isKing() && another.isKing() || !this.isKing() && !another.isKing()) {
            if (this.getColor() != another.getColor()) {
                return false;
            }
        }
        for (int i = 1; i < checkers.size(); ++i) {
            if (!checkers.get(i).equals(another.checkers.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * returns the last felled checker
     *
     * @return checker
     * @throws GameLogicException (if the moves are against the rules)
     */
    public Checker getLastCutChecker() throws GameLogicException {
        if (checkers.isEmpty()) {
            throw new GameLogicException("error");
        }
        return checkers.get(checkers.size() - 1);
    }

    /**
     * Add new checker in pole
     *
     * @param checker - checker to be added
     */
    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    /**
     * Returns color of the top checker in pole
     *
     * @return checker
     */
    public Color getColor() {
        return checkers.get(0).getColor();
    }

    /**
     * Returns whether the top checker in King
     *
     * @return true if king
     */
    public boolean isKing() {
        return checkers.get(0).isKing();
    }

    /**
     * Checks whether poles are on the same diagonal on checkerboard
     *
     * @param another - pole to check with
     * @return true if on the same diagonal
     */
    public boolean isOnSameDiagonal(Pole another) {
        return Math.abs(x - another.x) == Math.abs(y - another.y);
    }

    /**
     * Returns diagonal distance for poles on the same diagonal
     *
     * @param another - pole to find distance to
     * @return int distance
     */
    public int getDistance(Pole another) {
        return Math.abs(x - another.x);
    }

    /**
     * Removes top checker from pole
     * (use it if pole is cut)
     */
    public void removeChecker() {
        checkers.remove(0);
    }

    /**
     * Calculates hash of direction to another pole
     */
    int direction(Pole another) {
        if (another.x > x && another.y > y) return pos(new Pole(1, 1));
        if (another.x < x && another.y > y) return pos(new Pole(-1, 1));
        if (another.x > x && another.y < y) return pos(new Pole(1, -1));
        if (another.x < x && another.y < y) return pos(new Pole(-1, -1));

        // Unreachable
        return 0;
    }

    /**
     * Calculates hash for pole position
     *
     * @param pole - pole to calc hash for
     * @return integer x + y * 10
     */
    static int pos(Pole pole) {
        return pole.getX() * 10 + pole.getY();
    }

    /**
     * Check if pos is correct
     *
     * @param pos - pos of pole
     * @return true if correct
     */
    static boolean checkCorrectPos(int pos) {
        int x = pos / 10;
        int y = pos % 10;
        if ((x + y) % 2 == 1) {
            return false;
        }
        return x <= 8 && x >= 1 && y <= 8 && y >= 1;
    }
}

