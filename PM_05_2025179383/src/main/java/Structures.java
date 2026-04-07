public abstract class Structures {
    protected final String structureType;
    protected final ResourceType production;
    protected final ResourceType costType; // Recurso necessário para manutenção
    protected final ResourceType upgradeMaterial;

    protected int expense;
    protected int profit;
    protected int level;
    protected final Player owner;

    public Structures(String structureType, ResourceType production, ResourceType costType,
                      int expense, int profit, Player owner, ResourceType upgradeMaterial) {
        this.structureType = structureType;
        this.production = production;
        this.costType = costType;
        this.expense = expense;
        this.profit = profit;
        this.owner = owner;
        this.level = 1;
        this.upgradeMaterial = upgradeMaterial;
    }

    public void generateResource() {
        if (production != ResourceType.NONE) {
            owner.addResource(production, level * 2);
        }
    }

    public void consumeResources() {
        if (owner.removeResource(costType, expense * level)) {
            System.out.println(structureType + ": Manutenção paga.");
        } else {
            System.out.println(structureType + ": Falha na manutenção! Recursos insuficientes.");
        }
    }

    public void upgradeStructure() {
        int cost = level * 5;
        if (owner.removeResource(upgradeMaterial, cost)) {
            level++;
            profit += 5;
            expense += 6;
            System.out.println(structureType + " melhorada para nível " + level);
        } else {
            System.out.println("Não tens " + upgradeMaterial + " suficiente para o upgrade.");
        }
    }

    @Override
    public String toString() {
        return String.format(structureType + level);
    }

    public Player getOwner() { return owner; }
}