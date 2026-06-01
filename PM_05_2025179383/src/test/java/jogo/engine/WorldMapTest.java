package jogo.engine;


import jogo.exceptions.*;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldMapTest {

    private WorldMap map;
    private Player player1;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        map = new WorldMap();
    }

    @Test
    void addStructuresWithoutThrows() throws GameException {

        assertDoesNotThrow(() -> {
            map.addStructure(new Forest(player1,1),0,0);
        }, "Não deveria lançar nenhuma exceção");

        assertEquals(Forest.class,map.getStructure(0,0).getClass(),"Deveria ser da Classe FOREST");
    }

    @Test
    void addStructuresThrowingSpaceAlreadyOccupiedException() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);

        assertThrows(SpaceAlreadyOccupiedException.class, () -> {
            map.addStructure(new Mine(new Player("Player2"),1),0,0);
        }, "Deveria Ter lançado SpaceAlreadyOccupiedException");

    }

    @Test
    void addStructuresOutsideTheMap(){

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.addStructure(new Forest(player1,1),-1,8);
        },"Deveria ter Lançado CoordinatesOutOfBoundsException");

    }

    @Test
    void getStructuresInsideTheMap() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);

        assertDoesNotThrow(() -> {
            map.getStructure(0,0);
        },"Deveria devolver a Estrutura");

    }

    @Test
    void getNullStructuresInsideTheMap() throws GameException {

        assertNull(map.getStructure(0, 0), "Deveria ser null");

    }

    @Test
    void getStructuresInfoOutsideMap(){
        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.getStructure(9,-1);
        },"Deveria ter Lançado CoordinatesOutOfBoundsException");

    }

    @Test
    void spaceIsOccupied() throws GameException {
            map.addStructure(new Forest(new Player("Player"),1),0,0);

        assertFalse(map.isNotOccupied(0, 0), "O espaço deveria estar ocupado");
    }

    @Test
    void spaceIsNotOccupied() throws GameException {


        assertTrue(map.isNotOccupied(0, 0), "O espaço não deveria estar ocupado");
    }

    @Test
    void spaceIsOccupiedOutsideMap() throws GameException {
        map.addStructure(new Forest(new Player("Player"),1),0,0);


        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.isNotOccupied(-2,10);
        }, "Deveria ter lançado CoordinatesOutOfBoundsException");

    }

    @Test
    void canInteract() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);

        assertDoesNotThrow(() -> {
            map.canInteract(0,0,player1);
        },"O jogador deveria conseguir interagir");
    }

    @Test
    void canInteractThrowsStructureDontExistException(){

        assertThrows(StructureDontExistException.class, () -> {
            map.canInteract(0,0,player1);
        }, "Não deveria conseguir interagir com null");

    }

    @Test
    void canInteractThrowsUnauthorizedActionException() throws GameException {
        Player player2 = new Player("Player2");

        map.addStructure(new Forest(player2,1),0,0);

        assertThrows(UnauthorizedActionException.class, () -> {
            map.canInteract(0,0,player1);
        }, "Não deveria conseguir interagir com a estrutura que não lhe pertence");

    }

    @Test
    void canInteractOutsideMap() {

        assertThrows(CoordinatesOutOfBoundsException.class, () -> {
            map.canInteract(-2,10,player1);
        }, "Não deveria conseguir interagir fora do mapa");

    }

    @Test
    void testGenerateResources() throws GameException {
        map.addStructure(new Forest(player1,1),0,0);

        int woodQuantityBefore = player1.getResourceQuantity(ResourceType.WOOD);

        map.generateResources(1);

        int woodQuantityAfter = player1.getResourceQuantity(ResourceType.WOOD);

        assertTrue(woodQuantityAfter > woodQuantityBefore, "Não gerou os recursos corretamente");
    }

    @Test
    void testConsumeResourcesWithoutDamagingTheStructure() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);

        int levelBefore = map.getStructure(0,0).getLevel();

        map.consumeResources();

        int levelAfter = map.getStructure(0,0).getLevel();

        assertEquals(levelAfter, levelBefore, "O Nivel não deveria diminuir");
    }

    @Test
    void testConsumeResourcesDamagingTheStructure() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);
        player1.addResource(Forest.getMATERIAL_TO_UPGRADE(),100);
        player1.addResource(ResourceType.ACTION_POINTS, 100);
        GameEngine.upgradeStructure(map,player1,0,0,1);

        int levelBefore = map.getStructure(0,0).getLevel();

        player1.clearInventory();

        map.consumeResources();
        int levelAfter = map.getStructure(0,0).getLevel();

        assertTrue(levelAfter < levelBefore, "O Nivel não diminuiu");
        assertEquals(levelAfter + 1, levelBefore, "O Nivel não foi diminuido corretamente");

    }

    @Test
    void testConsumeResourcesDestroyingTheStructure() throws GameException {

        map.addStructure(new Forest(player1,1),0,0);
        player1.clearInventory();
        map.consumeResources();

        assertNull(map.getStructure(0, 0));

    }
}
