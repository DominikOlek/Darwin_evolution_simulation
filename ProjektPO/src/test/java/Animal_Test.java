import agh.ics.oop.Models.Enums.MapDirection;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Animal_Test {
    @Test
    public void moveTEST(){
        Vector2D vector = new Vector2D(1,0);
        Dna dna = new Dna(List.of(1,4),1); //size 1 znaczy tutaj że losowanie zawsze wybierze 1 jako początkowe ustawienie
        Animal animal = new Animal(vector,dna,2);

        Assertions.assertEquals(MapDirection.NORTHEAST,animal.getDirection());

        animal.move(new Vector2D(2,1));

        Assertions.assertEquals(1,animal.getEnergy());
        Assertions.assertEquals(1,animal.getCurrentDay());
        Assertions.assertEquals(new Vector2D(2,1),animal.getPosition());
        Assertions.assertEquals(MapDirection.SOUTHWEST,animal.getDirection());
        Assertions.assertEquals(1,dna.getActual());

        animal.move(new Vector2D(2,2));

        Assertions.assertEquals(0,animal.getEnergy());
        Assertions.assertEquals(2,animal.getCurrentDay());
        Assertions.assertEquals(new Vector2D(2,2),animal.getPosition());
        Assertions.assertEquals(MapDirection.WEST,animal.getDirection());
        Assertions.assertEquals(4,dna.getActual());

        animal.move(new Vector2D(3,4));

        Assertions.assertEquals(0,animal.getEnergy());
        Assertions.assertEquals(2,animal.getCurrentDay());
        Assertions.assertEquals(new Vector2D(2,2),animal.getPosition());
        Assertions.assertEquals(MapDirection.WEST,animal.getDirection());
        Assertions.assertEquals(4,dna.getActual());
    }

    @Test
    public void rotateTEST(){
        Vector2D vector = new Vector2D(1,0);
        Dna dna = new Dna(List.of(4,4,4),3);
        Animal animal = new Animal(vector,dna,2);

        animal.rotate(3);
        Assertions.assertEquals(MapDirection.NORTHWEST,animal.getDirection());

        animal.rotate(1);
        Assertions.assertEquals(MapDirection.NORTH,animal.getDirection());

        animal.rotate(-11);
        Assertions.assertEquals(MapDirection.NORTH,animal.getDirection());

        animal.rotate(11);
        Assertions.assertEquals(MapDirection.SOUTHEAST,animal.getDirection());

        animal.fullRotate();
        Assertions.assertEquals(MapDirection.NORTHWEST,animal.getDirection());

    }

    @Test
    public void multiplicationTEST(){
        Vector2D vector = new Vector2D(1,0);
        Dna dna = new Dna(List.of(4,3,2,1,0),5);
        Animal animal = new Animal(vector,dna,2);

        Vector2D vector2 = new Vector2D(1,0);
        Dna dna2 = new Dna(List.of(7,6,5,4,3),5);
        Animal animal2 = new Animal(vector2,dna2,4);

        List<Integer> childDna = animal.multiplicationWith(animal2,0,4,false); // Z powodu mutacji nie da się przewidzieć dokładnego wyniku

        Assertions.assertEquals(5,childDna.size());

        vector = new Vector2D(1,0);
        dna = new Dna(List.of(4,3,2,1,0),5);
        animal = new Animal(vector,dna,5);

        vector2 = new Vector2D(1,0);
        dna2 = new Dna(List.of(7,6,5,4,3),5);
        animal2 = new Animal(vector2,dna2,5);

        childDna = animal.multiplicationWith(animal2,0,4,true); // Z powodu mutacji nie da się przewidzieć dokładnego wyniku

        Assertions.assertEquals(5,childDna.size());

        vector = new Vector2D(1,0);
        dna = new Dna(List.of(4,3,2,1,0),5);
        animal = new Animal(vector,dna,5);

        vector2 = new Vector2D(2,0);
        dna2 = new Dna(List.of(7,6,5,4,3),5);
        animal2 = new Animal(vector2,dna2,5);

        try {
            animal.multiplicationWith(animal2,0,4,false);
            Assertions.fail("Tu powinnien być wyjątek, inne pozycje");
        }catch (IllegalArgumentException e){
            Assertions.assertNotNull(e);
        }

    }
}
