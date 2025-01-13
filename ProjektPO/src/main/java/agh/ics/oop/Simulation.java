package agh.ics.oop;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapSettings;

public class Simulation implements Runnable {
    private final Object lock = new Object();

    private MainMap map;
    private boolean running = true;

    public Simulation(MapSettings settings, Boundary size,int howMany) {
        this.map = new MainMap(size,settings,howMany);
    }


    @Override
    public void run() {
        synchronized (lock) {
            while(map.checkRun()) {
                map.doDay();
                try {
                    while (!running) {
                        lock.wait();
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }
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
