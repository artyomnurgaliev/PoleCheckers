package game_logic_exceptions;

public class InvalidMoveException extends GameLogicException{
    /**
     * Exception that you can throw if move is invalid
     *
     * @param errorMessage - type of error (invalid move)
     */
    public InvalidMoveException(String errorMessage) {
        super(errorMessage);
    }
}
