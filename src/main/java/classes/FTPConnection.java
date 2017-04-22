package classes;
import classes.File;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPConnection {
    private String server;
    private String path;
    private String username;
    private String password = "";
    private FTPClient ftpClient = null;
    private List<File> fileList;
    public FTPConnection(String server,String path,String username,String password){
        this.server = server;
        this.path = path;
        this.username = username;
        this.password = password;
    }
    public String checkConnection(){
        if(ftpClient != null){
            if(ftpClient.isConnected()){
                return "Connection already estabilished.";
            }else {
                estabilishConnection();
                return "Estabilishing new connection";
            }

        }else{
            estabilishConnection();
            if(ftpClient.isConnected()){
                return "New connection estabilished.";
            }else {
                return "Could not estabilish connection to ftp server";
            }
        }
    }
    private void estabilishConnection() {
        try{
            ftpClient = new FTPClient();
            ftpClient.connect(server);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login(username,password);
            ftpClient.setConnectTimeout(20000);
            System.out.println(ftpClient.getReplyCode());
        }catch (IOException excetpion){
            excetpion.printStackTrace();
        }

    }

    private void generateFileList(){
        try {
            fileList = new ArrayList<File>();
            FTPFile[] ftpFiles = ftpClient.listFiles(path);
            System.out.println(ftpFiles.length);
            for (FTPFile ftpFile : ftpFiles) {
                if(ftpFile.isFile()) {
                    fileList.add(new File(path + "/" + ftpFile.getName()));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> getFileList(){
        generateFileList();
        return fileList;
    }
    public void disconnect(){
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPath(String path) {
        this.path = path;
    }
}
