package org.example;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListenThread extends Thread{
    public ArrayList<ServerListenThread> serverListenThreads = new ArrayList<>();
    private ServerSocket serverSocket;
    private JTextArea serverLogText;
    private JTextField clientTotalText;
    public ServerListenThread(ServerSocket serverSocket, JTextArea serverLogText, JTextField clientTotalText) throws Exception {
        this.serverSocket = serverSocket;
        this.serverLogText = serverLogText;
        this.clientTotalText = clientTotalText;
    }
    @Override
    public void run() {
        serverListenThreads.add(this);
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
//                System.out.println("Connection accepted: " + socketAddress.getAddress()
//                        .getHostAddress());
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket, serverLogText);
                httpConnectionWorkerThread.start();
                clientTotalText.setText(String.valueOf(serverListenThreads.size()));
            }
//            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
