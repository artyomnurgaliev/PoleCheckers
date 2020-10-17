import java.util.Objects;

public class Checker {

    private Color color;
    private boolean isKing;

    public Checker(Color color, boolean isKing) {
        this.color = color;
        this.isKing = isKing;
    }

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

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isKing() {
        return isKing;
    }

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

    public void setKing(boolean king) {
        isKing = king;
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

enum Color {
    BLACK,
    WHITE
}