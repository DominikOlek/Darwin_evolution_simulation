package agh.ics.oop.UI;

import agh.ics.oop.Models.Utils.Boundary;
import agh.ics.oop.Models.Utils.MapSettings;
import agh.ics.oop.Models.Utils.Vector2D;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.scenario.Settings;
import javafx.scene.control.Slider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SettingsSaver {
    ObjectMapper objectMapper = new ObjectMapper();
    ManagerPresenter managerPresenter;

    public SettingsSaver(ManagerPresenter managerPresenter) {
        this.managerPresenter = managerPresenter;
    }

    public void saveSettingsToFile(MapSettings settings, Vector2D size, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Settings");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.set("main", objectMapper.valueToTree(settings));
                rootNode.set("bound", objectMapper.valueToTree(size));
                objectMapper.writeValue(file, rootNode);
            } catch (IOException e) {
                System.out.println("Error Saving Settings" + e.getMessage());
            }
        }
    }

    public void loadSettingsFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Settings");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                ObjectNode rootNode = objectMapper.readValue(file, ObjectNode.class);
                MapSettings main = objectMapper.treeToValue(rootNode.get("main"), MapSettings.class);
                Vector2D bound = objectMapper.treeToValue(rootNode.get("bound"), Vector2D.class);
                managerPresenter.applySettings(main,bound);
            } catch (IOException e) {
                System.out.println("Error Loading Settings" + e.getMessage());
            }
        }
    }
}
