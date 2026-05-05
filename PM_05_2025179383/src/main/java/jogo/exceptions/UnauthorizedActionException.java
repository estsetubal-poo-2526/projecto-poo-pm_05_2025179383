package jogo.exceptions;

public class UnauthorizedActionException extends GameException {
    public UnauthorizedActionException() {

        super("Esta Estrutura não é Sua!");
    }
}
