package shadow.utility;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;

import java.io.File;
import java.nio.file.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {
    Drives info = new Drives();
    private Stack<String> navBackStk;
    private Stack<String> navForwardStk;
    private Stack<String> toolTipBackStk;
    private Stack<String> toolTipForwardStk;

    //parentPane
    @FXML
    private AnchorPane parentPane;

    //children

    @FXML
    private Label driveInfo;
    @FXML
    private TextField dirField;
    @FXML
    private ListView<String> driveList;

    @FXML
    private ListView<String> contentList;

    @FXML
    private ListView <String> searchList;
    @FXML
    private ListView<String> searchListView;

    @FXML
    private TextField searchField;
    @FXML
    private Button backBtn;
    @FXML
    private  Button fwdBtn;

    @FXML
    private Button searchBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
        displaySystemDrives();
        tipBack();
        tipForward();
        parentProperties();
        //listeners
        driveList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String prevValue, String newValue) {
                System.out.println("SELECTED : " + newValue);
                pathInfo(newValue);
                navigateDir(newValue);
            }
        });

        contentList.setOnMouseClicked(event ->{
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                String selectedItem = String.valueOf(contentList.getSelectionModel().getSelectedItem());
                Tooltip backBtnTip = new Tooltip("Back to " + selectedItem);
                backBtn.setTooltip(backBtnTip);
                openDirectory(selectedItem);
            }
        });

    }


    private void pathInfo(String path){
        String dirNameRegex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(dirNameRegex);
        Matcher pathMatcher = pattern.matcher(path);
        if(pathMatcher.find()){
            String extractedPath = pathMatcher.group(1);
            String showPath = extractedPath + "\\ " + info.getUsableSpace(extractedPath) + " GB Free of " + info.getTotalSpace(extractedPath) + " GB";
            driveInfo.setText(showPath);
        }
    }
    private  void openDirectory(String selectedItem){
        contentList.setCellFactory(param -> new ListCell<String>(){
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setText(null);
                    setGraphic(null);
                }else{
                    setText(item);
                    imageView.setImage(new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\explorer.png"));
                    setGraphic(imageView);
                }

            }
        });

        ObservableList<String> rootList = FXCollections.observableArrayList();
            String item = dirField.getText()+"\\"+selectedItem;
            toolTipBackStk.push(selectedItem);
            toolTipForwardStk.clear();
            navBackStk.push(dirField.getText());
            navForwardStk.clear();
            dirField.setText(item);
            System.out.println("Path :" + item);
            File directory = new File(item);
            if (directory.isDirectory()){
                System.out.println("DIRECTORY CLICKED : " + item);
                File [] files = directory.listFiles();
                if(files != null){
                    rootList.clear();
                    for (File object : files){
                        rootList.add(object.getName());
                    }
//                    contentList = new ListView<>(rootList);
                    contentList.setItems(rootList);
                }
            }else if(directory.isFile()){
                System.out.println("FILE CLICKED : " + item);
            }
    }
    @FXML
    private void searchDirectory(){
        Search searchTask = new Search(dirField.getText(), searchField.getText(), searchListView);
        searchTask.setOnRunning((taskRunning) -> {
            searchBtn.setDisable(true);
        });
        searchTask.setOnSucceeded((taskFinished) -> {
            searchTask.getSearchList().setItems(searchTask.searchResult());
            searchTask.getSearchList().setVisible(true);
            searchBtn.setDisable(false);
        });
        ExecutorService executeService = Executors.newFixedThreadPool(1);
        executeService.execute(searchTask);
        executeService.shutdown();
    }

    @FXML
    private void goBack(){
        if(!navBackStk.isEmpty()){
            navForwardStk.push(dirField.getText());
            Tooltip forwardBtnTip = new Tooltip("Forward to " + dirField.getText());
            fwdBtn.setTooltip(forwardBtnTip);
            String prevDir = navBackStk.pop();
            dirField.setText(prevDir);
            updateContentList(prevDir);
        }else{
            System.out.println("Stack is still empty");
        }
    }
    @FXML
    private void goForward(){
        if(!navForwardStk.isEmpty()){
            navBackStk.push(dirField.getText());
            String nextDir = navForwardStk.pop();
            dirField.setText(nextDir);
            updateContentList(nextDir);
        }
    }
    private void tipForward(){
        if(!toolTipForwardStk.isEmpty()){
            Tooltip forward = new Tooltip("Forward to " + toolTipForwardStk.pop());
            backBtn.setTooltip(forward);
        }
    }
    private void tipBack(){
        if(!toolTipBackStk.isEmpty()){
            Tooltip backBtnTip = new Tooltip("Back to " +toolTipBackStk.pop());
            backBtn.setTooltip(backBtnTip);
        }
    }
    private  void navigateDir(String dirPath){
        String dirNameRegex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(dirNameRegex);
        Matcher matchDir = pattern.matcher(dirPath);
        if(matchDir.find()){
            String newDir = matchDir.group(1);
            navBackStk.push(dirField.getText());
            navForwardStk.clear();
            dirField.setText(newDir+"\\");
            updateContentList(newDir+"\\");
        }
    }
    private void updateContentList(String dirPath){
            contentList.setCellFactory(param -> new ListCell<String>(){
                private final ImageView icon = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null){
                        setText(null);
                        setGraphic(null);
                    }else {
                        setText(item);
                        icon.setImage(new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\explorer.png"));
                    }
                    setGraphic(icon);
                }
            });

            ObservableList<String> rootList = FXCollections.observableArrayList();
            File dir = new File (dirPath);
            File[] files = dir.listFiles();
            if(files != null){
                rootList.clear();
                for (File item : files){
                    if(item.isFile()){
//                        System.out.println("FILE: " + item.getName());
                    }else if (item.isDirectory()){
//                        System.out.println("DIRECTORY: " + item.getName());
                    }
                    rootList.add(item.getName());
                }
                    contentList.setItems(rootList);
            }
    }
    private void displaySystemDrives(){
        //set hdd icon
        driveList.setCellFactory(param -> new ListCell<String>() {
            private final ImageView iconView = new ImageView();
            @Override
            protected void updateItem(String driveName, boolean empty){
                super.updateItem(driveName, empty);
                if(empty){
                    getStyleClass().setAll("list-cell", "empty-row");
                }
                else if(empty || driveName == null){
                    setText(null);
                    setGraphic(null);
                }else{
                    setText(driveName);
                    Image iconImg = new Image("D:\\CODING PROJECTS\\ExpFX\\src\\main\\resources\\shadow\\expfx\\icons\\hdd.png");
                    iconView.setImage(iconImg);
                    getStyleClass().setAll("list-cell", "non-empty-row");
                    setGraphic(iconView);
                }
            }
        });

        Iterable<FileStore> fStore = FileSystems.getDefault().getFileStores();
        List<String> dNames = new ArrayList<>();
        dNames.clear();
        for (FileStore fileStore : fStore){
            String root = fileStore.toString();
            dNames.add(root);
        }
        ObservableList <String> rootList = FXCollections.observableArrayList(dNames);
        driveList.getItems().addAll(rootList);
    }
    private void init(){
        Tooltip searchTip = new Tooltip("Search Here");
        searchField.setTooltip(searchTip);
        driveInfo.setText("");
        navBackStk = new Stack<>();
        navForwardStk = new Stack<>();
        toolTipBackStk = new Stack<>();
        toolTipForwardStk = new Stack<>();
        searchList = new ListView<>();
        Tooltip backBtnTip = new Tooltip("Back to " + dirField.getText());
        backBtn.setTooltip(backBtnTip);
        updateContentList(dirField.getText());
    }
    private void parentProperties(){
        double width = parentPane.getWidth();
        double height = parentPane.getHeight();
        double sx = searchField.getLayoutX();
    }

}