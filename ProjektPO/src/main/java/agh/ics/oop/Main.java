package agh.ics.oop;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.Models.Utils.Vector2D;

public class Main {
    public static void main(String[] args) {
        try {
            Boundary b = new Boundary(new Vector2D(0, 0), new Vector2D(5, 5));
            //TO SAMO TOADULT A TOMULTIPLICATION
            MapSettings s = new MapSettings(5, 0, 1, 5, 3, 0, 0, 20);
            MainMap mapa = new MainMap(b, s, 20);
            for (int i = 0; i < 20; i++) {
                System.out.println("----------- Dzien "+i);
                mapa.doDay();
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        //System.out.println(mapa.toString());
    }
}
