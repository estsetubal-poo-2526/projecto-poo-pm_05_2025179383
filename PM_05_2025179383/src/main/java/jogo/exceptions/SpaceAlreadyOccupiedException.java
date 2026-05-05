package jogo.exceptions;

public class SpaceAlreadyOccupiedException extends GameException {
    public SpaceAlreadyOccupiedException() {
        super("Espaço Já Ocupado! ");
    }
}
