package jogo.exceptions;

/**
 * Thrown when a player attempts to place or construct a structure on a map tile
 * that is already occupied by another structure.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class SpaceAlreadyOccupiedException extends GameException {

    /**
     * Constructs a new SpaceAlreadyOccupiedException with a default error message.
     */
    public SpaceAlreadyOccupiedException() {
        super("Espaço Já Ocupado! ");
    }
}