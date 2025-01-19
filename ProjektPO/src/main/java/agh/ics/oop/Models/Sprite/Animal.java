package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.ChangeType;
import agh.ics.oop.Models.Enums.FileNames;
import agh.ics.oop.Models.Enums.MapDirection;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.Observer;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.UI.WorldElementBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static agh.ics.oop.Models.Utils.GenerateID.GetID;

public class Animal implements LiveObject, Comparable<Animal> {
    private Vector2D position;
    private MapDirection direction;
    private final Dna dna;
    private int energy;
    private int adultEnergy=40;
    private final int ID;
    private int currentDay = 0;
    private int numberOfChild = 0;
    private int numberOfAllChild = 0;
    private boolean isAlive = true;
    private int howManyEaten = 0;
    private final ArrayList<LiveObject> parents = new ArrayList<>();

    private Observer observer = null;
    private final WorldElementBox graph;

    public Animal(Vector2D position,Dna dna,int energy,int adultEnergy){
        this(position,dna,energy);
        this.adultEnergy= adultEnergy;
    }

    public Animal(Vector2D position,Dna dna,int energy){
        this.position = position;
        this.dna = dna;
        this.energy = energy;
        this.direction = MapDirection.NORTH;
        this.ID = GetID();
        nextDay();
        graph = new WorldElementBox(this);
    }


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
        howManyEaten++;
        animalUpdate(ChangeType.Eat);
    }

    public void rotate(int number){
        if(number >0) this.direction = MapDirection.values()[(this.direction.ordinal()+number)%8];
    }

    public void fullRotate(){
        rotate(4);
    }

    public List<Integer> multiplicationWith(LiveObject other,int minMutation,int maxMutation,boolean isSwap){
        if (!other.getPosition().equals(position))
            throw new IllegalArgumentException("Position is not the same as the position "+other.getPosition()+" "+position);
        return dna.multiplicationWith(other.getDna(), (float)energy /(energy+other.getEnergy()),minMutation,maxMutation,isSwap);
    }

    public void multiplicationLostEnergy(int energy) {
        this.energy -= energy;
        numberOfChild++;
        addAllChild();
        animalUpdate(ChangeType.Child);
    }

    public void addAllChild() {
        numberOfAllChild++;
        animalUpdate(ChangeType.AllChild);
        if (parents.size()== 2){
            parents.get(0).addAllChild();
            parents.get(1).addAllChild();
        }
    }

    public void addParent(LiveObject one, LiveObject two){
        parents.add(one);
        parents.add(two);
    }

    private void animalUpdate(ChangeType type){
        if (observer != null) observer.update(this, type);
    }

    public void setObserver(Observer observer){
        this.observer=observer;
        observer.update(this, ChangeType.Gen);
        observer.update(this, ChangeType.ActiveGen);
        observer.update(this, ChangeType.Eat);
        observer.update(this, ChangeType.Child);
        observer.update(this, ChangeType.AllChild);
    }

    private void nextDay(){
        rotate(dna.getActual());
        dna.nextDay();
        animalUpdate(ChangeType.ActiveGen);
    }

    public void removeObserver(){
        this.observer=null;
    }
    public int getAllChild() {
        return numberOfAllChild;
    }
    public int getCurrentDay() {
        return currentDay;
    }
    public int getEnergy() {
        return energy;
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
    public int getHowManyEaten(){return howManyEaten;}
    public void isDead(){
        isAlive=false;
        animalUpdate(ChangeType.Dead);
    }
    public boolean isAlive(){
        return isAlive;
    }

    @Override
    public WorldElementBox getGraph() {
        return graph;
    }

    @Override
    public FileNames getFileName() {
        if (energy > adultEnergy){
            return FileNames.Strong;
        } else if (energy > 5) {
            return FileNames.Medium;
        }else{
            return FileNames.Weak;
        }
    }

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
        return direction.toString();
    }
}
