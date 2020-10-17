package game_logic_exceptions;

public class GameLogicException extends Exception {
    /**
     * Exception that you can throw if something against the rules of game occurred
     *
     * @param errorMessage - type of error
     */
    public GameLogicException(String errorMessage) {
        super(errorMessage);
    }
}
