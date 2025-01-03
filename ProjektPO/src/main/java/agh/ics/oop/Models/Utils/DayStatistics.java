package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Sprite.Animal;

public abstract class DayStatistics {
    private int numberOfAnimalsLife = 0;
    private int numberOfAnimalsDead = 0;
    private int numbersOfGrass = 0;
    private int numbersOfPlaces;
    private float allEnergy;
    private float allLifeTime = 0;
    private float allNumberOfChildren = 0;

    public void setSurface(Boundary boundary) {
        numbersOfPlaces = (boundary.upperRight().getX()-boundary.lowerLeft().getX() ) * (boundary.upperRight().getY()-boundary.lowerLeft().getY());
    }

    public void setStartEnergy(float energy) {
        allEnergy = energy;
    }

    public void changeNumberOfAnimals(boolean isBorn, Animal animal) {
        if (isBorn) {
            this.numberOfAnimalsLife++;
            allNumberOfChildren++;
        }else{
            this.numberOfAnimalsDead++;
            allLifeTime+=animal.getCurrentDay();
            allNumberOfChildren-=animal.getNumberOfChild();
        }
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
        return this.numbersOfPlaces - getNumberOfAnimals();
    }
    public float getAverageEnergy(){
        return this.allEnergy/this.numberOfAnimalsLife;
    }
    public float getAverageLifeTime(){
        return this.allLifeTime/this.numberOfAnimalsLife;
    }
    public float getAverageNumberOfChildren(){
        return this.allNumberOfChildren/this.numbersOfPlaces;
    }

}
