package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Utils.Vector2D;

import java.util.List;

public interface LiveObject extends MapObject , StatisticalObject{
    void move(Vector2D position);
    void eat(int energy);
    void fullRotate();
    List<Integer> multiplicationWith(LiveObject other, int minMutation, int maxMutation, boolean isSwap);
    void multiplicationLostEnergy(int energy);
    void addParent(LiveObject one, LiveObject two);
}
