package jogo.engine;

import jogo.exceptions.GameException;
import jogo.exceptions.InsufficientAPException;
import jogo.exceptions.InsufficientResourcesException;
import jogo.exceptions.SpaceAlreadyOccupiedException;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;

import java.util.Random;

public class GameEngine {

    private static final Random random = new Random();

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
            throw new GameException("Não existe nenhuma estrutura nessa posição.");
        }

        int apCost = 10;

        if (player.getActionPoints() < apCost) {
            throw new InsufficientAPException();
        }

        structure.upgradeStructure(scoreModifier);

        player.removeResource(ResourceType.ACTION_POINTS, apCost);
    }

    public static String searchResources(Player player, ResourceType type) throws GameException {
        int apCost = 4;

        if (player.getActionPoints() < apCost) {
            throw new InsufficientAPException();
        }

        if (type == ResourceType.NONE || type == ResourceType.ACTION_POINTS) {
            throw new GameException("Tipo de recurso inválido.");
        }

        player.removeResource(ResourceType.ACTION_POINTS, apCost);

        int gathered = random.nextInt(3) + 2;

        player.addResource(type, gathered);

        return "Encontraste " + gathered + " unidades de " + type + ".";
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
}