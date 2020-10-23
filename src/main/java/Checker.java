import java.util.Objects;

public class Checker {

    private final Color color;

    public void setKing(boolean king) {
        isKing = king;
    }

    private boolean isKing;

    /**
     * Creates checker
     *
     * @param c - character to create from
     */
    public Checker(char c) {
        if (c == 'w' || c == 'W') {
            color = Color.WHITE;
        } else {
            color = Color.BLACK;
        }
        isKing = Character.isUpperCase(c);
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    /**
     * String representation
     *
     * @return string
     */
    @Override
    public String toString() {
        if (color == Color.WHITE) {
            if (isKing())
                return "W";
            else
                return "w";
        } else {
            if (isKing())
                return "B";
            else
                return "b";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checker checker = (Checker) o;
        return isKing == checker.isKing &&
                color == checker.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, isKing);
    }
}

/**
 * Colors of checker
 */
enum Color {
    BLACK,
    WHITE
}