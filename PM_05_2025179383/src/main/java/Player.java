import java.util.HashMap;
import java.util.Map;

public class Player {

    private final String name;
    private int score;
    private Map<String, Integer> inventory;
    private Integer max_AP = 10;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.inventory = new HashMap<>();
        inventory.put("actionPoint", max_AP);
    }

    public String getName() {
        return name;
    }

    public int getActionPoints() {
        return inventory.get("actionPoint");
    }

    public Map<String, Integer> getInventory() {
        return Map.copyOf(inventory);
    }

    public int getScore() {
        return score;
    }

    public void resetAC(){
        inventory.put("actionPoint", max_AP);
    }

    public void addActionPoints(int value){
        addResource("actionPoint", getResourceQuantity("actionPoint") + value);
    }

    public void addScore(int value) {
        score += value;
    }

    public int getResourceQuantity(String resource){

        if ((inventory.containsKey(resource))) {
            return inventory.get(resource);
        }

        return 0;
    }

    public void addResource(String resource, int quantity){

        if (inventory.containsKey(resource)){
            inventory.put(resource,getResourceQuantity(resource) + quantity);
        } else {
            inventory.put(resource, quantity);

        }
    }

    public boolean removeResource(String resource, int quantity) {
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
