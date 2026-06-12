package jogo.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player within the game session.
 * Manages the player's unique identity, score tracking, Action Points (AP),
 * and an inventory of various material resources.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class Player {

    private static final int INITIAL_ITEMS = 10;
    private static final int INITIAL_ACTION_POINTS = 10;
    private static final int INITIAL_SCORE = 0;

    private final String name;
    private int score;
    private int baseActionPoints;
    private final Map<ResourceType, Integer> inventory;

    /**
     * Constructs a new Player with default starting resources, score, and Action Points.
     *
     * @param name The unique name of the player.
     * @throws IllegalArgumentException If the provided name is empty or null.
     */
    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome Inválido");
        }

        this.name = name;
        this.score = INITIAL_SCORE;
        this.baseActionPoints = INITIAL_ACTION_POINTS;
        this.inventory = new HashMap<>();

        inventory.put(ResourceType.ACTION_POINTS, baseActionPoints);
        inventory.put(ResourceType.WOOD, INITIAL_ITEMS);
        inventory.put(ResourceType.STONE, INITIAL_ITEMS);
        inventory.put(ResourceType.FOOD, INITIAL_ITEMS);
    }

    /**
     * Constructs a Player instance with explicit values for all attributes.
     * Primarily utilized by the data persistence layer when restoring a saved game state.
     *
     * @param name         The name of the player.
     * @param score        The player's accumulated score.
     * @param wood         The initial amount of wood resource.
     * @param stone        The initial amount of stone resource.
     * @param food         The initial amount of food resource.
     * @param actionPoints The current available Action Points.
     * @throws IllegalArgumentException If the provided name is empty or null.
     */
    @Deprecated
    public Player(String name, int score, int wood, int stone, int food, int actionPoints) {
        this(name, score, wood, stone, food, actionPoints, INITIAL_ACTION_POINTS);
    }

    /**
     * Advanced reconstruction constructor for the data persistence layer.
     * Separates volatile current turn AP from the permanent maximum baseline AP capacity.
     *
     * @param name         The name of the player.
     * @param score        The player's accumulated score.
     * @param wood         The current amount of wood resource.
     * @param stone        The current amount of stone resource.
     * @param food         The current amount of food resource.
     * @param currentAp    The current active Action Points remaining in the turn.
     * @param maxBaseAp    The maximum baseline Action Points capacity for turn initialization.
     * @throws IllegalArgumentException If the provided name is empty or null.
     */
    public Player(String name, int score, int wood, int stone, int food, int currentAp, int maxBaseAp) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome Inválido");
        }

        this.name = name;
        this.score = score;
        this.baseActionPoints = maxBaseAp;
        this.inventory = new HashMap<>();

        inventory.put(ResourceType.WOOD, wood);
        inventory.put(ResourceType.STONE, stone);
        inventory.put(ResourceType.FOOD, food);
        inventory.put(ResourceType.ACTION_POINTS, currentAp);
    }

    public String getName() {
        return name;
    }

    public int getActionPoints() {
        return inventory.getOrDefault(ResourceType.ACTION_POINTS, 0);
    }

    /**
     * Returns an unmodifiable view of the player's current resource inventory.
     *
     * @return A read-only Map containing the resource types and their quantities.
     */
    public Map<ResourceType, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }

    public int getScore() {
        return score;
    }

    public int getBaseActionPoints() {
        return baseActionPoints;
    }

    /**
     * Resets the player's current Action Points in the inventory back to their baseline value.
     * Typically triggered at the start of a new game turn.
     */
    public void resetAP() {
        inventory.put(ResourceType.ACTION_POINTS, baseActionPoints);
    }

    /**
     * Permanently increases the player's maximum baseline Action Points and updates
     * the current available pool.
     *
     * @param value The amount of baseline points to add.
     */
    public void addBaseActionPoints(int value) {
        addResource(ResourceType.ACTION_POINTS, value);
        baseActionPoints += value;
    }

    /**
     * Permanently decreases the player's maximum baseline Action Points and updates
     * the current available pool.
     *
     * @param value The amount of baseline points to decrease.
     */

    public void removeBaseActionPoints(int value) {
        removeResource(ResourceType.ACTION_POINTS,value);
        baseActionPoints -= value;
    }

    /**
     * Increments the player's total victory score.
     *
     * @param value The points to be added to the score.
     */
    public void addScore(int value) {
        score += value;
    }

    /**
     * Retrieves the current quantity of a specific resource stored in the inventory.
     *
     * @param resource The ResourceType to query.
     * @return The integer amount of the resource, or 0 if it is not present.
     */
    public int getResourceQuantity(ResourceType resource) {
        return inventory.getOrDefault(resource, 0);
    }

    /**
     * Adds a specified quantity of a resource to the player's inventory.
     *
     * @param resource The ResourceType to accumulate.
     * @param quantity The positive amount to add.
     */
    public void addResource(ResourceType resource, int quantity) {
        inventory.merge(resource, quantity, Integer::sum);
    }

    /**
     * Attempts to deduct a specified quantity of a resource from the inventory.
     * Fails if the player possesses insufficient quantities.
     *
     * @param resource The ResourceType to consume.
     * @param quantity The amount required for the operation.
     * @return true if the consumption succeeded; false if resources were insufficient.
     */
    public boolean removeResource(ResourceType resource, int quantity) {
        if (getResourceQuantity(resource) < quantity) {
            return false;
        }

        inventory.put(resource, getResourceQuantity(resource) - quantity);
        return true;
    }

    /**
     * Sets the quantity of all valid in-game resources to zero.
     */
    public void clearInventory() {
        for (ResourceType type : ResourceType.values()) {
            if (type != ResourceType.NONE) {
                inventory.put(type, 0);
            }
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

    public static int getInitialActionPoints() {
        return INITIAL_ACTION_POINTS;
    }

    public static int getInitialItems() {
        return INITIAL_ITEMS;
    }

    public static int getInitialScore() {
        return INITIAL_SCORE;
    }
}