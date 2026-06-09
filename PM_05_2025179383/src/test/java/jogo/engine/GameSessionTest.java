package jogo.engine;

import jogo.models.Player;
import jogo.models.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameSession class.
 *
 * This class tests the main behavior of a game session,
 * including starting a new game, loading a saved game,
 * triggering daily events, ending turns, changing players,
 * progressing days and determining the winner.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class GameSessionTest {

    /**
     * GameSession object used in the tests.
     */
    private GameSession session;

    /**
     * Creates a new GameSession before each test.
     *
     * A new game is started with two test players so that each test
     * begins with a valid and clean game state.
     */
    @BeforeEach
    void setUp() {
        session = new GameSession();
        session.startNewGame("Fabio", "Computer");
    }

    /**
     * Tests if a new game is initialized correctly.
     *
     * The test verifies if the map is created, if both players are created
     * with the correct names, if Player 1 starts the game, if the day starts
     * at 1 and if the first turn belongs to Player 1.
     */
    @Test
    void testStartNewGameInitializesCorrectly() {
        assertNotNull(session.getMap(), "O mapa deve ser inicializado.");
        assertEquals("Fabio", session.getPlayer1().getName());
        assertEquals("Computer", session.getPlayer2().getName());
        assertEquals(session.getPlayer1(), session.getActualPlayer(), "O jogo deve começar no Player 1.");
        assertEquals(1, session.getDay(), "O jogo deve começar no dia 1.");
        assertTrue(session.isPlayerOneTurn(), "Deve ser o turno do Player 1.");
    }

    /**
     * Tests if a saved game state is restored correctly.
     *
     * The test creates a custom map, two custom players and a saved day.
     * After loading the game, it verifies if all values were correctly
     * restored into the session.
     */
    @Test
    void testLoadGameRestoresState() {
        WorldMap customMap = new WorldMap();
        Player p1 = new Player("Custom1");
        Player p2 = new Player("Custom2");
        int savedDay = 5;

        session.loadGame(customMap, p1, p2, savedDay);

        assertEquals(customMap, session.getMap());
        assertEquals(p1, session.getPlayer1());
        assertEquals(p2, session.getPlayer2());
        assertEquals(savedDay, session.getDay());
        assertTrue(session.isPlayerOneTurn(), "Por defeito, o load coloca o turno no Player 1.");
    }

    /**
     * Tests if starting a day event updates the game modifiers.
     *
     * The test verifies if the event returns a message and if the resource
     * and score modifiers are valid values.
     *
     * Valid modifier values are 0, 1 or 2.
     */
    @Test
    void testStartDayEventUpdatesModifiers() {
        String msg = session.startDayEvent();

        assertNotNull(msg, "O evento deve devolver uma mensagem informativa.");

        int resMod = session.getResourceModifier();
        int scoreMod = session.getScoreModifier();

        assertTrue(resMod == 0 || resMod == 1 || resMod == 2, "Modificador de recursos inválido.");
        assertTrue(scoreMod == 0 || scoreMod == 1 || scoreMod == 2, "Modificador de score inválido.");
    }

    /**
     * Tests the end of Player 1's turn.
     *
     * The test simulates Action Point usage by Player 1 and then ends the turn.
     * It verifies if Player 1's Action Points are restored, if the current player
     * changes to Player 2, if no new day event is triggered and if the day
     * remains the same.
     */
    @Test
    void testEndTurnFromPlayerOneToPlayerTwo() {
        Player p1 = session.getActualPlayer();

        p1.removeResource(ResourceType.ACTION_POINTS, 5);

        String resultMessage = session.endTurn();

        assertNull(resultMessage, "Mudar do Player 1 para o Player 2 não deve disparar mensagens de novo dia.");
        assertEquals(session.getPlayer2(), session.getActualPlayer(), "O jogador atual deve passar a ser o Player 2.");
        assertEquals(p1.getBaseActionPoints(), p1.getActionPoints(), "Os Action Points do Player 1 deviam ter sido repostos.");
        assertFalse(session.isPlayerOneTurn(), "Já não deve ser o turno do Player 1.");
        assertEquals(1, session.getDay(), "O dia não deve mudar enquanto o Player 2 não jogar.");
    }

    /**
     * Tests the end of Player 2's turn.
     *
     * The test first passes the turn from Player 1 to Player 2.
     * Then, when Player 2 ends the turn, the day should increase,
     * the turn should return to Player 1 and a new daily event should occur.
     */
    @Test
    void testEndTurnFromPlayerTwoTriggersNewDay() {
        session.endTurn();

        int dayBefore = session.getDay();

        String eventMessage = session.endTurn();

        assertEquals(dayBefore + 1, session.getDay(), "O dia deve ter incrementado.");
        assertEquals(session.getPlayer1(), session.getActualPlayer(), "O turno deve voltar para o Player 1.");
        assertTrue(session.isPlayerOneTurn(), "Deve voltar a ser o turno do Player 1.");
        assertNotNull(eventMessage, "A transição de dia deve despoletar um evento diário.");
    }

    /**
     * Tests if the game ends after the maximum number of days
     * and if the winner is determined correctly.
     *
     * The test advances the game by ending turns until the total number
     * of days is exceeded. Then it verifies if the game is over.
     *
     * After that, the test changes the players' scores to check three cases:
     * Player 1 wins, Player 2 wins and both players tie.
     */
    @Test
    void testGameOverAndWinnerDetermination() {
        int totalTurnsToLoop = session.getTOTAL_DAYS() * 2;

        for (int i = 0; i < totalTurnsToLoop; i++) {
            session.endTurn();
        }

        assertTrue(session.isGameOver(), "O jogo deveria estar terminado após os turnos regulamentares.");
        assertNull(session.endTurn(), "Chamar endTurn com o jogo terminado não deve processar mais dias.");

        session.getPlayer1().addScore(50);
        session.getPlayer2().addScore(30);

        assertEquals(session.getPlayer1(), session.getWinner(), "O Player 1 devia ser o vencedor com mais pontos.");

        session.getPlayer2().addScore(30);

        assertEquals(session.getPlayer2(), session.getWinner(), "O Player 2 devia ser o vencedor após a ultrapassagem.");

        session.getPlayer1().addScore(10);

        assertNull(session.getWinner(), "Em caso de empate, o vencedor deve retornar null.");
    }
}