import java.util.StringTokenizer;

public class CheckerBoard {
    // Storage of poles on checkerboard
    private final Poles poles = new Poles();

    /**
     * Create checkerboard from two input strings
     * @param white_poles - string representation of white poles
     * @param black_poles - string representation of black poles
     * @throws GameLogicException (if something against the rules)
     */
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

    /**
     * Prints final position of checkerboard
     */
    public void printFinalPosition() {
        poles.printPoles();
    }

    /**
     * Process moves
     * @param moves - moves of one turn (one white and one black)
     * @throws GameLogicException (if the moves are against the rules)
     */
    public void move(String moves) throws GameLogicException {
        StringTokenizer st = new StringTokenizer(moves, " ");
        String white_moves = st.nextToken();
        String black_moves = st.nextToken();

        poles.move(white_moves);
        poles.move(black_moves);
    }
}
