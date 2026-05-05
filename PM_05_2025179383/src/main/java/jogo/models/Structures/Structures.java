package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.exceptions.*;

public abstract class Structures {
    protected final StructuresType structureType;
    protected final ResourceType production;
    protected final ResourceType costType; // Recurso necessário para manutenção
    protected final ResourceType upgradeMaterial;

    protected int expense;
    protected int profit;
    protected int level;
    protected final Player owner;

    public Structures(StructuresType structureType,
                      ResourceType production,
                      ResourceType costType,
                      int expense,
                      int profit,
                      Player owner,
                      ResourceType upgradeMaterial,
                      int scoreValue,
                      int scoreModifier) {

        this.structureType = structureType;
        this.production = production;
        this.costType = costType;
        this.expense = expense;
        this.profit = profit;
        this.owner = owner;
        this.level = 1;
        this.upgradeMaterial = upgradeMaterial;
        this.owner.addScore(scoreValue* scoreModifier);
    }

    public void generateResource(int resourcesModifier) {
        if (production != ResourceType.NONE) {
            owner.addResource(production, level * 2 * resourcesModifier);
        }
    }

    public String consumeResources() {
        int totalCost = expense * level;

        // Se o dono conseguir pagar, o fluxo segue feliz
        if (owner.removeResource(costType, totalCost)) {
            return String.format("%s (%s): Manutenção paga (%d %s).",
                    structureType, owner.getName(), totalCost, costType);
        }


        level--;
        if (level > 0) {
            this.expense = 3 + 6 * (level - 1);
            this.profit = 5 + 5 * (level - 1);
            return String.format("%s de %s falhou a manutenção! Degradou-se para Nível %d.",
                    structureType, owner.getName(), level);
        } else {
            return String.format("A estrutura %s de %s foi destruída por falta de pagamento!",
                    structureType, owner.getName());
        }
    }

    /**
     * Melhora a Estrutura se o jogador possuir os recursos necessarios
     * @return true, se foi possivel melhorar, false caso contrario
     */
    public boolean upgradeStructure(int scoreModifier) throws InsufficientResourcesException {
        int cost = level * 5;

        if (owner.removeResource(upgradeMaterial, cost)) {
            level++;
            profit += 5;
            expense += 6;
            owner.addScore(level * 5 * scoreModifier);

            return true;
        } else {
            throw new InsufficientResourcesException();
        }
    }

    @Override
    public String toString() {
        return structureType + level;
    }

    public Player getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.expense = 3 + 6 *( level - 1);
        this.profit = 5 + 5 * ( level - 1);
    }

}