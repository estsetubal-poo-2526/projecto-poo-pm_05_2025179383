package jogo.engine;

import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;

import java.util.Random;

public class GameEngine {

    private static final Random random = new Random();
    private static final int BASE_SEARCH_VALUE = 3;
    private static final int BONUS_SEARCH_VALUE = 2;
    private static final int AP_COST_SEARCH = 4;

    private GameEngine() {
    }

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

    public static void upgradeStructure(
            WorldMap map,
            Player player,
            int x,
            int y,
            int scoreModifier
    ) throws GameException {

        map.canInteract(x, y, player);

        Structures structure = map.getStructure(x, y);

        if (structure == null) {
            throw new StructureDontExistException();
        }

        int apCost = structure.getAPCostToUpgrade();

        if (player.getActionPoints() < apCost) {
            throw new InsufficientAPException();
        }

        structure.upgradeStructure(scoreModifier);

        player.removeResource(ResourceType.ACTION_POINTS, apCost);
    }

    public static int searchResources(Player player, ResourceType type) throws GameException {

        if (player.getActionPoints() < AP_COST_SEARCH) {
            throw new InsufficientAPException();
        }

        if (type == ResourceType.NONE || type == ResourceType.ACTION_POINTS) {
            throw new GameException("Tipo de recurso inválido.");
        }

        player.removeResource(ResourceType.ACTION_POINTS, AP_COST_SEARCH);

        int gathered = random.nextInt(BASE_SEARCH_VALUE) + BONUS_SEARCH_VALUE   ;

        player.addResource(type, gathered);

        return gathered;
    }

    public static Structures getStructureInfo(
            WorldMap map,
            int x,
            int y
    ) throws GameException {

        Structures structure = map.getStructure(x, y);

        if (structure == null) {
            throw new GameException("Não existe nenhuma estrutura nessa posição.");
        }

        return structure;
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