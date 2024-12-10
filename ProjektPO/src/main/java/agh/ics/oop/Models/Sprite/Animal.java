package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.Vector2D;

public class Animal implements MapObject{
    private Vector2D position;
    private Dna dna;
    private int energy;

    public Animal(Vector2D position){
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void move(){

    }

    public void eat(){

    }




}
