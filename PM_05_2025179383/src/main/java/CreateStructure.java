public class CreateStructure {

    public static Structures create(String structure, Player owner) {
        switch (structure.toLowerCase()) {
            case "floresta":
                return new Forest(owner);

            case "mina":
                return new Mine(owner);

            case "rancho":
                return new Ranch(owner);

            case "cidade":
                return new City(owner);

            default:
                return null;
        }
    }

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
