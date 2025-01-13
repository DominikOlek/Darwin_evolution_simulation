package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Enums.ChangeType;
import agh.ics.oop.Models.Sprite.Animal;

import java.util.List;

public class AnimalStatistics implements Observer {
    List<Integer> dna;
    int activeGen;
    int energy;
    int eat;
    int childNr;
    int allChild;
    int age;
    int dead;
    Vector2D position;

    @Override
    public void update(Animal animal, ChangeType changeType) {
        switch (changeType) {
            case Gen -> dna = animal.getDna().getPartToMultiplication(1,true);
            case ActiveGen -> {activeGen = animal.getDna().getActual(); position=animal.getPosition();energy=animal.getEnergy();age = animal.getCurrentDay();}
            case Eat -> {eat = animal.getHowManyEaten(); energy=animal.getEnergy();}
            case Dead -> {dead=animal.getCurrentDay(); age=0;}
            case Child -> {childNr=animal.getNumberOfChild();energy=animal.getEnergy();}
            case AllChild -> {allChild=animal.getAllChild();}
        }

        System.out.println(this.toString());

    }

    @Override
    public String toString() {
        return "AnimalStatistics{" +
                "dna=" + dna +
                ", activeGen=" + activeGen +
                ", energy=" + energy +
                ", eat=" + eat +
                ", childNr=" + childNr +
                ", allChild=" + allChild +
                ", age=" + age +
                ", dead=" + dead +
                ", position=" + position +
                '}';
    }
}
