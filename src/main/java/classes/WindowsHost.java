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
            connection.execute(CmdLine.build("ifconfig"));

        }catch (Exception e){
            System.out.println("test");
        }
    }
}
