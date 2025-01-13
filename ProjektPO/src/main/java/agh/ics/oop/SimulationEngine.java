package agh.ics.oop;

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
        //threadPool.shutdown();
        //awaitSimulationsEnd();
    }

    public void awaitSimulationsEnd(){
        try{
            threadPool.shutdown();
            if(!threadPool.awaitTermination(5, TimeUnit.SECONDS)){
                threadPool.shutdownNow();
            }
        }catch (InterruptedException e) {
            System.out.print("Interrupted w await" +e.getMessage());
        }
    }
}
