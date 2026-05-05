package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class Ranch extends Structures {

    private static final int EXPENSE = 3;
    private static final int PROFIT = 5;
    private static final int SCOREVALUE = 7;

    public Ranch(Player owner, int scoreModifier) {
        super(StructuresType.RANCH, ResourceType.FOOD, ResourceType.WOOD, EXPENSE, PROFIT, owner, ResourceType.STONE, SCOREVALUE, scoreModifier);
    }
    public static int getApNeeded() { return 5; }

    public static int getEXPENSE() {
        return EXPENSE;
    }

    @Override
    public void generateResource(int resourcesModifier) {
        if (production != ResourceType.NONE) {
            owner.addResource(production, (7 + level * 2) * resourcesModifier);
        }
    }

}