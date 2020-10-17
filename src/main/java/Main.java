import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
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
        checkerBoard.printResult();
    }


    public static void main(String[] args) throws IOException, GameLogicException {
        game();
    }
}
