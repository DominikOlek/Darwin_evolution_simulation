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

    public Animal(Vector2D position,Dna dna,int energy){
        this.position = position;
        this.dna = dna;
        this.energy = energy;
        this.direction = MapDirection.NORTH;
        this.ID = GetID();
    }

    public Vector2D getPosition() {
        return position;
    }
    public MapDirection getDirection() {
        return direction;
    }

    //cykl ruchu zwierzecia i przypisanie pozycji na podany z mapy
    public void move(Vector2D position){
        this.position = position;
        nextDay();
    }

    public void eat(int energy){
        this.energy += energy;
    }

    private void rotate(int number){
        this.direction = MapDirection.values()[(this.direction.ordinal()+number)%8];
    }

    public void nextDay(){ //Przejscie do kolejnego dnia
        rotate(dna.getActual()); // wykonanie obrotu na podstawie dna aktualnego
        energy -= 1;
        dna.nextDay();
    }
    public void fullRotate(){
        rotate(4);
    }

    public List<Integer> multiplicationWith(Animal other){
        return dna.multiplicationWith(other.dna, (float) energy /(energy+other.energy));
    }

    public int getEnergy() {
        return energy;
    }

    public void multiplicationLostEnergy(int energy) {
        this.energy -= energy;
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
            return 1;
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
