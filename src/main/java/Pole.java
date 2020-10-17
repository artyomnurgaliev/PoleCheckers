import java.util.ArrayList;

public class Pole {
    private ArrayList<Checker> checkers = new ArrayList<>();
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Pole(String pole_string) throws GameLogicException {
        int x_coord = pole_string.charAt(0);
        int offset = 'a';
        this.x = x_coord - offset + 1;
        this.y = Character.getNumericValue(pole_string.charAt(1));
        for (int i = 3; i < pole_string.length(); ++i) {
            addChecker(new Checker(pole_string.charAt(i)));
        }
        if ((x + y) % 2 == 1) {
            throw new GameLogicException("white cell");
        }
        if (x > 8 || x < 1 || y > 8 || y < 1) {
            throw new GameLogicException("error");
        }

    }

    public boolean isSameCheckers(Pole another) {
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
        for (int i = 1; i < checkers.size(); ++i) {
            if (!checkers.get(i).equals(another.checkers.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Checker getLastCutChecker() throws GameLogicException {
        if (checkers.isEmpty()) {
            throw new GameLogicException("error");
        }
        return checkers.get(checkers.size() - 1);
    }

    public void addChecker(Checker checker) {
        checkers.add(checker);
    }
    public Color getColor() {
        return checkers.get(0).getColor();
    }

    public boolean isKing() {
        return checkers.get(0).isKing();
    }

    public boolean isOnSameDiagonal(Pole another) {
        return Math.abs(x - another.x) == Math.abs(y - another.y);
    }

    public int getDistance(Pole another) {
        return Math.abs(x - another.x);
    }

    public void removeChecker() {
        checkers.remove(0);
    }

    public int direction_hash(Pole another) {
        if (another.x > x && another.y > y) return 11;
        if (another.x < x && another.y > y) return -9;
        if (another.x > x && another.y < y) return 9;
        if (another.x < x && another.y < y) return -11;

        // Unreachable
        return 0;
    }

    public static int hash(Pole pole) {
        return pole.getX() * 10 + pole.getY();
    }
}

