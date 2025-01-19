package agh.ics.oop.UI;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Maps.WorldMapI;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.DayStatisticsExport;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.Simulation.Simulation;
import agh.ics.oop.Simulation.SimulationEngine;
import agh.ics.oop.Simulation.StartSimulation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class ManagerPresenter extends StartSimulation {
    private static final double CELL_WIDTH = 30;
    private static final double CELL_HEIGHT = 30;
    WorldMapI worldMap;

    public void setWorldMap(WorldMapI worldMap) {
        this.worldMap = worldMap;
    }

    public void setPrimaryStage(Stage primaryStage) {
        primaryStage.setOnCloseRequest(this::handleCloseRequest);
        primaryStage.setTitle("Manager");

    }

    private void handleCloseRequest(WindowEvent event) {
        engine.awaitSimulationsEnd();
    }
    @FXML
    public Label moveLabel;


    @FXML
    public TextField stepField;

    public ToggleButton whichMutation;

    public ToggleButton whichGrow;

    public ToggleButton save;

    @FXML
    private Slider widthSlider,heightSlider,startAnimalSlider, lenGenSlider, energyFromEatSlider,
            minMutationSlider, maxMutationSlider, startGrassSlider,growGrassSlider, startEnergySlider,multiEnergySlider,adultEnergySlider;
    @FXML
    private Label widthLabel,heightLabel, startAnimalLabel,lenGenLabel, energyFromEatLabel, minMutationLabel,
            maxMutationLabel, resultLabel,startGrassLabel,growGrassLabel, startEnergyLabel,multiEnergyLabel,adultEnergyLabel;

    @FXML
    public void initialize() {
        setupSlider(widthSlider, widthLabel);
        setupSlider(heightSlider, heightLabel);
        setupSlider(lenGenSlider, lenGenLabel);
        setupSlider(startAnimalSlider, startAnimalLabel);
        setupSlider(energyFromEatSlider, energyFromEatLabel);
        setupSlider(minMutationSlider, minMutationLabel);
        setupSlider(maxMutationSlider, maxMutationLabel);
        setupSlider(startGrassSlider, startGrassLabel);
        setupSlider(growGrassSlider, growGrassLabel);
        setupSlider(startEnergySlider, startEnergyLabel);
        setupSlider(multiEnergySlider, multiEnergyLabel);
        setupSlider(adultEnergySlider, adultEnergyLabel);


        minMutationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > maxMutationSlider.getValue()) {
                maxMutationSlider.setValue(newVal.doubleValue());
            }
        });

        maxMutationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() < minMutationSlider.getValue()) {
                minMutationSlider.setValue(newVal.doubleValue());
            }
        });

        lenGenSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxMutationSlider.setMax(newVal.doubleValue());
            minMutationSlider.setMax(newVal.doubleValue());
        });

        widthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            startAnimalSlider.setMax((newVal.intValue()*heightSlider.getValue())-1);
            startGrassSlider.setMax((newVal.intValue()*heightSlider.getValue())-1);
            growGrassSlider.setMax((newVal.intValue()*heightSlider.getValue())-1);
        });
        heightSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            startAnimalSlider.setMax((widthSlider.getValue()*newVal.intValue())-1);
            startGrassSlider.setMax((widthSlider.getValue()*newVal.intValue())-1);
            growGrassSlider.setMax((widthSlider.getValue()*newVal.intValue())-1);
        });

        adultEnergySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            multiEnergySlider.setMax(adultEnergySlider.getValue());
        });
    }

    private void setupSlider(Slider slider, Label label) {
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.valueOf(newVal.intValue())));
    }

    @FXML
    public void handleSubmit() {
        int width = (int) widthSlider.getValue();
        int height = (int) heightSlider.getValue();
        int lenGen = (int) lenGenSlider.getValue();
        int startAnimals = (int) startAnimalSlider.getValue();
        int energyFromEat = (int) energyFromEatSlider.getValue();
        int minMutation = (int) minMutationSlider.getValue();
        int maxMutation = (int) maxMutationSlider.getValue();
        int startEnergy = (int) startEnergySlider.getValue();
        int startGrass = (int) startGrassSlider.getValue();
        int growGrass = (int) growGrassSlider.getValue();
        int multiEnergy = (int) multiEnergySlider.getValue();
        int adultEnergy = (int) adultEnergySlider.getValue();

        if (minMutation > maxMutation) {
            resultLabel.setText("Błąd: Minimalna długość musi być mniejsze niż maksymalna długość!");
        } else {
            Boundary size = new Boundary(new Vector2D(0,0),new Vector2D(width,height));
            MapSettings settings = new MapSettings(whichMutation.isSelected(), lenGen, energyFromEat, adultEnergy, multiEnergy, startGrass, startAnimals ,growGrass, startEnergy, minMutation, maxMutation,whichGrow.isSelected(),save.isSelected());
            resultLabel.setText("Utworzono");

            onSimulationStartClicked(size,settings);
        }
    }

    public void handleToggleMutation(ActionEvent actionEvent) {
        whichMutation.setText(!whichMutation.isSelected() ? "Normal" : "Swap");
    }

    public void handleToggleGrow(ActionEvent actionEvent) {
        whichGrow.setText(!whichGrow.isSelected() ? "Normal" : "Big grass");
    }

    public void handleToggleSave(ActionEvent actionEvent) {
        save.setText(!save.isSelected() ? "No" : "Yes");
    }
}
