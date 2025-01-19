package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.FileNames;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.UI.WorldElementBox;

public class Grass implements MapObject{
    private final Vector2D position;
    private final int energyValue;
    private final WorldElementBox graph;

    public Grass(Vector2D position, int energyValue) {
        this.position = position;
        this.energyValue = energyValue;
        graph = new WorldElementBox(this);
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public FileNames getFileName() {
        return FileNames.Lgrass;
    }
    @Override
    public WorldElementBox getGraph() {
        return graph;
    }

    @Override
    public String toString() {
        return "*";
    }

}