package jogo.engine;

import jogo.models.Player;
import jogo.models.ResourceType;

import java.util.Map;

/**
 * Manages the core game state, including player turns, day progression,
 * and invoking the event system to update game modifiers.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class GameSession {

    private WorldMap map;
    private Player player1;
    private Player player2;
    private Player actualPlayer;
    private Player opponent;

    private Events events;

    private int day;
    private final int TOTAL_DAYS = 30;

    private int scoreModifier;
    private int resourceModifier;

    private boolean playerOneTurn;

    private String currentEventMessage;

    public GameSession() {
        this.map = new WorldMap();
        this.events = new Events();
        this.day = 1;
        this.scoreModifier = 1;
        this.resourceModifier = 1;
        this.playerOneTurn = true;
        this.currentEventMessage = "";
    }

    /**
     * Initializes a new game session from scratch, creating a fresh world map,
     * resetting both players, and setting the calendar back to day 1.
     *
     * @param player1Name The name of the player who takes the first turn.
     * @param player2Name The name of the player who follows.
     */
    public void startNewGame(String player1Name, String player2Name) {
        this.map = new WorldMap();

        this.player1 = new Player(player1Name);
        this.player2 = new Player(player2Name);

        this.actualPlayer = player1;
        this.opponent = player2;

        this.day = 1;
        this.playerOneTurn = true;

        this.events = new Events();
        this.scoreModifier = 1;
        this.resourceModifier = 1;

        this.currentEventMessage = startDayEvent();
    }

    /**
     * Loads an existing game session from a saved state, restoring the map
     * with its structures, the players with their inventories, and the specific day.
     *
     * @param map The saved WorldMap state to be restored.
     * @param player1 The saved data for player 1.
     * @param player2 The saved data for player 2.
     * @param day The current day retrieved from the save file.
     */
    public void loadGame(WorldMap map, Player player1, Player player2, int day) {
        this.map = map;

        this.player1 = player1;
        this.player2 = player2;

        this.actualPlayer = player1;
        this.opponent = player2;

        this.day = day;
        this.playerOneTurn = true;

        this.events = new Events();
        this.scoreModifier = 1;
        this.resourceModifier = 1;

        this.currentEventMessage = startDayEvent();
    }

    /**
     * Triggers a random daily event, updates the session's current resource
     * and score modifiers based on the event's outcome, and returns the event message.
     *
     * @return A String describing the event and its effects for the current day.
     */
    public String startDayEvent() {
        this.currentEventMessage = events.triggerEvent();

        this.scoreModifier = events.getScoreModifier();
        this.resourceModifier = events.getResourceModifier();

        return currentEventMessage;
    }

    /**
     * Ends the current player's turn, switching active players and managing day progression.
     *
     * @return A String containing the current or new event message.
     */
    public String endTurn() {

        if (isGameOver()) {
            return null;
        }

        actualPlayer.resetAP();

        if (playerOneTurn) {
            actualPlayer = player2;
            opponent = player1;
            playerOneTurn = false;

            return currentEventMessage;
        }

        map.generateResources(resourceModifier);
        map.consumeResources();

        day++;

        actualPlayer = player1;
        opponent = player2;
        playerOneTurn = true;

        if (!isGameOver()) {
            return startDayEvent();
        }

        return null;
    }

    /**
     * Returns the resources that will be consumed at the end of the day
     * for the current active player.
     *
     * @return A map containing the resource costs for the current player.
     */
    public Map<ResourceType, Integer> getActualPlayerEndDayCosts() {
        return GameEngine.calculateEndDayCosts(map, actualPlayer);
    }

    /**
     * Determines the winner of the game session once the match has concluded.
     * Compares the final scores of both players to declare the victor.
     *
     * @return The Player object with the highest score;
     * null if the game is still in progress or if the match ends in a tie.
     */
    public Player getWinner() {
        if (!isGameOver()) {
            return null;
        }

        if (player1.getScore() > player2.getScore()) {
            return player1;
        }

        if (player2.getScore() > player1.getScore()) {
            return player2;
        }

        return null;
    }

    public boolean isGameOver() {
        return day > TOTAL_DAYS;
    }

    public WorldMap getMap() {
        return map;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public Player getOpponent() {
        return opponent;
    }

    public int getDay() {
        return day;
    }

    public int getTOTAL_DAYS() {
        return TOTAL_DAYS;
    }

    public int getScoreModifier() {
        return scoreModifier;
    }

    public int getResourceModifier() {
        return resourceModifier;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public String getCurrentEventMessage() {
        return currentEventMessage;
    }
}