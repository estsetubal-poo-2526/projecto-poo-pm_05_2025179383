package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;
import jogo.exceptions.*;

public class CreateStructure {

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

    public static ResourceType getMaterialType(StructuresType structure) {
        switch (structure) {
            case FOREST: case CITY: return ResourceType.STONE;
            case MINE:    case RANCH: return ResourceType.WOOD;

            default: return ResourceType.NONE;
        }
    }

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
                throw new StructureDontExistException();

        }
    }


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
