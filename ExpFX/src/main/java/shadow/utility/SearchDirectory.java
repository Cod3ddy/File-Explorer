package shadow.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;

public class SearchDirectory {
    public  ObservableList<String> searchInDrive(File drive, String targetFileName){
        ObservableList<String> resultFiles = FXCollections.observableArrayList();
        searchDir(drive, targetFileName, resultFiles);
        return resultFiles;
    }

    public  static void  searchDir(File directory, String targetFileName, ObservableList<String> resultFiles){
       if(directory.isDirectory()){
           File [] files = directory.listFiles();
           if(files!=null){
               for (File file : files){
                   if(file.isFile() && file.getName().equals(targetFileName)){
                       resultFiles.add(file.getAbsolutePath());
                   }else if (file.isDirectory()){
                       searchDir(file, targetFileName, resultFiles);
                   }
               }
           }
       }
    }
}
