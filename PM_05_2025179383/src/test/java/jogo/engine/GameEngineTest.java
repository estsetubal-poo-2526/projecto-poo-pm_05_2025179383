package jogo.engine;

import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Forest;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private WorldMap map;
    private Player player;

    @BeforeEach
    void setUp(){
         map = new WorldMap();
         player = new Player("PlayerTest");
    }

    @Test
    void testSuccessCreateStructure() throws CoordinatesOutOfBoundsException {
        makePlayerRich(player);

        assertDoesNotThrow(() -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 2, 3, 1);
        }, "A estrutura devia ter sido criada com sucesso.");

        assertFalse(map.isNotOccupied(2,3), "O Mapa devia estar Ocupado na coordenada [2,3].");

        assertEquals(110 - Forest.getApNeeded(), player.getActionPoints(), "Não foi retirada a quantidade correta de AP");

        int constructionCost = CreateStructure.getMaterialCost(StructuresType.FOREST);
        assertEquals(110 - constructionCost,player.getResourceQuantity(ResourceType.STONE), "Não foi retirada a quantidade correta de Recurso");
    }

    @Test
    void testCreateStructureThrowsCoordinatesOutOfBoundException(){
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.createStructure(map,player,StructuresType.FOREST, -1,-1,1);
        },"Devia Ter Lançado a exceção CoordinatesOutOfBounds para indices negativos");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.createStructure(map, player, StructuresType.FOREST, 7, 3, 1);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou superior ao tamanho.");
    }

    @Test
    void testCreateStructureThrowsSpaceAlreadyOccupiedException() throws GameException {
        makePlayerRich(player);
            map.addStructure(new Forest(player, 1), 2, 3);

        assertThrows(SpaceAlreadyOccupiedException.class, () -> {
            GameEngine.createStructure(map,player,StructuresType.FOREST,2,3,1);
        }, "Devia Ter Lançado SpaceAlreadyOccupiedException porque ja existia estruturas nas coordenadas [2,3]");

    }

    @Test
    void testCreateStructureThrowsInsufficientResourcesException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 100);

        assertThrows(InsufficientResourcesException.class, () -> {
            GameEngine.createStructure(map,player, StructuresType.CITY,0,0,1);
        }, "Devia Ter Lançado InsufficientResourcesException pois o jogador nao tem recursos suficientes");

    }

    @Test
    void testCreateStructureThrowsInsufficientAPException() {

        player.clearInventory();
        player.addResource(ResourceType.STONE,100);

        assertThrows(InsufficientAPException.class, () -> {
            GameEngine.createStructure(map,player, StructuresType.FOREST,0,0,1);
        }, "Devia Ter Lançado InsufficientAPException pois o jogador nao tem AP suficientes");

    }

    @Test
    void testSuccessUpgradeStructure() throws GameException {

        makePlayerRich(player);
        GameEngine.createStructure(map, player, StructuresType.CITY, 0, 0, 1);

        Structures structure = map.getStructure(0, 0);
        int resorcesBefone = player.getResourceQuantity(structure.getUpgradeMaterial());
        int apBefore = player.getResourceQuantity(ResourceType.ACTION_POINTS);

        int expectedResourcesConsuption = structure.getUpgradeCost();
        int expectedAPCost = structure.getAPCostToUpgrade();
        int apGanhoNoUpgrade = 6;

        assertDoesNotThrow(() -> {
            GameEngine.upgradeStructure(map, player, 0, 0, 1);
        }, "Estrutura devia ter sido melhorada com sucesso em [0,0]");

        assertEquals(resorcesBefone - expectedResourcesConsuption, player.getResourceQuantity(ResourceType.STONE),
                "O custo em pedra não foi debitado corretamente.");

        assertEquals(apBefore + apGanhoNoUpgrade - expectedAPCost, player.getResourceQuantity(ResourceType.ACTION_POINTS),
                "A lógica de AP (ganho vs custo) falhou.");

        assertEquals(2, structure.getLevel(), "A estrutura devia estar a nível 2.");
    }

    @Test
    void testUpgradeStructureThrowsCoordinatesOutOfBoundException(){
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.upgradeStructure(map,player, -1,-1,1);
        },"Devia Ter Lançado a exceção CoordinatesOutOfBounds para indices negativos");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.upgradeStructure(map, player, 7, 3, 1);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou superior ao tamanho.");
    }

    @Test
    void testUpgradeStructureThrowInsufficientAPException() throws GameException {

        makePlayerRich(player);
        GameEngine.createStructure(map,player,StructuresType.FOREST,0,0,1);

        player.clearInventory();
        player.addResource(ResourceType.STONE,100);

        assertThrows(InsufficientAPException.class,() -> {
            GameEngine.upgradeStructure(map,player,0,0,1);
        }, "Devia ter lançado a InsufficientAPException");

    }

    @Test
    void testUpgradeStructureThrowInsufficientResourcesException() throws GameException {

        makePlayerRich(player);
        GameEngine.createStructure(map,player,StructuresType.FOREST,0,0,1);

        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS,100);

        assertThrows(InsufficientResourcesException.class,() -> {
            GameEngine.upgradeStructure(map,player,0,0,1);
        }, "Devia ter lançado a InsufficientResourcesException");

    }

    @Test
    void testUpgradeStructureThrowsStructureDontExistException() throws GameException {

        assertThrows(StructureDontExistException.class,() -> {
            GameEngine.upgradeStructure(map,player,0,0,1);
        }, "Devia ter lançado a StructureDontExistException");

    }

    @Test
    void testUpgradeStructureThrowsUnauthorizedActionException() throws GameException {

        makePlayerRich(player);
        GameEngine.createStructure(map,player,StructuresType.FOREST,0,0,1);

        Player player2 = new Player("PlayerTest2");
        makePlayerRich(player2);

        assertThrows(UnauthorizedActionException.class,() -> {
            GameEngine.upgradeStructure(map,player2,0,0,1);
        }, "Devia ter lançado a UnauthorizedActionException");
    }

    @Test
    void testUpgradeStructureIncreaseStats() throws GameException {

        makePlayerRich(player);

        GameEngine.createStructure(map, player, StructuresType.FOREST, 0, 0, 1);

        Structures structure = map.getStructure(0, 0);

        int beforeUpgradeExpense = structure.getExpense();

        GameEngine.upgradeStructure(map,player,0,0,1);

        int expenseEsperada = beforeUpgradeExpense + Forest.getExpenseByLevel();
        assertEquals(expenseEsperada, structure.getExpense(),
                "A Melhoria não alterou a despesa corretamente.");

        assertEquals(Forest.getPROFIT() + Forest.getProfitByLevel(), structure.getProfit(),
                "A Melhoria não alterou o Lucro Corretamente");
    }

    @Test
    void testSearchResourcesSuccess() throws GameException {
        makePlayerRich(player);

        int apBefore = player.getResourceQuantity(ResourceType.ACTION_POINTS);
        int woodBefore = player.getResourceQuantity(ResourceType.WOOD);


        Integer result = assertDoesNotThrow(() -> {
            return GameEngine.searchResources(player, ResourceType.WOOD);
        }, "A busca por recursos devia ter funcionado.");

        assertNotNull(result);

        assertEquals(apBefore - 4, player.getResourceQuantity(ResourceType.ACTION_POINTS),
                "Não foi retirado o AP correto da procura");

        int woodAfter = player.getResourceQuantity(ResourceType.WOOD);
        int quantityObtained = woodAfter - woodBefore;


            assertTrue(quantityObtained >= 2 && quantityObtained <= 4,
                    "A quantidade de recursos gerada (" + quantityObtained + ") está fora do intervalo [2, 4].");
    }

    @Test
    void testSearchResourcesSuccessRepeatedly() throws GameException {

        for (int i = 0; i < 100; i++) {
            player.addResource(ResourceType.ACTION_POINTS,4);

            int woodBefore = player.getResourceQuantity(ResourceType.WOOD);
            GameEngine.searchResources(player, ResourceType.WOOD);
            int quantityObtained = player.getResourceQuantity(ResourceType.WOOD) - woodBefore;

            assertTrue(quantityObtained >= 2 && quantityObtained <= 4,
                    "Na iteração " + i + ", a quantidade (" + quantityObtained + ") saiu do intervalo [2, 4].");
        }
    }

    @Test
    void testSearchResourcesThrowsInsufficientAPException() {
        player.clearInventory();
        player.addResource(ResourceType.ACTION_POINTS, 3);

        assertThrows(InsufficientAPException.class, () -> {
            GameEngine.searchResources(player, ResourceType.WOOD);
        }, "Devia ter lançado InsufficientAPException");
    }

    @Test
    void testSearchResourcesThrowsGameExceptionForInvalidType() {
        makePlayerRich(player);

        GameException exNone = assertThrows(GameException.class, () -> {
            GameEngine.searchResources(player, ResourceType.NONE);
        }, "Devia ter lançado GameException para ResourceType.NONE");

        assertEquals("Tipo de recurso inválido.", exNone.getMessage());

        GameException exAp = assertThrows(GameException.class, () -> {
            GameEngine.searchResources(player, ResourceType.ACTION_POINTS);
        }, "Devia ter lançado GameException para ResourceType.ACTION_POINTS");

        assertEquals("Tipo de recurso inválido.", exAp.getMessage());
    }

    @Test
    void testGetStructureInfoSuccess() throws GameException {
        makePlayerRich(player);

        GameEngine.createStructure(map, player, StructuresType.FOREST, 1, 2, 1);

        Structures estrutura = assertDoesNotThrow(() -> {
            return GameEngine.getStructureInfo(map, 1, 2);
        }, "O método devia ter devolvido a estrutura sem lançar exceções.");

        assertNotNull(estrutura, "A estrutura devolvida não devia ser nula.");
        assertEquals(StructuresType.FOREST.toString(), estrutura.getName(),
                "O tipo da estrutura devolvida está errado.");

        assertEquals(player, estrutura.getOwner(), "O dono da estrutura devia ser o player.");
    }

    @Test
    void testGetStructureInfoThrowsCoordinatesOutOfBoundException(){
        makePlayerRich(player);

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.getStructureInfo(map, -1,1);
        },"Devia Ter Lançado a exceção CoordinatesOutOfBounds para indice negativo");

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            GameEngine.getStructureInfo(map, 7, 3);
        }, "Devia ter lançado CoordinatesOutOfBoundsException para índice igual ou maior ao tamanho.");
    }

    @Test
    void testGetStructureInfoThrowsExceptionWhenEmpty() throws CoordinatesOutOfBoundsException {
        assertTrue(map.isNotOccupied(4, 4), "A coordenada devia estar vazia.");

        GameException exception = assertThrows(GameException.class, () -> {
            GameEngine.getStructureInfo(map, 4, 4);
        }, "Devia ter lançado GameException porque a coordenada está vazia.");

        assertEquals("Não existe nenhuma estrutura nessa posição.", exception.getMessage(),
                "A mensagem de erro da exceção está errada.");
    }

    private static void makePlayerRich(Player player){
        player.addResource(ResourceType.WOOD,100);
        player.addResource(ResourceType.STONE,100);
        player.addResource(ResourceType.ACTION_POINTS,100);
        player.addResource(ResourceType.FOOD,100);
    }
}