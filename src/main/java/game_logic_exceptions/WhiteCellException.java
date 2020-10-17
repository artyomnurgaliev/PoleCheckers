package game_logic_exceptions;

public class WhiteCellException extends GameLogicException {
    /**
     * Exception that you can throw if cell is white
     *
     * @param errorMessage - type of error (white cell)
     */
    public WhiteCellException(String errorMessage) {
        super(errorMessage);
    }
}
