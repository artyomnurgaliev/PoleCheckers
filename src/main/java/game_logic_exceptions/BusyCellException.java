package game_logic_exceptions;

public class BusyCellException extends GameLogicException {
    /**
     * Exception that you can throw if cell is busy
     *
     * @param errorMessage - type of error (busy cell)
     */
    public BusyCellException(String errorMessage) {
        super(errorMessage);
    }
}
