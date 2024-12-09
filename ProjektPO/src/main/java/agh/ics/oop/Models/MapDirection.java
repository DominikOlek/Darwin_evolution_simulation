package agh.ics.oop.Models;

public enum MapDirection {
    NORTH, EAST, SOUTH, WEST;

    public String toString() {
        return switch (this) {
            case NORTH -> "Polnoc";
            case EAST -> "Wschod";
            case SOUTH -> "Poludnie";
            case WEST -> "Zachod";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Vector2D toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2D(0, 1);
            case WEST -> new Vector2D(-1, 0);
            case SOUTH -> new Vector2D(0, -1);
            case EAST -> new Vector2D(1, 0);
        };
    }
}