package jogo.models;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private static final int INITIAL_ITEMS = 10;
    private static final int INITIAL_ACTION_POINTS = 10;

    private final String name;
    private int score;
    private int baseActionPoints;
    private final Map<ResourceType, Integer> inventory;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.baseActionPoints = INITIAL_ACTION_POINTS;
        this.inventory = new HashMap<>();

        inventory.put(ResourceType.ACTION_POINTS, baseActionPoints);
        inventory.put(ResourceType.WOOD, INITIAL_ITEMS);
        inventory.put(ResourceType.STONE, INITIAL_ITEMS);
        inventory.put(ResourceType.FOOD, INITIAL_ITEMS);
    }

    public Player(String name, int score, int wood, int stone, int food, int actionPoints) {
        this.name = name;
        this.score = score;
        this.baseActionPoints = actionPoints;
        this.inventory = new HashMap<>();

        inventory.put(ResourceType.WOOD, wood);
        inventory.put(ResourceType.STONE, stone);
        inventory.put(ResourceType.FOOD, food);
        inventory.put(ResourceType.ACTION_POINTS, actionPoints);
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

    public int getBaseActionPoints() {
        return baseActionPoints;
    }

    public void resetAC() {
        inventory.put(ResourceType.ACTION_POINTS, baseActionPoints);
    }

    public void addActionPoints(int value) {
        addResource(ResourceType.ACTION_POINTS, value);
        baseActionPoints += value;
    }

    public void addScore(int value) {
        score += value;
    }

    public int getResourceQuantity(ResourceType resource) {
        return inventory.getOrDefault(resource, 0);
    }

    public void addResource(ResourceType resource, int quantity) {
        inventory.merge(resource, quantity, Integer::sum);
    }

    public boolean removeResource(ResourceType resource, int quantity) {
        if (getResourceQuantity(resource) < quantity) {
            return false;
        }

        inventory.put(resource, getResourceQuantity(resource) - quantity);
        return true;
    }

    public void clearInventory() {
        for (ResourceType type : ResourceType.values()) {
            inventory.put(type, 0);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Player %s [Score: %d]%nCurrent Inventory: %s",
                name,
                score,
                inventory
        );
    }
}