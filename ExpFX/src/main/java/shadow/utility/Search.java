package shadow.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import shadow.expfx.Main;

import java.io.File;
import java.util.List;


public class Search implements Runnable{
   MainController mc = new MainController();
   private  String path;
   private String searchInput;

   private ListView <String> searchList;
   public Search(String path, String searchInput, ListView <String> searchList){
       this.path = path;
       this.searchInput = searchInput;
       this.searchList = searchList;
   }
   public void setSearchList(ListView<String> searchList){
       this.searchList = searchList;
   }
   public void setSearchInput(String searchInput){
       this.searchInput = searchInput;
   }
   public void setPath(String path){
       this.path = path;
   }
   public String getPath(){
       return path;
   }
   public String getSearchInput(){
       return searchInput;
   }

   public ListView<String> getSearchList(){
       return searchList;
   }

    @Override
    public void run() {
        System.out.println("DIR: " + getPath());
        System.out.println("Running thread");
        SearchDirectory sd = new SearchDirectory();
        ObservableList<String> resultFiles;
        ObservableList<String> resultList = FXCollections.observableArrayList();
        if(!getPath().isBlank()){
            System.out.println("Searching......");
            File selectedPath = new File(getPath());
            resultFiles = sd.searchInDrive(selectedPath, getSearchInput());
            if(resultFiles.isEmpty()){
                System.out.println(getSearchInput() + " was not found in " + selectedPath);
                double height = getSearchList().getHeight() * 0.2;
                getSearchList().setPrefHeight(height);
                resultList.add(getSearchInput()+ " was not found in " + selectedPath);
                getSearchList().setItems(resultList);
                getSearchList().setDisable(true);
                getSearchList().setVisible(true);
            }else{
                System.out.println("MATCHING FILES FOUND IN " + selectedPath);
                for(String filepath : resultFiles){
                    System.out.println(filepath);
                    resultList.add(filepath);
                }
                getSearchList().setItems(resultList);
                getSearchList().setVisible(true);
                System.out.println("ITEMS : " + searchList.getItems());
            }
        }else{
            System.out.println("Dir Field is empty");
        }
    }
}
