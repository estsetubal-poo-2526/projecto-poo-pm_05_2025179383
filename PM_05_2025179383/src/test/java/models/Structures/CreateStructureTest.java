package models.Structures;

import jogo.exceptions.GameException;
import jogo.models.Player;
import jogo.models.Structures.*;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the CreateStructure class.
 *
 * This class tests the factory and utility methods responsible
 * for creating structures and returning their construction values.
 *
 * The tests verify if the correct material costs, material types,
 * Action Point costs and concrete structure objects are returned
 * for each structure type.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class CreateStructureTest {

    /**
     * Tests if the material cost returned for each structure type is correct.
     *
     * The method compares the value returned by CreateStructure
     * with the expense value defined in each structure class.
     */
    @Test
    void testCorrectExpenseValue() {

        assertEquals(Forest.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.FOREST),
                "Devia ter imprimido a expense da Forest correta");

        assertEquals(Mine.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.MINE),
                "Devia ter imprimido a expense da Mine correta");

        assertEquals(Ranch.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.RANCH),
                "Devia ter imprimido a expense do Ranch correta");

        assertEquals(City.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.CITY),
                "Devia ter imprimido a expense da City correta");
    }

    /**
     * Tests if the correct material type is returned for each structure type.
     *
     * The method verifies if each structure requires the expected
     * ResourceType to be built.
     */
    @Test
    void testCorrectMaterialType() {

        assertEquals(Forest.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.FOREST),
                "Devia ter imprimido o material que a Forest custa correto");

        assertEquals(Mine.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.MINE),
                "Devia ter imprimido o material que a Mine custa correto");

        assertEquals(Ranch.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.RANCH),
                "Devia ter imprimido o material que o Ranch custa correto");

        assertEquals(City.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.CITY),
                "Devia ter imprimido o material que a City custa correto");
    }

    /**
     * Tests if the correct Action Point cost is returned for each structure type.
     *
     * The method compares the AP cost returned by CreateStructure
     * with the AP cost defined in each concrete structure class.
     */
    @Test
    void testCorrectAPNeededQuantity() {

        assertEquals(Forest.getApNeeded(), CreateStructure.getApCost(StructuresType.FOREST),
                "Devia ter imprimido o AP que a Forest custa correto");

        assertEquals(Mine.getApNeeded(), CreateStructure.getApCost(StructuresType.MINE),
                "Devia ter imprimido o AP que a Mine custa correto");

        assertEquals(Ranch.getApNeeded(), CreateStructure.getApCost(StructuresType.RANCH),
                "Devia ter imprimido o AP que o Ranch custa correto");

        assertEquals(City.getApNeeded(), CreateStructure.getApCost(StructuresType.CITY),
                "Devia ter imprimido o AP que a City custa correto");
    }

    /**
     * Tests if CreateStructure creates the correct concrete structure objects.
     *
     * The method creates one structure of each type and verifies if
     * the returned object belongs to the expected class.
     *
     * @throws GameException if an unexpected error occurs while creating a structure
     */
    @Test
    void testSucessffullyCreateAStructure() throws GameException {

        Player player = new Player("PlayerTest");

        assertEquals(Forest.class, Objects.requireNonNull(CreateStructure.create(StructuresType.FOREST, player, 1)).getClass(),
                "Não esta a criar o objeto FOREST de forma correta");

        assertEquals(Mine.class, Objects.requireNonNull(CreateStructure.create(StructuresType.MINE, player, 1)).getClass(),
                "Não esta a criar o objeto MINE de forma correta");

        assertEquals(Ranch.class, Objects.requireNonNull(CreateStructure.create(StructuresType.RANCH, player, 1)).getClass(),
                "Não esta a criar o objeto RANCH de forma correta");

        assertEquals(City.class, Objects.requireNonNull(CreateStructure.create(StructuresType.CITY, player, 1)).getClass(),
                "Não esta a criar o objeto CITY de forma correta");
    }
}