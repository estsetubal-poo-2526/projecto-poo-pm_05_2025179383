public abstract class Structures {
    protected final String structureType;
    protected final ResourceType production;
    protected final ResourceType costType; // Recurso necessário para manutenção
    protected final ResourceType upgradeMaterial;

    protected int expense;
    protected int profit;
    protected int level;
    protected final Player owner;

    public Structures(String structureType,
                      ResourceType production,
                      ResourceType costType,
                      int expense,
                      int profit,
                      Player owner,
                      ResourceType upgradeMaterial,
                      int scoreValue) {

        this.structureType = structureType;
        this.production = production;
        this.costType = costType;
        this.expense = expense;
        this.profit = profit;
        this.owner = owner;
        this.level = 1;
        this.upgradeMaterial = upgradeMaterial;
        this.owner.addScore(scoreValue*Events.getScoreModifier());
    }

    public void generateResource() {
        if (production != ResourceType.NONE) {
            owner.addResource(production, level * 2 * Events.getResourceModifier());
        }
    }

    /**
     * Consome os recursos do inventário do jogador. Se o mesmo não possuir recursos suficientes, o level da estrutura
     * diminui em 1. Se o nivel for 1, a estrutura é destruida
     * @return true se a estrutura não foi destruida, false se foi destruida
     */
    public boolean consumeResources() {
        if (owner.removeResource(costType, expense * level)) {
            System.out.println(structureType + " de " + owner.getName() + ": Manutenção paga.");
            return true;

        } else {
            System.out.println( structureType + " de " + owner.getName() + " falhou a manutenção!");
            level--;

            if (level > 0) {
                System.out.println("A estrutura degradou-se para o Nível " + level);

                expense -= 6;
                profit -= 5;
                return true;
            } else {
                System.out.println("A estrutura foi destruida");
                return false;
            }

        }
    }

    /**
     * Melhora a Estrutura se o jogador possuir os recursos necessarios
     * @return true, se foi possivel melhorar, false caso contrario
     */
    public boolean upgradeStructure() {
        int cost = level * 5;

        if (owner.removeResource(upgradeMaterial, cost)) {
            level++;
            profit += 5;
            expense += 6;
            owner.addScore(level * 5 * Events.getScoreModifier());
            System.out.println(structureType + " melhorada para nível " + level);

            return true;
        } else {
            System.out.println("Não tens " + upgradeMaterial + " suficiente para o upgrade.");
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format(structureType + level);
    }

    public Player getOwner() {
        return owner;
    }

}