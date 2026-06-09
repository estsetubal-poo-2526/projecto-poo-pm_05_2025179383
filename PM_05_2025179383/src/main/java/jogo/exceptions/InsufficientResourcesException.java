package jogo.exceptions;

/**
 * Thrown when a player attempts to construct or upgrade a structure
 * without having the required amount of specific material resources.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class InsufficientResourcesException extends GameException {

    /**
     * Constructs a new InsufficientResourcesException with a default error message.
     */
    public InsufficientResourcesException() {
        super("Recursos Insuficientes! ");
    }
}