package jogo.exceptions;

/**
 * Base exception class for all generic and custom game logic violations.
 * Serves as the root of the exception hierarchy within the game engine.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */

public class GameException extends Exception {

    /**
     * Constructs a new GameException with the specified detail message.
     *
     * @param message The specific error message detailing the cause of the exception.
     */
    public GameException(String message) {
        super(message);
    }
}

