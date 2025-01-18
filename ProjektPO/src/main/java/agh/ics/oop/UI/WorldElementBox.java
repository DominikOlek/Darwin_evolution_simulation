package agh.ics.oop.UI;

import agh.ics.oop.Models.Enums.FileNames;
import agh.ics.oop.Models.Sprite.MapObject;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Objects;

public class WorldElementBox {
    private final VBox vbox =new VBox();
    private final MapObject worldElement;
    private static final Image strong = new Image(FileNames.Strong.toString());
    private static final Image medium = new Image(FileNames.Medium.toString());
    private static final Image weak = new Image(FileNames.Weak.toString());
    private static final Image lgrass = new Image(FileNames.Lgrass.toString());
    private static final Image sgrass = new Image(FileNames.Sgrass.toString());
    private final ImageView imageView;
    private final Label label = new Label();

    public WorldElementBox(MapObject element) {
        this.worldElement = element;
        imageView = new ImageView();
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        vbox.getChildren().addAll(imageView, label);
        vbox.setAlignment(Pos.CENTER);
        label.setFont(new Font("Arial", 9));

    }
    public VBox get(){

        switch (worldElement.getFileName()){
            case FileNames.Strong:imageView.setImage(strong);break;
            case FileNames.Medium:imageView.setImage(medium);break;
            case FileNames.Weak:imageView.setImage(weak);break;
            case FileNames.Lgrass:imageView.setImage(lgrass);break;
            case FileNames.Sgrass:imageView.setImage(sgrass);break;
        }

        return vbox;
    }

    public void setLabel(String text){
        label.setText(text);
    }
}
