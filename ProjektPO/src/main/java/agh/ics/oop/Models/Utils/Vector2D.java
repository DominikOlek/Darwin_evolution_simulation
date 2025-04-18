package agh.ics.oop.Models.Utils;

import java.util.Objects;

public class Vector2D {
    private int x;
    private int y;

    public Vector2D() {

    }
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(%d,%d)".formatted(x,y);
    }

    public boolean precedes(Vector2D other){
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2D other){
        return x >= other.x && y >= other.y;
    }

    public Vector2D add(Vector2D other){
        return new Vector2D(x + other.x, y + other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2d = (Vector2D) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y) {
        this.y=y;
    }
}
