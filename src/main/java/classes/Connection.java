package classes;

import Interfaces.Host;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.*;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.UNIX;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionType.WINRM_INTERNAL;
import static com.xebialabs.overthere.ssh.SshConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.ssh.SshConnectionType.SFTP;

public class Connection {
    private String host;
    private String user;
    private String password;
    private String os = null;
    private OverthereConnection con = null;
    private boolean turnedOn = false;
    private Host oshost = null;
    @FXML private TextArea logs;

    public Connection(String host,String user,String password, TextArea logs) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.logs = logs;
    }

    private void checkAccessibility(){
        try {
            logs.appendText("[INFO] Checking if HostOS is turned ON\n");
            InetAddress server = Inet4Address.getByName(host);
            server.isReachable(10000);
            turnedOn = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            turnedOn = false;
        } catch (IOException e) {
            e.printStackTrace();
            turnedOn = false;
        }
    }
    private void resolveOS(){
        checkAccessibility();
        if (!turnedOn){
            System.out.println("Host might be turned off");
            logs.appendText("[ERROR] HostOS might be turned OFF\n");
        }else{
            logs.appendText("[INFO] Resolving HostOS type\n");
            try{
                Socket sock_win = new Socket(host,5985);
                os = "win";
                sock_win.close();
            }catch (IOException win_ex){
                try{
                    Socket sock_lx = new Socket(host,22);
                    os = "lx";
                    sock_lx.close();
                }catch (IOException lx_ex){
                    System.out.println("Could not resolve OS");
                    logs.appendText("[ERROR] Could not resolve HostOS type\n");
                }
            }
        }
    }
    public void estabilishConnection(){
        resolveOS();
        logs.appendText("[INFO] Estabilishing connection\n");
        if(os.equals("lx")){
            ConnectionOptions options = new ConnectionOptions();
            options.set(ADDRESS,host);
            options.set(USERNAME,user);
            options.set(PASSWORD,password);
            options.set(OPERATING_SYSTEM, UNIX);
            options.set(CONNECTION_TYPE,SFTP);
            con = Overthere.getConnection("ssh",options);
            System.out.println("Linux remote connection estabilished");
            logs.appendText("[INFO] Linux remote connection estabilished.\n");
            oshost = new LinuxHost(con, logs);

        }else if(os.equals("win")){
            ConnectionOptions options = new ConnectionOptions();
            options.set(ADDRESS,host);
            options.set(USERNAME,user);
            options.set(PASSWORD,password);
            options.set(OPERATING_SYSTEM, WINDOWS);
            options.set(CONNECTION_TYPE, WINRM_INTERNAL);
            con = Overthere.getConnection("cifs",options);
            System.out.println("Windows remote connection estabilished");
            logs.appendText("[INFO] Windows remote connection estabilished\n");
            oshost = new WindowsHost(con, logs);
        }else{
            System.out.println("Unresolved system .");
            logs.appendText("[ERROR] Could not estabilish connection due unresolved HostOS type.\n");
        }
    }
    public void downloadFile(String ftpServer,String ftpPath){
        oshost.sendCommand(ftpServer,ftpPath);
    }

}
