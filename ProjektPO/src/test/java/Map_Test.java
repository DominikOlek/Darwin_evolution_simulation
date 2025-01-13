import agh.ics.oop.Models.Enums.MapDirection;
import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.Models.Utils.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class Map_Test {
    @Test
    public void constructorAndCanMoveTEST() {
        Boundary size = new Boundary(new Vector2D(0,0),new Vector2D(5,5));
        MapSettings set = new MapSettings(5, 0, 1, 5, 3, 0, 0, 20);
        MainMap map = new MainMap(size,set,1);
        Set<Animal> animals = map.copyCollection();
        Animal animal = animals.stream().findFirst().orElseThrow();

        System.out.println(animals);
        Assertions.assertEquals(1,animals.size());
        Assertions.assertEquals(20,animal.getEnergy());
        Assertions.assertTrue(map.canMoveTo(new Vector2D(3,3)));
        Assertions.assertFalse(map.canMoveTo(new Vector2D(0,6)));
        Assertions.assertTrue(map.canMoveTo(new Vector2D(0,0)));
        Assertions.assertFalse(map.canMoveTo(new Vector2D(-1,2)));

    }

    @Test
    public void addAndRemoveTEST() {
        Boundary size = new Boundary(new Vector2D(0,0),new Vector2D(5,5));
        MapSettings set = new MapSettings(5, 0, 1, 5, 3, 0, 0, 20);
        MainMap mapa2 = new MainMap(size,set,0);

        Vector2D vector = new Vector2D(1,1);
        Dna dna = new Dna(List.of(0,1,2,3,4),5);
        Animal animal = new Animal(vector,dna,20);

        Assertions.assertTrue(mapa2.addAnimal(vector,animal));

        Vector2D vector2 = new Vector2D(1,1);
        Dna dna2 = new Dna(List.of(0,1,2,3),4);
        Animal animal2 = new Animal(vector2,dna2,20);

        Assertions.assertFalse(mapa2.addAnimal(vector2,animal2));

        vector2 = new Vector2D(1,1);
        dna2 = new Dna(List.of(0,1,2,3,4),5);
        animal2 = new Animal(vector2,dna2,-1);

        Assertions.assertFalse(mapa2.addAnimal(vector2,animal2));

        Assertions.assertEquals(1,mapa2.getAnimalsAt(vector).size());
        Assertions.assertFalse(mapa2.removeAnimal(vector2,animal2));
        Assertions.assertTrue(mapa2.removeAnimal(vector,animal));
        Assertions.assertNull(mapa2.getAnimalsAt(vector));

    }
    @Test
    public void doDayTEST(){
        //KILL

        Boundary size = new Boundary(new Vector2D(0,0),new Vector2D(5,5));
        MapSettings set = new MapSettings(3, 0, 1, 5, 3, 0, 0, 1);
        MainMap mapa = new MainMap(size,set,0);

        Dna dna = new Dna(List.of(2,4,0),1);
        Animal animal = new Animal(new Vector2D(1,1),dna,1);
        mapa.addAnimal(animal.getPosition(),animal);

        Dna dna2 = new Dna(List.of(0,2,5),1);
        Animal animal2 = new Animal(new Vector2D(0,4),dna2,0);
        mapa.addAnimal(animal2.getPosition(),animal2);

        Assertions.assertEquals(2,mapa.copyCollection().size());
        mapa.doDay();
        Assertions.assertEquals(1,mapa.copyCollection().size());
        mapa.doDay();
        Assertions.assertEquals(0,mapa.copyCollection().size());

        //MOVE

        dna = new Dna(List.of(4,6,7),1);
        animal = new Animal(new Vector2D(0,0),dna,5);
        mapa.addAnimal(animal.getPosition(),animal);

        dna2 = new Dna(List.of(0,6,5),1);
        animal2 = new Animal(new Vector2D(5,5),dna2,5);
        mapa.addAnimal(animal2.getPosition(),animal2);

        mapa.doDay();
        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(0,0)).size());
        Assertions.assertEquals(new Vector2D(0,0),animal.getPosition());
        Assertions.assertEquals(MapDirection.WEST,animal.getDirection());
        Assertions.assertEquals(7,animal.getDna().getActual());

        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(5,5)).size());
        Assertions.assertEquals(new Vector2D(5,5),animal2.getPosition());
        Assertions.assertEquals(MapDirection.EAST,animal2.getDirection());
        Assertions.assertEquals(5,animal2.getDna().getActual());

        mapa.doDay();
        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(5,0)).size());
        Assertions.assertEquals(new Vector2D(5,0),animal.getPosition());
        Assertions.assertEquals(MapDirection.SOUTHWEST,animal.getDirection());
        Assertions.assertEquals(4,animal.getDna().getActual());

        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(0,5)).size());
        Assertions.assertEquals(new Vector2D(0,5),animal2.getPosition());
        Assertions.assertEquals(MapDirection.NORTHWEST,animal2.getDirection());
        Assertions.assertEquals(0,animal2.getDna().getActual());

        mapa.doDay();
        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(4,0)).size());
        Assertions.assertEquals(new Vector2D(4,0),animal.getPosition());
        Assertions.assertEquals(MapDirection.SOUTHWEST,animal.getDirection());
        Assertions.assertEquals(6,animal.getDna().getActual());

        Assertions.assertEquals(1,mapa.getAnimalsAt(new Vector2D(5,5)).size());
        Assertions.assertEquals(new Vector2D(5,5),animal2.getPosition());
        Assertions.assertEquals(MapDirection.SOUTHEAST,animal2.getDirection());
        Assertions.assertEquals(6,animal2.getDna().getActual());


        mapa = new MainMap(size,set,0);

        dna = new Dna(List.of(0,0,0),1);
        animal = new Animal(new Vector2D(1,1),dna,4);
        mapa.addAnimal(animal.getPosition(),animal);

        dna2 = new Dna(List.of(4,0,0),1);
        animal2 = new Animal(new Vector2D(1,3),dna2,4);
        mapa.addAnimal(animal2.getPosition(),animal2);

        mapa.doDay();
        Assertions.assertEquals(2,mapa.getAnimalsAt(new Vector2D(1,2)).size());
        Assertions.assertEquals(new Vector2D(1,2),animal.getPosition());
        Assertions.assertEquals(new Vector2D(1,2),animal2.getPosition());
        Assertions.assertEquals(MapDirection.NORTH,animal.getDirection());
        Assertions.assertEquals(MapDirection.SOUTH,animal2.getDirection());

        mapa.doDay();
        Assertions.assertNull(mapa.getAnimalsAt(new Vector2D(1,2)));
        Assertions.assertEquals(new Vector2D(1,3),animal.getPosition());
        Assertions.assertEquals(new Vector2D(1,1),animal2.getPosition());
        Assertions.assertEquals(MapDirection.NORTH,animal.getDirection());
        Assertions.assertEquals(MapDirection.SOUTH,animal2.getDirection());

        //ROZMNAŻANIE

        mapa = new MainMap(size,set,0);
        dna = new Dna(List.of(0,0,0),1);
        animal = new Animal(new Vector2D(1,1),dna,10);
        mapa.addAnimal(animal.getPosition(),animal);

        dna2 = new Dna(List.of(4,0,0),1);
        animal2 = new Animal(new Vector2D(1,3),dna2,10);
        mapa.addAnimal(animal2.getPosition(),animal2);

        mapa.doDay();
        Assertions.assertEquals(3,mapa.getAnimalsAt(new Vector2D(1,2)).size());
        Assertions.assertEquals(6,animal.getEnergy());
        Assertions.assertEquals(6,animal2.getEnergy());
        Animal finalAnimal = animal;
        Animal finalAnimal1 = animal2;
        Animal child = mapa.getAnimalsAt(new Vector2D(1,2)).stream().filter(an -> an.equals(finalAnimal) || an.equals(finalAnimal1)).toList().getFirst();
        Assertions.assertEquals(6,child.getEnergy());
        Assertions.assertEquals(1,animal.getNumberOfChild());
        Assertions.assertEquals(1,animal2.getNumberOfChild());


        mapa = new MainMap(size,set,0);
        dna = new Dna(List.of(0,0,0),1);
        animal = new Animal(new Vector2D(1,1),dna,10);
        mapa.addAnimal(animal.getPosition(),animal);

        dna2 = new Dna(List.of(4,0,0),1);
        animal2 = new Animal(new Vector2D(1,3),dna2,7);
        mapa.addAnimal(animal2.getPosition(),animal2);

        Dna dna3 = new Dna(List.of(2,0,0),1);
        Animal animal3 = new Animal(new Vector2D(0,2),dna3,10);
        mapa.addAnimal(animal3.getPosition(),animal3);

        mapa.doDay();
        Assertions.assertEquals(4,mapa.getAnimalsAt(new Vector2D(1,2)).size());
        Assertions.assertEquals(6,animal.getEnergy());
        Assertions.assertEquals(6,animal3.getEnergy());
        Assertions.assertEquals(1,animal.getNumberOfChild());
        Assertions.assertEquals(0,animal2.getNumberOfChild());
        Assertions.assertEquals(1,animal3.getNumberOfChild());


        mapa = new MainMap(size,set,0);
        dna = new Dna(List.of(0,0,0),1);
        animal = new Animal(new Vector2D(1,1),dna,10);
        mapa.addAnimal(animal.getPosition(),animal);

        dna2 = new Dna(List.of(4,0,0),1);
        animal2 = new Animal(new Vector2D(1,3),dna2,8);
        mapa.addAnimal(animal2.getPosition(),animal2);

        dna3 = new Dna(List.of(2,0,0),1);
        animal3 = new Animal(new Vector2D(0,2),dna3,4);
        mapa.addAnimal(animal3.getPosition(),animal3);

        mapa.doDay();
        Assertions.assertEquals(4,mapa.getAnimalsAt(new Vector2D(1,2)).size());
        Assertions.assertEquals(6,animal.getEnergy());
        Assertions.assertEquals(4,animal2.getEnergy());
        Assertions.assertEquals(3,animal3.getEnergy());
        Assertions.assertEquals(1,animal.getNumberOfChild());
        Assertions.assertEquals(1,animal2.getNumberOfChild());
        Assertions.assertEquals(0,animal3.getNumberOfChild());
    }
}
