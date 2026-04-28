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
    public static int getMaterialCost(String structure) {
        switch (structure.toLowerCase()) {
            case "floresta":
            case "mina":
            case "rancho":
                return 3;
            case "cidade":
                return 10;
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


    public static ResourceType getMaterialType(String structure) {
        switch (structure.toLowerCase()) {
            case "floresta": case "cidade": return ResourceType.STONE;
            case "mina":    case "rancho": return ResourceType.WOOD;

            default: return ResourceType.NONE;
        }
    }

    /**
     * Cria a estrutura
     * @param structure estrutura a ser criada
     * @param owner dono da estrutura
     * @return a estrutura criada
     */
    public static Structures create(String structure, Player owner) {

        switch (structure.toLowerCase()) {
            case "floresta":
            case "forest":
                return new Forest(owner);

            case "mina":
            case "mine":
                return new Mine(owner);

            case "rancho":
            case "ranch":
                return new Ranch(owner);

            case "cidade":
            case "city":
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
    public static int getApCost(String structure) {
        switch (structure.toLowerCase()) {
            case "floresta":
                return Forest.getApNeeded();

            case "mina":
                return Mine.getApNeeded();

            case "rancho":
                return Ranch.getApNeeded();

            case "cidade":
                return City.getApNeeded();

            default:
                return 9999;


        }
    }
}
