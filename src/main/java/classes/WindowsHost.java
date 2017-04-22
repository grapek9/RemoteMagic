package classes;

import Interfaces.Host;
import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class WindowsHost implements Host {
    private OverthereConnection connection = null;
    @FXML private TextArea logs;
    public WindowsHost(OverthereConnection connection, TextArea logs) {
        this.connection = connection;
        this.logs = logs;

    }

    public void sendCommand(String ftpServer,String ftpPath) {
        try{
            logs.appendText("[INFO] Uploading file "+ftpPath.split("/")[ftpPath.split("/").length-1]+" to remote host.\n");
            connection.execute(CmdLine.build("echo","open",ftpServer,">>","&echo","user","anonymous@anonymous.com",">>","ftp","&echo","binary"
                    ,">>","ftp","&echo","get",ftpPath,">>","ftp","&echo","bye",">>","ftp","&ftp","-n","-v","-s:ftp","&del","ftp"));
            connection.execute(CmdLine.build("move","/Y",ftpPath.split("/")[ftpPath.split("/").length-1],"C:\\Users\\%username%\\Desktop\\"));

        }catch (Exception e){
            logs.appendText("[ERROR] Could not upload file "+ftpPath.split("/")[ftpPath.split("/").length-1]);
        }
    }
}
