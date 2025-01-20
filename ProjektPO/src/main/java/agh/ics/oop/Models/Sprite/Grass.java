package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.FileNames;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.UI.WorldElementBox;

public class Grass implements MapObject{
    private final Vector2D position;
    private final int energyValue;
    private final boolean isBig;
    private final WorldElementBox graph;

    public Grass(Vector2D position, int energyValue, boolean isBig) {
        this.position = position;
        this.energyValue = energyValue;
        this.isBig = isBig;
        graph = new WorldElementBox(this);
    }

    public boolean isBig() {
        return isBig;
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getEnergyValue() {
        return energyValue;
    }

    @Override
    public FileNames getFileName() {
        if (isBig) {
            return FileNames.Lgrass;
        } else {
            return FileNames.Sgrass;
        }
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