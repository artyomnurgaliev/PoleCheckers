import game_logic_exceptions.GameLogicException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * Play pole checkers game
     *
     * @throws IOException        (if something wrong with input)
     * @throws GameLogicException (if something wrong with game logic)
     */
    public static void game() throws IOException, GameLogicException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String white_poles = br.readLine();
        String black_poles = br.readLine();

        CheckerBoard checkerBoard = new CheckerBoard(white_poles, black_poles);
        String line;
        while ((line = br.readLine()) != null) {
            if ("".equals(line)) break;
            checkerBoard.move(line);
        }
        checkerBoard.printFinalPosition();
    }


    public static void main(String[] args) throws IOException, GameLogicException {
        try {
            game();
        } catch (GameLogicException e) {
            System.out.println(e.getMessage());
        }

    }
}
