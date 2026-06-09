package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.exceptions.*;

/**
 * Factory and utility class responsible for instantiating game structures
 * and retrieving their baseline costs and requirements.
 * Centralizes construction logic for all valid StructureTypes values.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class CreateStructure {

    /**
     * Retrieves the baseline material resource cost required to build a specific structure type.
     *
     * @param structure The StructuresType to query.
     * @return The integer cost value of the primary building material.
     */
    public static int getMaterialCost(StructuresType structure) {
        switch (structure) {
            case FOREST:
                return Forest.getEXPENSE();
            case MINE:
                return Mine.getEXPENSE();
            case RANCH:
                return Ranch.getEXPENSE();
            case CITY:
                return City.getEXPENSE();
            default:
                return 0;
        }
    }

    /**
     * Determines the specific resource type required as payment to construct a given structure type.
     *
     * @param structure The StructuresType to query.
     * @return The required ResourceType, or {@link ResourceType#NONE} if unrecognized.
     */
    public static ResourceType getMaterialType(StructuresType structure) {
        switch (structure) {
            case FOREST:
                return Forest.getCOST_TYPE();
            case CITY:
                return City.getCOST_TYPE();
            case MINE:
                return Mine.getCOST_TYPE();
            case RANCH:
                return Ranch.getCOST_TYPE();
            default:
                return ResourceType.NONE;
        }
    }

    /**
     * Factory method that instantiates a concrete subclass of {@link Structures}
     * based on the provided type identifier.
     *
     * @param structure     The StructuresType indicating which building to create.
     * @param owner         The Player who will own the newly created structure.
     * @param scoreModifier The global score multiplier currently active.
     * @return A newly initialized concrete implementation of Structures, or null if unmapped.
     * @throws GameException If structure instantiation triggers unexpected baseline state violations.
     */
    public static Structures create(StructuresType structure, Player owner, int scoreModifier) throws GameException {
        switch (structure) {
            case FOREST:
                return new Forest(owner, scoreModifier);
            case MINE:
                return new Mine(owner, scoreModifier);
            case RANCH:
                return new Ranch(owner, scoreModifier);
            case CITY:
                return new City(owner, scoreModifier);
            default:
                return null;
        }
    }

    /**
     * Retrieves the Action Points (AP) required to execute the construction of a specific structure type.
     *
     * @param structure The StructuresType to query.
     * @return The AP construction cost, or a restrictive high value if the type is unrecognized.
     */
    public static int getApCost(StructuresType structure) {
        switch (structure) {
            case FOREST:
                return Forest.getApNeeded();
            case MINE:
                return Mine.getApNeeded();
            case RANCH:
                return Ranch.getApNeeded();
            case CITY:
                return City.getApNeeded();
            default:
                return 9999;
        }
    }
}