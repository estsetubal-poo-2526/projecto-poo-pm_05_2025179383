package models.Structures;

import jogo.exceptions.GameException;
import jogo.models.Player;
import jogo.models.Structures.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateStructureTest {

    @Test
    void testCorrectExpenseValue(){

        assertEquals(Forest.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.FOREST),"Devia ter imprimido a expense da Forest correta");
        assertEquals(Mine.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.MINE),"Devia ter imprimido a expense da Mine correta");
        assertEquals(Ranch.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.RANCH),"Devia ter imprimido a expense do Ranch correta");
        assertEquals(City.getEXPENSE(), CreateStructure.getMaterialCost(StructuresType.CITY),"Devia ter imprimido a expense da City correta");

    }

    @Test
    void testCorrectMaterialType(){
        assertEquals(Forest.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.FOREST),"Devia ter imprimido o material que a Forest custa correto");
        assertEquals(Mine.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.MINE),"Devia ter imprimido o material que a Forest custa correto");
        assertEquals(Ranch.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.RANCH),"Devia ter imprimido o material que a Forest custa correto");
        assertEquals(City.getCOST_TYPE(), CreateStructure.getMaterialType(StructuresType.CITY),"Devia ter imprimido o material que a Forest custa correto");
    }

    @Test
    void testCorrectAPNeededQuantity(){
        assertEquals(Forest.getApNeeded(), CreateStructure.getApCost(StructuresType.FOREST),"Devia ter imprimido o AP que a Forest custa correto");
        assertEquals(Mine.getApNeeded(), CreateStructure.getApCost(StructuresType.MINE),"Devia ter imprimido o AP que a Forest custa correto");
        assertEquals(Ranch.getApNeeded(), CreateStructure.getApCost(StructuresType.RANCH),"Devia ter imprimido o AP que a Forest custa correto");
        assertEquals(City.getApNeeded(), CreateStructure.getApCost(StructuresType.CITY),"Devia ter imprimido o AP que a Forest custa correto");
    }

    @Test
    void testSucessffullyCreateAStructure() throws GameException {

        Player player = new Player("PlayerTest");

        assertEquals(Forest.class,CreateStructure.create(StructuresType.FOREST,player,1).getClass(), "Não esta a criar o objeto FOREST de forma correta");
        assertEquals(Mine.class,CreateStructure.create(StructuresType.MINE,player,1).getClass(), "Não esta a criar o objeto MINE de forma correta");
        assertEquals(Ranch.class,CreateStructure.create(StructuresType.RANCH,player,1).getClass(), "Não esta a criar o objeto RANCH de forma correta");
        assertEquals(City.class,CreateStructure.create(StructuresType.CITY,player,1).getClass(), "Não esta a criar o objeto CITY de forma correta");
    }

}
