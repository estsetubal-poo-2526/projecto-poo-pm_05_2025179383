package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Ranch extends Structures {

    private static final int EXPENSE = 3;
    private static final int PROFIT = 5;
    private static final int SCOREVALUE = 7;

    public Ranch(Player owner) {
        super("Ranch", ResourceType.FOOD, ResourceType.WOOD, EXPENSE, PROFIT, owner, ResourceType.STONE, SCOREVALUE);
    }
    public static int getApNeeded() { return 5; }

    public static int getEXPENSE() {
        return EXPENSE;
    }
}