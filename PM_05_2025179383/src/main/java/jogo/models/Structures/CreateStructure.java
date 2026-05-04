package jogo.models.Structures;

import jogo.models.Player;
import jogo.models.ResourceType;

public class CreateStructure {

    /**
     *
     * Metodo para obter a quantidade de recursos que a estrutura custa
     *
     * @param structure estrutura em que se quer saber
     * @return valor do custo
     */
    public static int getMaterialCost(StructuresType structure) {
        switch (structure) {
            case FOREST:
                return Forest.getExpense();
            case MINE:
                return Mine.getExpense();
            case RANCH:
                return Ranch.getEXPENSE();
            case CITY:
                return City.getEXPENSE();
            default:
                return 0;
        }
    }

    /**
     *
     * Metodo para saber o tipo de material que a estrutura precisa para ser construida
     *
     * @param structure estrutura em que se quer saber
     * @return tipo de recurso
     */


    public static ResourceType getMaterialType(StructuresType structure) {
        switch (structure) {
            case FOREST: case CITY: return ResourceType.STONE;
            case MINE:    case RANCH: return ResourceType.WOOD;

            default: return ResourceType.NONE;
        }
    }

    /**
     * Cria a estrutura
     * @param structure estrutura a ser criada
     * @param owner dono da estrutura
     * @return a estrutura criada
     */
    public static Structures create(StructuresType structure, Player owner) {

        switch (structure) {
            case FOREST:
                return new Forest(owner);

            case MINE:
                return new Mine(owner);

            case RANCH:
                return new Ranch(owner);

            case CITY:
                return new City(owner);

            default:
                System.out.println("Erro: Tipo de estrutura '" + structure + "' não reconhecido.");
                return null;
        }
    }


    /**
     * Devolve a quantidade de AP necessarios para criar a estrutura
     * @param structure estrutura de que se quer saber
     * @return valor necessario para construir
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
