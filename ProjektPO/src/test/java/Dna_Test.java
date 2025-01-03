
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Dna_Test {
    @Test
    public void constructorGeneratorTEST(){
        Dna dna = new Dna(20);
        List<Integer> val = dna.getPartToMultiplication(1,true);
        if (val.size() != 20 || dna.getActual() < 0 || dna.getActual() >= 20){
            Assertions.fail();
        }
        for(int i=0;i<20;i++){
            if (val.get(i) <0 || val.get(i)>=8){
                Assertions.fail();
            }
        }
        Assertions.assertTrue(true);
    }

    @Test
    public void getPartTEST(){
        Dna dna = new Dna(20);
        List<Integer> val = dna.getPartToMultiplication(1,true);

        List<Integer> val1 = dna.getPartToMultiplication(1,true);
        List<Integer> val2 = dna.getPartToMultiplication(1,false);
        if (val1.size() != 20 || val2.size() != 20){
            Assertions.fail();
        }else {
            Assertions.assertTrue(true);
        }

        val1 = dna.getPartToMultiplication(0.5f,true);
        val2 = dna.getPartToMultiplication(0.5f,false);
        if (val1.size() != 10 || val2.size() != 10 || !val1.equals(val.subList(0,10)) || !val2.equals(val.subList(10,20))){
            Assertions.fail();
        }else {
            Assertions.assertTrue(true);
        }

        val1 = dna.getPartToMultiplication(0.3f,true);
        val2 = dna.getPartToMultiplication(0.7f,false);
        if (val1.size() != 6 || val2.size() != 14 || !val1.equals(val.subList(0,6)) || !val2.equals(val.subList(6,20))){
            Assertions.fail();
        }else {
            Assertions.assertTrue(true);
        }

        dna = new Dna(19);
        val = dna.getPartToMultiplication(1,true);

        val1 = dna.getPartToMultiplication(0.3f,true);
        val2 = dna.getPartToMultiplication(0.7f,false);
        if (val1.size() != 6 || val2.size() != 13 || !val1.equals(val.subList(0,6)) || !val2.equals(val.subList(6,19))){
            Assertions.fail();
        }else {
            Assertions.assertTrue(true);
        }

        val1 = dna.getPartToMultiplication(0.5f,true);
        val2 = dna.getPartToMultiplication(0.5f,false);
        if (val1.size() != 10 || val2.size() != 9 || !val1.equals(val.subList(0,10)) || !val2.equals(val.subList(10,19))){
            Assertions.fail();
        }else {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void toStringTEST(){
        List<Integer> val = new ArrayList<Integer>(List.of(2,7,5,1,5,4,2,1,3,6));
        Dna dna = new Dna(val,10);
        Assertions.assertEquals("Dna=[2, 7, 5, 1, 5, 4, 2, 1, 3, 6]" ,dna.toString());
    }
}
