package agh.ics.oop.Models.Maps;

import agh.ics.oop.Models.Sprite.StatisticalObject;
import agh.ics.oop.Models.Utils.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class WorldMap implements StatisticReport, WorldMapI {
    private int numberOfAnimalsLife = 0;
    private int numberOfAnimalsDead = 0;
    private int numbersOfGrass = 0;
    protected int numbersOfPlaces;
    private float allEnergy;
    private float allLifeTime = 0;
    private float allNumberOfChildren = 0;
    private int numberOfUsePlaces = 0;
    protected int currentDay = 1;
    private final int ID;

    public WorldMap(){
        ID = GenerateID.GetID();
    }

    private final Map<Dna,Integer> countDna =  new HashMap<Dna,Integer>();

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

    protected void changeNumberOfAnimals(boolean isBorn, StatisticalObject animal) {
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
        try {
            countDna.compute(dna, (k, a) -> countDna.containsKey(dna) ? a + 1 : 1);
        }catch (NullPointerException e) {
            System.out.println("DNA not found");
        }
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
        List<Map.Entry<Dna, Integer>> result = new ArrayList<>(countDna.entrySet());

        return result.stream()
                .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                .limit(howMany)
                .collect(Collectors.toList());
    }

    public int getID(){
        return ID;
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

    public int getCurrentDay(){
        return currentDay;
    }

}
