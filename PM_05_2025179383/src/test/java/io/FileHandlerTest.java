package io;

import jogo.engine.WorldMap;
import jogo.exceptions.*;
import jogo.io.FileHandler;
import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.models.Structures.Forest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    private static final String FILE_NAME = "savegame.csv";
    private File backupFile = new File("savegame_backup.csv");
    private File realFile = new File(FILE_NAME);

    private WorldMap map;
    private Player p1;
    private Player p2;
    private int day;

    @BeforeEach
    void setUp() throws Exception {
        // TRUQUE DE MAGIA: Altera a constante FILE_NAME do FileHandler apenas durante o teste
        Field field = FileHandler.class.getDeclaredField("FILE_NAME");
        field.setAccessible(true);
        field.set(null, "savegame_test.csv"); // O teste passa a usar este ficheiro temporário

        map = new WorldMap();
        p1 = new Player("Fabio");
        p2 = new Player("Computer");
        day = 4;

        p1.addScore(150);
        p1.addResource(ResourceType.WOOD, 25);
        p2.addScore(80);
        p2.addResource(ResourceType.STONE, 12);
        map.addStructure(new Forest(p1, 1), 2, 3);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Limpa o ficheiro de teste do disco
        File testFile = new File("savegame_test.csv");
        if (testFile.exists()) {
            testFile.delete();
        }

        // Restaura o nome original para não estragar o jogo real fora dos testes
        Field field = FileHandler.class.getDeclaredField("FILE_NAME");
        field.setAccessible(true);
        field.set(null, "savegame.csv");
    }

    @Test
    void testSaveAndLoadFullGameWithSuccess() {
        assertDoesNotThrow(() -> {
            FileHandler.saveFullGame(map, p1, p2, day);
        }, "Não devia lançar exceção ao gravar o jogo.");

        File file = new File(FILE_NAME);
        assertTrue(file.exists(), "O ficheiro de save devia ter sido criado no disco.");

        final FileHandler.SaveData[] loadedDataContainer = new FileHandler.SaveData[1];
        assertDoesNotThrow(() -> {
            loadedDataContainer[0] = FileHandler.loadGame();
        }, "Não devia lançar exceção ao carregar um save válido.");

        FileHandler.SaveData loadedData = loadedDataContainer[0];
        assertNotNull(loadedData, "Os dados carregados não deviam ser nulos.");

        assertEquals(day, loadedData.getDay(), "O dia restaurado não coincide.");
        assertEquals("Fabio", loadedData.getP1().getName());
        assertEquals(154, loadedData.getP1().getScore(), "O score do Player 1 não foi restaurado corretamente.");
        assertEquals(35, loadedData.getP1().getResourceQuantity(ResourceType.WOOD), "A madeira do Player 1 falhou.");

        assertEquals("Computer", loadedData.getP2().getName());
        assertEquals(80, loadedData.getP2().getScore(), "O score do Player 2 não coincide.");
        assertEquals(22, loadedData.getP2().getResourceQuantity(ResourceType.STONE), "A pedra do Player 2 falhou.");

        assertDoesNotThrow(() -> {
            assertNotNull(loadedData.getMap().getStructure(2, 3), "A estrutura devia ter sido recriada na posição (2,3).");
            assertEquals(Forest.class, loadedData.getMap().getStructure(2, 3).getClass(), "A estrutura devia ser uma Forest.");
            assertEquals("Fabio", loadedData.getMap().getStructure(2, 3).getOwner().getName(), "O dono da estrutura mudou de forma errada.");
        });
    }

    @Test
    void testLoadGameThrowsGameExceptionWhenFileDoesNotExist() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }

        assertThrows(GameException.class, () -> {
            FileHandler.loadGame();
        }, "Devia ter lançado GameException devido ao ficheiro não existir no caminho.");
    }
}