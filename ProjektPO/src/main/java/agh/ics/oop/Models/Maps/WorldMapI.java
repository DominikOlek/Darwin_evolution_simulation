package agh.ics.oop.Models.Maps;

import agh.ics.oop.Models.Sprite.LiveObject;
import agh.ics.oop.Models.Sprite.MapObject;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapObserver;
import agh.ics.oop.Models.Utils.Vector2D;

import java.util.Set;

public interface WorldMapI {
    void doDay();

    Set<LiveObject> copyAnimalCollection();

    boolean canMoveTo(Vector2D pos);

    boolean addAnimal(Vector2D pos, LiveObject animal);

    boolean removeAnimal(Vector2D pos, LiveObject animal);

    void addObserver(MapObserver observer);

    void removeObserver(MapObserver observer);

    Set<LiveObject> getAnimalsAt(Vector2D pos);

    MapObject getGrassAt(Vector2D pos);

    boolean isGrassAt(Vector2D pos);

    boolean checkRun();

    Boundary getSize();
}