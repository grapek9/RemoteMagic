package classes;

import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;

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

    public Connection(String host,String user,String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    private void checkAccessibility(){
        try {
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
        }else{
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
                }
            }
        }
    }
    public void estabilishConnection(){
        resolveOS();
        if(os.equals("lx")){
            ConnectionOptions options = new ConnectionOptions();
            options.set(ADDRESS,host);
            options.set(USERNAME,user);
            options.set(PASSWORD,password);
            options.set(OPERATING_SYSTEM, UNIX);
            options.set(CONNECTION_TYPE,SFTP);
            con = Overthere.getConnection("ssh",options);

        }else if(os.equals("win")){
            ConnectionOptions options = new ConnectionOptions();
            options.set(ADDRESS,host);
            options.set(USERNAME,user);
            options.set(PASSWORD,password);
            options.set(OPERATING_SYSTEM, WINDOWS);
            options.set(CONNECTION_TYPE, WINRM_INTERNAL);
            con = Overthere.getConnection("cifs",options);
        }else{
            System.out.println("Unresolved system.");
        }
    }

}
