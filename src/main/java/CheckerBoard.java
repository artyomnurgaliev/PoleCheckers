import java.util.StringTokenizer;

public class CheckerBoard {
    private Poles poles = new Poles();

    public CheckerBoard(String white_poles, String black_poles) throws GameLogicException {
        StringTokenizer st = new StringTokenizer(white_poles, " ");
        while (st.hasMoreTokens()) {
            this.poles.addPole(st.nextToken());
        }
        st = new StringTokenizer(black_poles, " ");
        while (st.hasMoreTokens()) {
            this.poles.addPole(st.nextToken());
        }
    }

    public void printResult() {
        poles.printResult();
    }

    public void move(String moves) throws GameLogicException {
        StringTokenizer st = new StringTokenizer(moves, " ");
        String white_moves = st.nextToken();
        String black_moves = st.nextToken();

        poles.move(white_moves);
        poles.move(black_moves);
    }
}
