package entry;


import classes.FTPConnection;
import classes.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable{
    private FTPConnection ftpServer = null;
    private String ftpUser = "anonymous";
    private String ftpPassword= "test@test.pl";
    private List<File> files = null;


    @FXML private TextField ftp;
    @FXML private TextField ftpPath;
    @FXML private TextField userftp;
    @FXML private PasswordField passwordftp;
    @FXML private CheckBox anonymouchchk;
    @FXML private Button checkconnectionftp;
    @FXML private TextField host;
    @FXML private TextField userhost;
    @FXML private PasswordField passwordhost;
    @FXML private Button checkconnectionhost;
    @FXML private Button scanfiles;
    @FXML private Button uploadfiles;
    @FXML private ListView<String> filelist = new ListView<String>();
    @FXML private TextArea logs;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        userftp.setDisable(true);
        passwordftp.setDisable(true);
        logs.setEditable(false);
        filelist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    @FXML
    private void checkFTPConnection(){
        if (ftpServer == null){
            System.out.println(ftp.getText());
            System.out.println(ftpUser+" "+ftpPassword);
            ftpServer = new FTPConnection(ftp.getText(),ftpPath.getText(),ftpUser,ftpPassword);
        }
        ftpServer.setPath(ftpPath.getText());
        logs.appendText("[INFO]"+ftpServer.checkConnection()+"\n");

    }
    @FXML
    private void exploreFTP(){
        checkFTPConnection();
        files = ftpServer.getFileList();
        System.out.println("ScanFiles");
        ObservableList<String> items = FXCollections.observableArrayList ();
        for (File file : files) {
            items.add(file.getName());
            System.out.println(file.getPath());
        }
        filelist.setItems(items);

    }
    @FXML
    private void anonymousConnection(){
        if(userftp.isDisabled()){
            userftp.setDisable(false);
            passwordftp.setDisable(false);
            ftpUser = userftp.getText();
            ftpPassword = passwordftp.getText();
            logs.appendText("[INFO] Switching ftp connection to: SECURE\n");
        }else{
            userftp.setDisable(true);
            passwordftp.setDisable(true);
            ftpUser = "anonymous";
            ftpPassword= "test@test.pl";
            logs.appendText("[INFO] Switching ftp connection to: ANONYMOUS\n");
        }
    }
    @FXML
    private void checkHostConnection(){

    }
    @FXML
    private void uploadFiles(){
        ObservableList<String> selectedFiles = filelist.getSelectionModel().getSelectedItems();
        for (String selectedFile : selectedFiles) {
            System.out.println("Selected Files : "+selectedFile);

        }

    }
}
