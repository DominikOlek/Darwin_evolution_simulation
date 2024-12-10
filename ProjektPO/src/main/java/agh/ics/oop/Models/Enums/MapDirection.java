package agh.ics.oop.Models.Enums;

import agh.ics.oop.Models.Utils.Vector2D;

public enum MapDirection {
    NORTH,NORTHEAST, EAST,SOUTHEAST,SOUTH,SOUTHWEST, WEST,NORTHWEST;

    public String toString() {
        return switch (this) {
            case NORTH     -> "North";
            case NORTHEAST -> "North East";
            case EAST      -> "East";
            case SOUTHEAST -> "South East";
            case SOUTH     -> "South";
            case SOUTHWEST -> "South West";
            case WEST      -> "West";
            case NORTHWEST -> "North West";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> NORTHWEST;
            case NORTHWEST -> WEST;
            case WEST -> SOUTHWEST;
            case SOUTHWEST -> SOUTH;
            case SOUTH -> SOUTHEAST;
            case SOUTHEAST -> EAST;
            case EAST -> NORTHEAST;
            case NORTHEAST -> NORTH;
        };
    }

    public Vector2D toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2D(0, 1);
            case NORTHWEST -> new Vector2D(-1, 1);
            case WEST -> new Vector2D(-1, 0);
            case SOUTHWEST -> new Vector2D(-1, -1);
            case SOUTH -> new Vector2D(0, -1);
            case SOUTHEAST -> new Vector2D(1, -1);
            case EAST -> new Vector2D(1, 0);
            case NORTHEAST -> new Vector2D(1, 1);
        };
    }
}