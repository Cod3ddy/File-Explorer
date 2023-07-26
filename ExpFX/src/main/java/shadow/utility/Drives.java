package shadow.utility;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class Drives {
    private final double gb = 1073741824;
    public String getTotalSpace(String dir){
        double gigabytes = 0.0;
        String sFormat = "";
        FileStore fileStore = null;

        Path path  = Paths.get(dir);
        try{
            fileStore = Files.getFileStore(path);
            long bytes = fileStore.getTotalSpace();
            gigabytes = (double) bytes /gb;
            DecimalFormat dFormat = new DecimalFormat("#.#");
            sFormat = dFormat.format(gigabytes);
        }catch(IOException e){
            System.err.println("Error : " + e.getMessage());
        }
        return sFormat;
    }

    public String getUsableSpace(String dir){
        double gigabytes = 0.0;
        String sFormat = "";
        FileStore fileStore = null;
        Path path = Paths.get(dir);
        try{
            fileStore = Files.getFileStore(path);
            long bytes = fileStore.getUsableSpace();
            gigabytes = (double) bytes / gb;
            DecimalFormat dFormat = new DecimalFormat("#.#");
            sFormat = dFormat.format(gigabytes);
        }catch (IOException e){
            System.err.println("Error : "  + e.getMessage());
        }
        return sFormat;
    }
}
