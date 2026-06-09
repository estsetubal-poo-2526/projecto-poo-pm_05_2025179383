package jogo.exceptions;

/**
 * Thrown when an operation attempts to interact with or retrieve a structure
 * from a map coordinate that is currently empty.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */

public class StructureDontExistException extends GameException {

    /**
     * Constructs a new StructureDontExistException with a default error message.
     */
    public StructureDontExistException() {
        super("Essa Estrutura não Existe!");
    }
}