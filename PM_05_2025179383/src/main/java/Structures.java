public abstract class Structures {

    protected final String structureType;
    protected final String production;
    protected String cost;
    protected int expense;
    protected int profit;
    protected int level;
    protected final Player owner;
    protected final String materialToUpgrade;

    public Structures(String structureType,
                      String production,
                      String cost,
                      int expense,
                      int profit,
                      Player owner,
                      String materialToUpgrade) {

        this.structureType = structureType;
        this.production = production;
        this.cost = cost;
        this.expense = expense;
        this.profit = profit;
        this.owner = owner;
        this.level = 1;
        this.materialToUpgrade = materialToUpgrade;
    }

    public Structures(String structureType, String cost, int expense, Player owner, String materialToUpgrade){
        this(structureType,"NONE",cost,expense,0,owner,materialToUpgrade);
    }

    public void generateResource(){
        owner.addResource(production,level*2);
    }

    public boolean consumeResources(){
        if (owner.removeResource(cost,expense * level)){
            System.out.println("Recurso removido com sucesso");
            return true;
        } else {
            System.out.println("Falha, nao ha recurso o suficiente");
            return false;
        }
    }

    public void upgradeStructure(){
        if (owner.removeResource(materialToUpgrade,level * 5)) {
            level += 1;
            profit += 5;
            expense += 6;
        } else {
            System.out.println("Falha");
        }

    }

    public int getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return String.format(structureType + level);
    }

    public Player getOwner() {
        return owner;
    }

    public String getMaterialToUpgrade() {
        return materialToUpgrade;
    }

    public String getCost() {
        return cost;
    }
}
