package jogo.models.Structures;

import jogo.exceptions.InsufficientResourcesException;
import jogo.models.Player;
import jogo.models.ResourceType;

/**
 * Abstract baseline class for all architectural structures within the game world.
 * Defines the core state machine for lifecycle management, including daily resource
 * consumption, maintenance degradation, production yields, and evolutionary upgrades.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public abstract class Structures {

    protected final StructuresType STRUCTURE_TYPE;
    protected final ResourceType PRODUCTION;
    protected final ResourceType MAINTENANCE_MATERIAL;
    protected final ResourceType UPGRADE_MATERIAL;

    protected int expense;
    protected int profit;
    protected int level;

    protected final Player owner;

    /**
     * Constructs the structural foundations, binds it to an owner, and injects
     * the initial architectural score value into the player's records.
     *
     * @param structureType       The specific type categorizing this structure.
     * @param production          The type of resource this building yields.
     * @param maintenanceMaterial The resource required to sustain daily operational costs.
     * @param expense             The initial maintenance expense value.
     * @param profit              The initial baseline resource production profit value.
     * @param owner               The Player instances who holds title to this building.
     * @param upgradeMaterial     The resource required to process evolutionary upgrades.
     * @param level               The starting developmental level.
     * @param scoreValue          The baseline victory points value assigned by this structure.
     * @param scoreModifier       The active multiplier applied to the baseline score.
     */
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

    /**
     * Triggers the daily resource acquisition routine, forcing concrete implementations
     * to resolve and append materials directly to the owning player's inventory.
     *
     * @param resourcesModifier The current active global production multiplier.
     */
    public abstract void generateResource(int resourcesModifier);

    /**
     * Deducts the current maintenance fees from the owner's inventory.
     * If the asset balance is deficient, the structure enters a degradation cycle,
     * reducing its developmental level, or faces complete destruction if the level hits zero.
     *
     * @return A formatted status string detailing the success, degradation, or destruction outcome.
     */
    public String consumeResources() {
        // CORRIGIDO: Removida a multiplicação absurda por level.
        // O atributo expense já acompanha o crescimento do nível da estrutura.
        int totalCost = expense;

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

    public StructuresType getStructureType() {
        return STRUCTURE_TYPE;
    }

    @Override
    public String toString() {
        return STRUCTURE_TYPE + " nível " + level;
    }
}