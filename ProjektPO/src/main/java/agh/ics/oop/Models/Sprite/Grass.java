package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Utils.Vector2D;

public class Grass{
    private final Vector2D position;
    private final int energyValue;

    public Grass(Vector2D position, int energyValue) {
        this.position = position;
        this.energyValue = energyValue;
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public String toString() {
        return "*";
    }

}