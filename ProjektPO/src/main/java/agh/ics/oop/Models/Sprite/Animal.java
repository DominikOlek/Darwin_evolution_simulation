package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.MapDirection;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.GenerateID;
import agh.ics.oop.Models.Utils.Vector2D;

import java.util.List;
import java.util.Objects;

import static agh.ics.oop.Models.Utils.GenerateID.GetID;

public class Animal implements MapObject{
    private Vector2D position;
    private MapDirection direction;
    private Dna dna;
    private int energy;
    private int ID;
    private int currentDay = 0;
    private int numberOfChild = 0;
    private final boolean isAlive = false;
    private final int dayOfDead = -1;

    public Animal(Vector2D position,Dna dna,int energy){
        this.position = position;
        this.dna = dna;
        this.energy = energy;
        this.direction = MapDirection.NORTH;
        this.ID = GetID();
        nextDay();
    }


    //cykl ruchu zwierzecia i przypisanie pozycji na podany z mapy
    public void move(Vector2D position){
        try {
            if(energy<=0)
                return;
            this.position = position;
            energy -= 1;
            currentDay+=1;
            nextDay();
        }catch (Exception e){
            System.out.println(e+ " "+position);
        }
    }

    public void eat(int energy){
        if (energy>=0) this.energy += energy;
    }

    public void rotate(int number){
        if(number >0) this.direction = MapDirection.values()[(this.direction.ordinal()+number)%8];
    }

    public void nextDay(){ //Przejscie do kolejnego dnia
        rotate(dna.getActual()); // wykonanie obrotu na podstawie dna aktualnego
        dna.nextDay();
    }
    public void fullRotate(){
        rotate(4);
    }

    public List<Integer> multiplicationWith(Animal other){
        if (!other.getPosition().equals(position)) throw new IllegalArgumentException("Position is not the same as the position "+other.getPosition()+" "+position);
        return dna.multiplicationWith(other.dna, (float)energy /(energy+other.energy));
    }

    public int getEnergy() {
        return energy;
    }

    public void multiplicationLostEnergy(int energy) {
        this.energy -= energy;
        this.numberOfChild ++;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public Dna getDna() {
        return dna;
    }
    public int getNumberOfChild(){
        return numberOfChild;
    }
    public Vector2D getPosition() {
        return position;
    }
    public MapDirection getDirection() {
        return direction;
    }

    //Wymagane do hashset i treeset


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return ID == animal.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    @Override
    public int compareTo(Animal o) {
        if (this.energy > o.energy) {
            return -1;
        } else if (this.energy < o.energy) {
            return 1;
        } else {
            if (this.currentDay > o.currentDay) {
                return -1;
            } else if (this.currentDay < o.currentDay) {
                return 1;
            } else {
                if (this.numberOfChild > o.numberOfChild) {
                    return -1;
                } else if (this.numberOfChild < o.numberOfChild) {
                    return 1;
                } else {
                    return (Math.random()<0.5) ? -1:1;
                }
            }
        }
    }


    @Override
    public String toString() {
        return "Animal{" +
                "position=" + position +
                ", direction=" + direction +
                ", dna=" + dna +
                ", energy=" + energy +
                '}';
    }
}
