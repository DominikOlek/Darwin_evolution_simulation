package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Sprite.Animal;

import java.util.*;
import java.util.stream.Collectors;

public abstract class DayStatistics {
    private int numberOfAnimalsLife = 0;
    private int numberOfAnimalsDead = 0;
    private int numbersOfGrass = 0;
    private int numbersOfPlaces;
    private float allEnergy;
    private float allLifeTime = 0;
    private float allNumberOfChildren = 0;
    private boolean isAddDna = true;
    private int numberOfUsePlaces = 0;

    private Map<Dna,Integer> countDna =  new HashMap<Dna,Integer>();

    public void setSurface(Boundary boundary) {
        numbersOfPlaces = (boundary.upperRight().getX()-boundary.lowerLeft().getX() +1 ) * (boundary.upperRight().getY()-boundary.lowerLeft().getY() +1);
    }

    public void setStartEnergy(int howMany,float startEnergy) {
        allEnergy = howMany*startEnergy;
        numberOfAnimalsLife = howMany;
    }

    public void changeNumberOfUsePlaces(int howMany) {
        numberOfUsePlaces += howMany;
    }

    public void changeNumberOfAnimals(boolean isBorn, Animal animal) {
        if (isBorn) {
            this.numberOfAnimalsLife++;
            allNumberOfChildren+=2;
            addDna(animal.getDna());
        }else{
            this.numberOfAnimalsDead++;
            this.numberOfAnimalsLife--;
            allLifeTime+=animal.getCurrentDay();
            allNumberOfChildren-=animal.getNumberOfChild();
        }
    }

    public void addDna(Dna dna) {
        countDna.compute(dna, (k, a) -> countDna.containsKey(dna) ? a + 1 : 1);
        isAddDna = true;
    }

    public void allMove(){
        allEnergy -= numberOfAnimalsLife;
    }

    public void oneEat(int energy){
        this.allEnergy += energy;
    }

    public int getNumberOfAnimals(){
        return this.numberOfAnimalsLife+this.numberOfAnimalsDead;
    }
    public int getNumberOfFreePlaces(){
        return this.numbersOfPlaces - this.numberOfUsePlaces;
    }
    public float getAverageEnergy(){
        return this.allEnergy/this.numberOfAnimalsLife;
    }
    public float getAverageLifeTime(){
        return this.allLifeTime/this.numberOfAnimalsDead;
    }
    public float getAverageNumberOfChildren(){
        return this.allNumberOfChildren/this.numberOfAnimalsLife;
    }
    public List<Map.Entry<Dna,Integer>> getTopDna(int howMany) {
        if (howMany <0 || !isAddDna)
            return null;
        PriorityQueue<Map.Entry<Dna,Integer>> result = new PriorityQueue<>((a,b)-> a.getValue().compareTo(b.getValue()));
        result.addAll(countDna.entrySet());
        isAddDna = false;

        return result.stream().limit(howMany).collect(Collectors.toList());
    }

}
