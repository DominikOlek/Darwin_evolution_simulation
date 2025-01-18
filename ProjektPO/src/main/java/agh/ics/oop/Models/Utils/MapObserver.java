package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Maps.MainMap;

public interface MapObserver {
    void mapChanged(MainMap worldMap, String message);
}
