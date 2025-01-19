package agh.ics.oop.UI;

import agh.ics.oop.Models.Maps.MainMap;
import agh.ics.oop.Models.Maps.WorldMapI;
import agh.ics.oop.Models.Sprite.LiveObject;
import agh.ics.oop.Models.Sprite.MapObject;
import agh.ics.oop.Models.Utils.AnimalStatistics;
import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapObserver;
import agh.ics.oop.Models.Utils.Vector2D;
import agh.ics.oop.Simulation.Simulation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;

public class SimulationPresenter implements MapObserver {
    private static final double CELL_WIDTH = 30;
    private static final double CELL_HEIGHT = 30;
    public Button changeStateBtn;
    MainMap worldMap;
    Simulation simulation;
    AnimalStatistics animalStatistics = new AnimalStatistics(this);
    LiveObject animal;
    Popup popup = new Popup();
    Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setOnCloseRequest(this::handleCloseRequest);
        animalStat.setHgap(5);
        animalStat.setVgap(5);
        animalStat.setPadding(new Insets(0, 0, 0, 0));
        dayStat.setHgap(5);
        dayStat.setVgap(5);
        dayStat.setPadding(new Insets(0, 0, 0, 0));

    }
    private void handleCloseRequest(WindowEvent event) {
        try {
            simulation.stop();
        }catch (InterruptedException e) {
            System.out.println("Simulation interrupted");
        }
    }
    private void setPop(double x, double y,Set<LiveObject> animals) {
        popup.getContent().clear();
        popup.setAutoHide(true);

        VBox popupContent = new VBox();
        popupContent.setStyle("-fx-background-color: rgb(228, 234, 186); -fx-border-color: rgb(132, 145, 47); -fx-padding: 3;");

        popupContent.getChildren().addAll(
                new Label("Select: ")
        );
        for (LiveObject ani : animals) {
            Label lab = new Label("Animal "+ani.getCurrentDay()+" old");
            lab.setOnMouseClicked(e -> {
                if (animal != null) animal.removeObserver();
                animal = ani;
                animal.setObserver(animalStatistics);
                getAnimalStat();
            });
            popupContent.getChildren().addAll(
                lab
            );
        }

        popup.getContent().add(popupContent);
        popup.show(primaryStage,x,y);
    }

    public void setWorldMap(MainMap worldMap) {
        this.worldMap = worldMap;
        ID.setText("Mapa o ID: "+worldMap.getID() + " Dzień: "+worldMap.getCurrentDay());
    }
    public void setSimulation(Simulation simulation) {this.simulation = simulation;}
    @FXML
    public GridPane gridPane;

    @FXML
    public GridPane animalStat;
    @FXML
    public GridPane dayStat;
    @FXML
    public Label ID;


    private void clearGrid() {
        gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        popup.getContent().clear();
    }

    private void drawLayout(){
        try {
            ID.setText("Mapa o ID: "+worldMap.getID() + " Dzień: "+worldMap.getCurrentDay());
            Boundary size = worldMap.getSize();
            int sizeX = size.upperRight().getX() - size.lowerLeft().getX();
            int sizeY = size.upperRight().getY() - size.lowerLeft().getY();

            for (int i = 0; i <= sizeX + 1; i++) {
                int realX = size.lowerLeft().getX() + i - 1;
                gridPane.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
                for (int j = 0; j <= sizeY + 1; j++) {
                    VBox vbox = null;
                    Label lab = new Label("");
                    int realY = size.upperRight().getY() - j + 1;
                    if (i == 0) {
                        gridPane.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
                        lab = j == 0 ? new Label("y/x") : new Label(realY + "");
                    } else {
                        if (j == 0) {
                            lab = new Label(realX + "");
                        } else {
                            Vector2D pos = new Vector2D(realX, realY);
                            Set<LiveObject> atPos = worldMap.getAnimalsAt(pos);
                            if (atPos != null) {
                                LiveObject animalatPos = atPos.stream().findFirst().get();
                                WorldElementBox box = animalatPos.getGraph();
                                box.setLabel("" + atPos.size());
                                vbox = box.get();
                                if (atPos.size() > 1) {
                                    vbox.setOnMouseClicked(e -> {
                                        setPop(e.getScreenX(), e.getScreenY(), atPos);
                                    });
                                } else {
                                    vbox.setOnMouseClicked(e -> {
                                        if (animal != null) animal.removeObserver();
                                        animal = animalatPos;
                                        animal.setObserver(animalStatistics);
                                        getAnimalStat();
                                    });
                                }
                            } else {
                                MapObject grass = worldMap.getGrassAt(pos);
                                if (grass != null) {
                                    vbox = grass.getGraph().get();
                                } else {
                                    lab = new Label("");
                                }
                            }
                        }
                    }
                    GridPane.setHalignment(vbox != null ? vbox : lab, HPos.CENTER);
                    gridPane.add(vbox != null ? vbox : lab, i, j);
                }
            }
        }catch (IllegalArgumentException e){
            System.out.println("Error with multiple element");
        }
    }

    private void getMapStat(){
        if (!dayStat.getChildren().isEmpty()) {
            dayStat.getChildren().retainAll(dayStat.getChildren().getFirst());
            dayStat.getColumnConstraints().clear();
            dayStat.getRowConstraints().clear();
        }
        Map<String, String> stat = worldMap.getStatistics();

        setDataToArray(dayStat,stat);
    }

    public void getAnimalStat(){
        Platform.runLater(() -> {
            if (!animalStat.getChildren().isEmpty()) {
                animalStat.getChildren().retainAll(animalStat.getChildren().getFirst());
                animalStat.getColumnConstraints().clear();
                animalStat.getRowConstraints().clear();
            }

            Map<String, String> stat = animalStatistics.getStatistics();

            setDataToArray(animalStat,stat);
        });

    }

    private void setDataToArray(GridPane pane,Map<String, String> stat){
        int row = 0;
        for (Map.Entry<String, String> entry : stat.entrySet()) {
            Text keyText = new Text(entry.getKey());
            keyText.setFont(Font.font ("Verdana", 15));
            GridPane.setConstraints(keyText, 0, row);
            Text valueText = new Text(entry.getValue());
            valueText.setFont(Font.font ("Verdana", 15));
            ScrollPane scrollPane = new ScrollPane(valueText);
            scrollPane.setFitToWidth(true);
            scrollPane.setPadding(new Insets(1, 1, 1, 1));
            GridPane.setConstraints(scrollPane, 1, row);
            pane.getChildren().addAll(keyText, scrollPane);
            row++;
        }
    }

    @Override
    public void mapChanged(WorldMapI worldMap, String message) {
        Platform.runLater(() -> {
            clearGrid();
            drawLayout();
            getMapStat();
        });
    }

    @FXML
    private void changeState(ActionEvent actionEvent) {
        try {
            if (Objects.equals(changeStateBtn.getText(), "Pause")) {
                simulation.pause();
            } else {
                simulation.resume();
            }
            changeStateBtn.setText((Objects.equals(changeStateBtn.getText(), "Pause")) ? "Continue" : "Pause");
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
