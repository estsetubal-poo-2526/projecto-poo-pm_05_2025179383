package jogo.exceptions;

/**
 * Thrown when a player attempts to perform an action or interaction on a structure
 * that belongs to another player.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class UnauthorizedActionException extends GameException {

    /**
     * Constructs a new UnauthorizedActionException with a default error message.
     */
    public UnauthorizedActionException() {
        super("Esta Estrutura não é Sua!");
    }
}