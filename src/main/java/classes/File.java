package classes;

/**
 * Created by danie on 02-Apr-17.
 */
public class File {
    private String path;
    private String name;
    public File(String path){
        this.path = path;
        this.name = path.split("/")[path.split("/").length-1];
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
