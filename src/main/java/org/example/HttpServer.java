package org.example;


import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {
    Server server = new Server();
    private ServerSocket serverSocket;
    private ServerListenThread serverListenThread;


    public HttpServer(ServerSocket serverSocket, ServerListenThread serverListenThread)  {
        this.serverSocket = serverSocket;
        this.serverListenThread = serverListenThread;
    }

    public void startServer() {
        System.out.println("Start Server ...");

        try {
//            Socket socket = serverSocket.accept();
            serverListenThread.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void closeServer() {
        if(serverSocket != null) {
            try {
                serverSocket.close();
                serverListenThread.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerListenThread getServerListenThread() {
        return serverListenThread;
    }

    public void setServerListenThread(ServerListenThread serverListenThread) {
        this.serverListenThread = serverListenThread;
    }
}
