package xyz.prathamgandhi.globechat;


import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;

public class GetUsers extends Thread{
    URI uri = URI.create("http://192.168.29.140:5000");
    Socket socket = IO.socket(uri);

    @Override
    public void run() {
        socket.connect();
        
    }
}
