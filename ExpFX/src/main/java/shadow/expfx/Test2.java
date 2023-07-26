package shadow.expfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;

public class Test2 extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a sample list of files and directories
        ObservableList<File> items = FXCollections.observableArrayList(
                new File("path_to_file1.txt"),
                new File("path_to_file2.txt"),
                new File("path_to_directory1"),
                new File("path_to_directory2")
        );
        ObservableList<File> rootList = FXCollections.observableArrayList();

        File dir = new File ("C:\\");
        File[] files = dir.listFiles();
//        System.out.println("Files : " + files[1].isDirectory());
        if(files != null){
            rootList.clear();
            for (File item : files){
                if(item.isFile()){
//                        System.out.println("FILE: " + item.getName());
                }else if (item.isDirectory()){
//                        System.out.println("DIRECTORY: " + item.getName());
                }
                rootList.add(new File(item.getName()));
            }
        }


        // Create the ListView and set the items to it
        ListView<File> listView = new ListView<>(rootList);

        // Set a custom cell factory to display icons
        listView.setCellFactory(param -> new ListCell<File>() {
            private final ImageView imageView = new ImageView();
            int count;
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item.getName());
                    File path = new File("C://");
                    File [] dir = path.listFiles();
//                    System.out.println(iter.isDirectory());
//                    System.out.println(dir);
//                    System.out.println("Item : " + dir[1].toString());
                        count  = 0;
                        if (count < 1){
                            System.out.println(count + " FILE : " + item);
                        }

                    if (dir[1].toString().equals(item)) {
                        imageView.setImage(new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\explorer.png"));
                    } else if (item.isFile()){
                        imageView.setImage(new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\file.png"));
                    }
                    setGraphic(imageView);
                }
                count++;
            }
        });

        // Create a layout and add the ListView to it
        StackPane root = new StackPane();
        root.getChildren().add(listView);

        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("ListView with Icons Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
