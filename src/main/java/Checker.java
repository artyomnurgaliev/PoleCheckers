import sun.jvm.hotspot.code.Location;

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

    public void setKing(boolean king) {
        isKing = king;
    }
}

enum Color {
    BLACK,
    WHITE
}