package agh.ics.oop;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Sprite.Animal;
import agh.ics.oop.Models.Utils.*;
import agh.ics.oop.UI.SimulationApp;
import javafx.application.Application;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            try {
                Application.launch(SimulationApp.class, args);
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
                return;
            }
            /*
            Boundary b = new Boundary(new Vector2D(0, 0), new Vector2D(5, 5));
            AnimalStatistics stat = new AnimalStatistics();
            //TO SAMO TOADULT A TOMULTIPLICATION
            MapSettings s = new MapSettings(false,5, 0, 1, 6, 5, 0,20 ,0, 20,0,0);
            MainMap map = new MainMap(b,s);
            Simulation one = new Simulation(map);
            Simulation two = new Simulation(map);
            List<Simulation> sims = new LinkedList<Simulation>(){{add(two);add(one);}};
            SimulationEngine eng=  new SimulationEngine(sims);
            eng.runAsyncInThreadPool();
            Thread.sleep(1000);
            one.pause();
            two.pause();
            Thread.sleep(5000);
            one.resume();
            two.resume();
            Thread.sleep(5000);
            eng.awaitSimulationsEnd();

            // MainMap mapa = new MainMap(b, s, 20);

            /*Set<Animal> sss = mapa.copyCollection();
            Animal obs = sss.stream().findFirst().get();
            obs.setObserver(stat);

            DayStatisticsExport export = new DayStatisticsExport();

            for (int i = 0; i < 20; i++) {
                System.out.println("----------- Dzien "+(i+1));
                mapa.doDay();

                export.toCsv(mapa);
                /*System.out.println("Av LifeTime: "+mapa.getAverageLifeTime()+ " Av Energy: "+mapa.getAverageEnergy() + " Av ChildNumber: "+mapa.getAverageNumberOfChildren() + " All animals: "+mapa.getNumberOfAnimals() + " Free places: "+mapa.getNumberOfFreePlaces());
                List<Map.Entry<Dna,Integer>> best= mapa.getTopDna(5);
                if(best!=null){
                    System.out.println("Rank dna: " +best);
                }

            } */
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        //System.out.println(mapa.toString());
    }
}
