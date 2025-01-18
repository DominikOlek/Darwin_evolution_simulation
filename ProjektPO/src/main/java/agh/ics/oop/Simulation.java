package agh.ics.oop;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.DayStatisticsExport;
import agh.ics.oop.Models.Utils.MapSettings;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Simulation implements Runnable {
    private final Object lock = new Object();

    private MainMap map;
    private boolean running = true;
    private DayStatisticsExport saveToFile = null;

    public Simulation(MainMap map, DayStatisticsExport saveToFile) {
        this.map = map;
        this.saveToFile = saveToFile;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                while (map.checkRun()) {
                    try {
                        while (!running) {
                            lock.wait();
                        }
                        map.doDay();
                        if (saveToFile != null) {
                            saveToFile.toCsv(map);
                        }
                        Thread.sleep(50);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            running = false;
            Thread.currentThread().interrupt();
        }
    }

    public void stop() throws InterruptedException {
        Thread.currentThread().interrupt();
        pause();
    }

    public void pause() throws InterruptedException {
        running = false;
    }

    public void resume() throws InterruptedException {
        synchronized (lock) {
            running = true;
            lock.notifyAll();
        }
    }

    //run
    //runDay
}
