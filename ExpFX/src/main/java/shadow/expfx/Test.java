package shadow.expfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) {
        ListView<String> listView = new ListView<>();
        listView.setCellFactory(param -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    // Load the icon image here (replace "icon.png" with the actual image file)
                    Image iconImage = new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\folder.png");
                    imageView.setImage(iconImage);
                    setGraphic(imageView);
                }

            }
        });

        // Add some sample items with their names and associated icon images
        listView.getItems().addAll("File 1", "File 2", "File 3", "File 4");
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("ListView with Icons Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
