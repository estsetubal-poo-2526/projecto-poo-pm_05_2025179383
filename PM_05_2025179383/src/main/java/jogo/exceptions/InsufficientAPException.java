package jogo.exceptions;

/**
 * Thrown when a player attempts to perform an action without having
 * the required amount of Action Points (AP).
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class InsufficientAPException extends GameException {

    /**
     * Constructs a new InsufficientAPException with a default error message.
     */
    public InsufficientAPException() {
        super("AP Insuficiente");
    }
}
