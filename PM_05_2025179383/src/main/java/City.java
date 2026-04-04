public class City extends Structures{

    private static final int apNeeded = 3;

    public City(Player owner) {
        super("Cidade","FOOD", 20, owner, "STONE");
    }

    public static int getApNeeded() {
        return apNeeded;
    }


    @Override
    public void upgradeStructure(){
        if (owner.removeResource(this.materialToUpgrade,level * 5)) {
            level += 1;
            profit += 5;
            expense += 6;
            owner.addActionPoints(2*level);
        } else {
            System.out.println("Falha");
        }
    }

    @Override
    public String getMaterialToUpgrade() {
        return super.getMaterialToUpgrade();
    }

    @Override
    public String getCost() {
        return super.getCost();
    }
}
