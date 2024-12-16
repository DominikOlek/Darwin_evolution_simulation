package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Utils.Vector2D;

public class Grass{
    private final Vector2D position;
    public Grass(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }
}