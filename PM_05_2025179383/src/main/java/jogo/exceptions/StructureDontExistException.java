package jogo.exceptions;

public class StructureDontExistException extends GameException {
    public StructureDontExistException() {
        super("Essa Estrutura não Existe!");
    }
}
