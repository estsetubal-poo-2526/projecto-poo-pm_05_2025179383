package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class City extends Structures {

    private static final int EXPENSE = 10;
    private static final int PROFIT = 0;
    private static final int SCOREVALUE = 15;

    public City(Player owner) {
        super("City", ResourceType.NONE, ResourceType.FOOD, EXPENSE, PROFIT, owner, ResourceType.STONE, SCOREVALUE);
        owner.addActionPoints(3);
    }

    public static int getApNeeded() {
        return 3;
    }

    @Override
    public boolean upgradeStructure() {
        if (super.upgradeStructure()) {
            owner.addActionPoints(2 * level); // Bónus específico da cidade
            return true;
        }
        return false;
    }

    public static int getEXPENSE() {
        return EXPENSE;
    }
}