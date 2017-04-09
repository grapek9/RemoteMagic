package classes;

import Interfaces.Host;


public class LinuxHost implements Host {
    public void sendCommand() {
        System.out.println("Linux Command sended");
    }
}

