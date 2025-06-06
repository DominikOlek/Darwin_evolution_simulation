package agh.ics.oop.Simulation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simList;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(8); ;

    public SimulationEngine(List<Simulation> simList) {
        this.simList = simList;
    }

    public void runAsyncInThreadPool(){
        for (Simulation sim : simList) {
            threadPool.submit(sim);
        }
        simList.clear();
    }

    public void awaitSimulationsEnd(){
        try{
            threadPool.shutdown();
            if(!threadPool.awaitTermination(2, TimeUnit.SECONDS)){
                threadPool.shutdownNow();
            }
        }catch (InterruptedException e) {
            System.out.print("Interrupted w await" +e.getMessage());
            e.printStackTrace();
        }
    }
}
