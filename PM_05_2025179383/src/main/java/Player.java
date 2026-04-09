import java.util.HashMap;
import java.util.Map;

public class Player {

    private final String name;
    private int score;
    private final Map<ResourceType, Integer> inventory;
    private Integer BASE_AP = 10;

    public Player(String name) {
        final Integer BASE_ITENS = 10;

        this.name = name;
        this.score = 0;
        this.inventory = new HashMap<>();
        inventory.put(ResourceType.ACTION_POINTS, BASE_AP);
        inventory.put(ResourceType.WOOD,BASE_ITENS);
        inventory.put(ResourceType.STONE, BASE_ITENS);
        inventory.put(ResourceType.FOOD, BASE_ITENS);

    }

    public String getName() {

        return name;
    }

    public int getActionPoints() {

        return inventory.get(ResourceType.ACTION_POINTS);
    }

    public Map<ResourceType, Integer> getInventory() {

        return Map.copyOf(inventory);
    }

    public int getScore() {
        return score;
    }

    /**
     * Restaura o valor de AP do jogador
     */
    public void resetAC(){
        inventory.put(ResourceType.ACTION_POINTS, BASE_AP   );
    }

    /**
     * Aumenta o valor de AP máximo e adiciona ao inventario
     * @param value valor em que deve ser aumentado
     */
    public void addActionPoints(int value){
        addResource(ResourceType.ACTION_POINTS, value);
        BASE_AP += value;
    }

    public void addScore(int value) {
        score += value;
    }

    public int getResourceQuantity(ResourceType resource){

        if ((inventory.containsKey(resource))) {
            return inventory.get(resource);
        }

        return 0;
    }

    public void addResource(ResourceType resource, int quantity){

        inventory.merge(resource,quantity,Integer::sum);
    }

    public boolean removeResource(ResourceType resource, int quantity) {
        if (!inventory.containsKey(resource)){
            return false;
        } else if (inventory.get(resource) >= quantity){
            inventory.put(resource,getResourceQuantity(resource) - quantity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("Player %s[Score: %d]\n" +
                "Current Inventory: %s", name,score,inventory);
    }
}
