package jogo.models;

/**
 * Defines the types of resources available within the game economy.
 * Used to categorize player inventory contents and structure production/consumption costs.
 * * @author Fabio Cruz
 * @author Tiago Silva
 */
public enum ResourceType {
    /** Represents wood material used primarily for constructing basic buildings. */
    WOOD,

    /** Represents stone material used primarily for advanced structures and upgrades. */
    STONE,

    /** Represents food supplies required for daily structure maintenance and population. */
    FOOD,

    /** Represents the action currency required for a player to perform builds or upgrades during a turn. */
    ACTION_POINTS,

    /** Represents the absence of a resource type, used for validation or empty requirements. */
    NONE
}