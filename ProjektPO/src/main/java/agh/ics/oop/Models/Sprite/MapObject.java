package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.FileNames;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.UI.WorldElementBox;

public interface MapObject{
    Vector2D getPosition();
    String toString();
    FileNames getFileName();
    WorldElementBox getGraph();
    int getEnergy();
}
