package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Maps.WorldMapI;

public interface MapObserver {
    public void mapChanged(WorldMapI worldMap, String message);
}
