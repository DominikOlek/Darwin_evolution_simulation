package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Enums.ChangeType;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Sprite.StatisticalObject;

import java.util.Observable;

public interface Observer {
    public void update(StatisticalObject animal, ChangeType changeType);
}
