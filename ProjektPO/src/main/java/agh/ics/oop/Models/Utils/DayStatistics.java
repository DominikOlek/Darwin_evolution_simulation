package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Sprite.Animal;

import java.util.*;
import java.util.stream.Collectors;

public abstract class DayStatistics {
    private int numberOfAnimalsLife = 0;
    private int numberOfAnimalsDead = 0;
    private int numbersOfGrass = 0;
    protected int numbersOfPlaces;
    private float allEnergy;
    private float allLifeTime = 0;
    private float allNumberOfChildren = 0;
    private int numberOfUsePlaces = 0;

    private Map<Dna,Integer> countDna =  new HashMap<Dna,Integer>();

    protected void setSurface(Boundary boundary) {
        numbersOfPlaces = (boundary.upperRight().getX()-boundary.lowerLeft().getX() +1 ) * (boundary.upperRight().getY()-boundary.lowerLeft().getY() +1);
    }

    protected void setStartEnergy(int howMany,float startEnergy) {
        allEnergy = howMany*startEnergy;
        numberOfAnimalsLife = howMany;
    }

    protected void changeNumberOfUsePlaces(int howMany) {
        numberOfUsePlaces += howMany;
    }

    protected void changeNumberOfAnimals(boolean isBorn, Animal animal) {
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

    protected void addDna(Dna dna) {
        countDna.compute(dna, (k, a) -> countDna.containsKey(dna) ? a + 1 : 1);
    }

    protected void changeGrass(int number){
        numbersOfGrass += number;
    }

    protected void allMove(){
        allEnergy -= numberOfAnimalsLife;
    }

    protected void oneEat(int energy){
        this.allEnergy += energy;
        changeGrass(-1);
    }

    public int getNumberOfAnimals(){
        return this.numberOfAnimalsLife+this.numberOfAnimalsDead;
    }
    public int getNumbersOfGrass(){
        return numbersOfGrass;
    }
    public int getNumberOfFreePlaces(){
        return this.numbersOfPlaces - this.numberOfUsePlaces;
    }
    public float getAverageEnergy(){
        if (numberOfAnimalsLife !=0)
            return this.allEnergy/this.numberOfAnimalsLife;
        return 0;
    }
    public float getAverageLifeTime(){
        if (numberOfAnimalsDead!=0)
            return this.allLifeTime/this.numberOfAnimalsDead;
        return 0;
    }
    public float getAverageNumberOfChildren(){
        if (numberOfAnimalsLife !=0)
            return this.allNumberOfChildren/this.numberOfAnimalsLife;
        return 0;
    }
    public List<Map.Entry<Dna,Integer>> getTopDna(int howMany) {
        if (howMany <0)
            return null;
        PriorityQueue<Map.Entry<Dna,Integer>> result = new PriorityQueue<>((a,b)-> a.getValue().compareTo(b.getValue()));
        result.addAll(countDna.entrySet());

        return result.stream().limit(howMany).collect(Collectors.toList());
    }

    public Map<String,String> getStatistics() {
        Map<String,String> data=new HashMap<>();
        data.put("Number of Animals: ",Integer.toString(getNumberOfAnimals()));
        data.put("Number of Free Places: ",Integer.toString(getNumberOfFreePlaces()));
        data.put("Number of Grass: ",Integer.toString(getNumbersOfGrass()));
        data.put("Avg Energy: ",String.format("%.2f", getAverageEnergy()));
        data.put("Avg LifeTime: ",String.format("%.2f", getAverageLifeTime()));
        data.put("Avg Number of Child: ",String.format("%.2f", getAverageNumberOfChildren()));
        data.put("Top Dna: ",getTopDna(3).toString());
        return data;
    }

}
