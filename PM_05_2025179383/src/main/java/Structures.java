public class Structures {

    private final String structureType;
    private final String production;
    private String cost;
    private int expense;
    private int profit;
    private int level;
    private final Player owner;
    private final String materialToUpgrade;

    public Structures(String structureType, String production, String cost, int expense, int profit, Player owner, String materialToUpgrade) {
        this.structureType = structureType;
        this.production = production;
        this.cost = cost;
        this.expense = expense;
        this.profit = profit;
        this.owner = owner;
        this.level = 1;
        this.materialToUpgrade = materialToUpgrade;
    }

    public void generateResource(){
        owner.addResource(production,level*2);
    }

    public void consumeResources(){
        if (owner.removeResource(cost,expense * level)){
            System.out.println("Recurso removido com sucesso");
        } else {
            System.out.println("Falha, nao ha recurso o suficiente");
        }
    }

    public void upgradeStructure(){
        if (owner.removeResource(materialToUpgrade,level * 3)) {
            level += 1;
            profit += 5;
        } else {
            System.out.println("Falha");
        }

    }

    @Override
    public String toString() {
        return String.format(structureType);
    }

    public Player getOwner() {
        return owner;
    }
}
