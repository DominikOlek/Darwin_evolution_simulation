package agh.ics.oop.Simulation;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.DayStatisticsExport;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.UI.SimulationPresenter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class StartSimulation {
    protected List<Simulation> simList = new ArrayList<>();
    protected SimulationEngine engine = new SimulationEngine(simList);
    protected DayStatisticsExport exporter = new DayStatisticsExport();

    protected void onSimulationStartClicked(Boundary size, MapSettings settings) {
        try {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
                BorderPane root1 = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Simulation Running");
                stage.setScene(new Scene(root1));
                SimulationPresenter presenter = loader.getController();

                MainMap map = new MainMap(size,settings);
                map.addObserver(presenter);
                presenter.setWorldMap(map);
                presenter.setPrimaryStage(stage);
                Simulation newSimulation;
                if(settings.saveToFile()){
                    newSimulation = new Simulation(map,exporter);
                }else{
                    newSimulation = new Simulation(map,null);
                }
                presenter.setSimulation(newSimulation);
                simList.add(newSimulation);
                engine.runAsyncInThreadPool();
                stage.show();
            } catch(Exception e) {
                e.printStackTrace();
            }

        }catch(IllegalArgumentException e){
            System.out.println("User bad request: "+e.getMessage());
        }
    }

}
