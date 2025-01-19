package agh.ics.oop;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Utils.*;
import agh.ics.oop.UI.SimulationApp;
import javafx.application.Application;


public class Main {
    public static void main(String[] args) {
        try {
            Application.launch(SimulationApp.class, args);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
