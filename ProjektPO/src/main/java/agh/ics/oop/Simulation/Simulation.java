package agh.ics.oop.Simulation;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Maps.WorldMap;
import agh.ics.oop.Models.Utils.DayStatisticsExport;

import java.io.IOException;

public class Simulation implements Runnable {
    private final Object lock = new Object();

    private final WorldMap map;
    private boolean running = true;
    private DayStatisticsExport saveToFile = null;
    private boolean paused = false;

    public Simulation(MainMap map, DayStatisticsExport saveToFile) {
        this.map = map;
        this.saveToFile = saveToFile;
    }

    @Override
    public void run() {
        try {
            while (running && map.checkRun() && !Thread.currentThread().isInterrupted()) {
                synchronized (lock) {
                    while (paused) {
                        lock.wait();
                    }
                }
                map.doDay();
                if (saveToFile != null) {
                    try {
                        saveToFile.toCsv(map);
                    } catch (IOException e) {
                        System.err.println("Write to CSV error");
                    }
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            running = false;
        }
    }

    public void stop() throws InterruptedException {
        running = false;
        Thread.currentThread().interrupt();
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void pause() throws InterruptedException {
        paused = true;
        map.setBreak(true);
    }

    public void resume() throws InterruptedException {
        synchronized (lock) {
            paused = false;
            map.setBreak(false);
            lock.notifyAll();
        }
    }
}
