package classes;

import Interfaces.Host;
import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class LinuxHost implements Host {
    private OverthereConnection connection = null;
    @FXML private TextArea logs;
    public LinuxHost(OverthereConnection connection, TextArea logs) {
        this.connection = connection;
        this.logs = logs;
    }

    public void sendCommand(String ftpServer,String ftpPath) {
        try{
            logs.appendText("[INFO] Uploading file "+ftpPath.split("/")[ftpPath.split("/").length-1]+" to remote host.\n");
            System.out.println("wget -O"+" "+"/root/Desktop/"+ftpPath.split("/")[ftpPath.split("/").length-1]+ " " +"ftp://"+ftpServer+ftpPath);
            connection.execute(CmdLine.build("wget","-O","/root/Desktop/"+ftpPath.split("/")[ftpPath.split("/").length-1],"ftp://"+ftpServer+"/"+ftpPath));

        }catch (Exception e){
            logs.appendText("[ERROR] Could not upload file "+ftpPath.split("/")[ftpPath.split("/").length-1]);
        }
    }
}

