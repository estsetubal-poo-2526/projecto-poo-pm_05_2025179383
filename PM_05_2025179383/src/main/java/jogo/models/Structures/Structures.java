package jogo.models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;

public abstract class Structures {

    protected final StructuresType STRUCTURE_TYPE;
    protected final ResourceType PRODUCTION;
    protected final ResourceType MAINTENANCE_MATERIAL;
    protected final ResourceType UPGRADE_MATERIAL;

    protected int expense;
    protected int profit;
    protected int level;

    protected final Player owner;

    public Structures(
            StructuresType structureType,
            ResourceType production,
            ResourceType maintenanceMaterial,
            int expense,
            int profit,
            Player owner,
            ResourceType upgradeMaterial,
            int level,
            int scoreValue,
            int scoreModifier
    ) {
        this.STRUCTURE_TYPE = structureType;
        this.PRODUCTION = production;
        this.MAINTENANCE_MATERIAL = maintenanceMaterial;
        this.UPGRADE_MATERIAL = upgradeMaterial;

        this.expense = expense;
        this.profit = profit;
        this.level = level;

        this.owner = owner;

        this.owner.addScore(scoreValue * scoreModifier);
    }

    public  abstract void generateResource(int resourcesModifier);

    public String consumeResources() {
        int totalCost = expense * level;

        if (owner.removeResource(MAINTENANCE_MATERIAL, totalCost)) {
            return String.format(
                    "%s (%s): Manutenção paga (%d %s).",
                    STRUCTURE_TYPE,
                    owner.getName(),
                    totalCost,
                    MAINTENANCE_MATERIAL
            );
        }

        level--;

        if (level > 0) {
            setLevel(level);

            return String.format(
                    "%s de %s falhou a manutenção! Degradou-se para Nível %d.",
                    STRUCTURE_TYPE,
                    owner.getName(),
                    level
            );
        }

        return String.format(
                "A estrutura %s de %s foi destruída por falta de pagamento!",
                STRUCTURE_TYPE,
                owner.getName()
        );
    }

    public abstract boolean upgradeStructure(int scoreModifier) throws InsufficientResourcesException;

    public abstract void setLevel(int level);

    public abstract int getFutureProfit();

    public abstract int getFutureExpense();

    public abstract int getUpgradeCost();

    public abstract int getAPCostToUpgrade();

    public Player getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return String.valueOf(STRUCTURE_TYPE);
    }

    public int getExpense() {
        return expense;
    }

    public int getProfit() {
        return profit;
    }

    public ResourceType getProduction() {
        return PRODUCTION;
    }

    public ResourceType getCostType() {
        return MAINTENANCE_MATERIAL;
    }

    public ResourceType getUpgradeMaterial() {
        return UPGRADE_MATERIAL;
    }

    @Override
    public String toString() {
        return STRUCTURE_TYPE + " nível " + level;
    }
}