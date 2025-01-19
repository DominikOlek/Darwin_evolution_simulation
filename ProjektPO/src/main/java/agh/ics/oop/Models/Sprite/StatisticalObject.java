package agh.ics.oop.Models.Sprite;

import agh.ics.oop.Models.Enums.MapDirection;
import agh.ics.oop.Models.Utils.Dna;
import agh.ics.oop.Models.Utils.Observer;
import agh.ics.oop.Models.Utils.Vector2D;

public interface StatisticalObject {
    void setObserver(Observer observer);
    void removeObserver();
    int getHowManyEaten();
    void addAllChild();
    int getAllChild();
    void isDead();
    boolean isAlive();
    int getCurrentDay();
    int getNumberOfChild();
    int getEnergy();
    Dna getDna();
    MapDirection getDirection();
    Vector2D getPosition();
}
