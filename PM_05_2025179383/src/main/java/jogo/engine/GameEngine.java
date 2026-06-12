package jogo.engine;

import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Utility class responsible for managing core game logic and validating
 * action requests performed by the players.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class GameEngine {

    private static final Random random = new Random();
    private static final int BASE_SEARCH_VALUE = 3;
    private static final int BONUS_SEARCH_VALUE = 2;
    private static final int AP_COST_SEARCH = 4;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GameEngine() {
    }

    /**
     * Constructs and places a structure on the map at the specified coordinates.
     * Validates space availability, player Action Points, and required resources before construction.
     *
     * @param map The WorldMap where the structure will be placed.
     * @param player The Player who will own the new structure.
     * @param structureType The type of structure to be built.
     * @param x The X-coordinate on the map grid.
     * @param y The Y-coordinate on the map grid.
     * @param scoreModifier The current score multiplier affecting the points granted upon construction.
     * @throws SpaceAlreadyOccupiedException If the target coordinates are already occupied by another structure.
     * @throws InsufficientAPException If the player does not have enough Action Points (AP) to build the structure.
     * @throws InsufficientResourcesException If the player lacks the required amount of the specific resource material.
     * @throws GameException If any generic game logic violation occurs during execution.
     */
    public static void createStructure(
            WorldMap map,
            Player player,
            StructuresType structureType,
            int x,
            int y,
            int scoreModifier
    ) throws GameException {

        if (!map.isNotOccupied(x, y)) {
            throw new SpaceAlreadyOccupiedException();
        }

        int apCost = CreateStructure.getApCost(structureType);
        int materialCost = CreateStructure.getMaterialCost(structureType);
        ResourceType materialType = CreateStructure.getMaterialType(structureType);

        if (player.getActionPoints() < apCost) {
            throw new InsufficientAPException();
        }

        if (player.getResourceQuantity(materialType) < materialCost) {
            throw new InsufficientResourcesException();
        }

        Structures newStructure = CreateStructure.create(
                structureType,
                player,
                scoreModifier
        );

        map.addStructure(newStructure, x, y);

        player.removeResource(ResourceType.ACTION_POINTS, apCost);
        player.removeResource(materialType, materialCost);
    }

    /**
     * Upgrades an existing structure on the map at the specified coordinates.
     * Validates interaction rights and checks if the player has enough Action Points (AP)
     * before performing the upgrade.
     *
     * @param map The current WorldMap instance.
     * @param player The Player attempting to upgrade the structure.
     * @param x The X-coordinate of the target structure.
     * @param y The Y-coordinate of the target structure.
     * @param scoreModifier The current score multiplier affecting the points granted upon upgrading.
     * @throws InsufficientAPException If the player lacks the required Action Points (AP) to upgrade.
     * @throws GameException If the structure does not exist, belongs to the opponent, or any other validation fails.
     */
    public static void upgradeStructure(
            WorldMap map,
            Player player,
            int x,
            int y,
            int scoreModifier
    ) throws GameException {

        map.canInteract(x, y, player);

        Structures structure = map.getStructure(x, y);

        int apCost = structure.getAPCostToUpgrade();

        if (player.getActionPoints() < apCost) {
            throw new InsufficientAPException();
        }

        structure.upgradeStructure(scoreModifier);

        player.removeResource(ResourceType.ACTION_POINTS, apCost);
    }

    /**
     * Prevents player soft-locking by allowing manual resource gathering without requiring structures.
     * Consumes a fixed amount of Action Points (AP) to generate a random quantity of a specific resource.
     *
     * @param player The Player performing the search action.
     * @param type The ResourceType to be gathered.
     * @return The total quantity of resources successfully gathered.
     * @throws InsufficientAPException If the player lacks the required Action Points (AP) to perform the search.
     * @throws GameException If the requested resource type is invalid or cannot be gathered manually.
     */
    public static int searchResources(Player player, ResourceType type) throws GameException {

        if (player.getActionPoints() < AP_COST_SEARCH) {
            throw new InsufficientAPException();
        }

        if (type == ResourceType.NONE || type == ResourceType.ACTION_POINTS) {
            throw new GameException("Tipo de recurso inválido.");
        }

        player.removeResource(ResourceType.ACTION_POINTS, AP_COST_SEARCH);

        int gathered = random.nextInt(BASE_SEARCH_VALUE) + BONUS_SEARCH_VALUE;

        player.addResource(type, gathered);

        return gathered;
    }

    /**
     * Retrieves the structure information located at the specified map coordinates.
     *
     * @param map The current WorldMap instance.
     * @param x The X-coordinate on the map grid.
     * @param y The Y-coordinate on the map grid.
     * @return The Structures object found at the given position.
     * @throws StructureDontExistException If no structure exists at the specified coordinates.
     * @throws GameException If any generic game logic violation occurs during execution.
     */
    public static Structures getStructureInfo(
            WorldMap map,
            int x,
            int y
    ) throws GameException {

        Structures structure = map.getStructure(x, y);

        if (structure == null) {
            throw new StructureDontExistException();
        }

        return structure;
    }

    /**
     * Calculates the resources that will be consumed by a specific player's structures
     * at the end of the day.
     *
     * @param map The current WorldMap instance.
     * @param player The player whose structures will be checked.
     * @return A map containing the total cost of WOOD, STONE and FOOD.
     */
    public static Map<ResourceType, Integer> calculateEndDayCosts(WorldMap map, Player player) {
        Map<ResourceType, Integer> costs = new EnumMap<>(ResourceType.class);

        costs.put(ResourceType.WOOD, 0);
        costs.put(ResourceType.STONE, 0);
        costs.put(ResourceType.FOOD, 0);

        int columns = map.getCOLUMN_SIZE();
        int rows = map.getLINE_SIZE();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                try {
                    Structures structure = map.getStructure(x, y);

                    if (structure == null) {
                        continue;
                    }

                    if (structure.getOwner() == null || player == null) {
                        continue;
                    }

                    if (!structure.getOwner().getName().equals(player.getName())) {
                        continue;
                    }

                    ResourceType maintenanceMaterial = structure.getCostType();
                    int expense = structure.getExpense();

                    switch (maintenanceMaterial) {
                        case WOOD:
                            costs.put(ResourceType.WOOD, costs.get(ResourceType.WOOD) + expense);
                            break;

                        case STONE:
                            costs.put(ResourceType.STONE, costs.get(ResourceType.STONE) + expense);
                            break;

                        case FOOD:
                            costs.put(ResourceType.FOOD, costs.get(ResourceType.FOOD) + expense);
                            break;

                        default:
                            break;
                    }

                } catch (Exception e) {
                    System.out.println("Erro ao calcular custos em " + x + "," + y + ": " + e.getMessage());
                }
            }
        }

        return costs;
    }

    public static int getApCostSearch() {
        return AP_COST_SEARCH;
    }

    public static int getBaseSearchValue() {
        return BASE_SEARCH_VALUE;
    }

    public static int getBonusSearchValue() {
        return BONUS_SEARCH_VALUE;
    }
}