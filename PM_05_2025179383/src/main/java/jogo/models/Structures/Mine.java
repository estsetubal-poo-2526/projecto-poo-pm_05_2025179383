package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Mine extends Structures {

    private static final int EXPENSE = 3;
    private static final int PROFIT = 7;
    private static final int SCOREVALUE = 4;

    public Mine(Player owner) {
        super("Mine", ResourceType.STONE, ResourceType.WOOD, EXPENSE, PROFIT, owner, ResourceType.WOOD, SCOREVALUE);
    }
    public static int getApNeeded() { return 5; }

    public static int getExpense() {
        return EXPENSE;
    }
}