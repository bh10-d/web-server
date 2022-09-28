package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server extends JFrame {
    JButton startServerBtn;
    JButton stopServerButton;
    JTextField portServerText;
    private JTextField clientTotalText;
    JPanel panelMain;
    JTextArea serverLogText;
    private ServerSocket serverSocket;
    private ServerListenThread serverListenThread;
    HttpServer httpServer;

    public Server() {

        startServerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServerBtn.setEnabled(false);
                stopServerButton.setEnabled(true);
                String textServerLog = serverLogText.getText();
                System.out.println(serverLogText);
                int port = Integer.parseInt(portServerText.getText());
                try {
                    serverSocket = new ServerSocket(port);
                    serverListenThread = new ServerListenThread(serverSocket, serverLogText, clientTotalText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {

                    httpServer = new HttpServer(serverSocket, serverListenThread);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                httpServer.startServer();

                serverLogText.setText(textServerLog + "Server Started Running With Port: " + port + "\n");

            }
        });
        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServerButton.setEnabled(false);
                startServerBtn.setEnabled(true);
                String textServerLog = serverLogText.getText();
                serverLogText.setText(textServerLog + "Server Has Stoppted" + "\n");

                httpServer = new HttpServer(serverSocket, serverListenThread);
                httpServer.closeServer();
            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public void setTextArea(String text) {

        String currentText = serverLogText.getText();
        System.out.println(this.serverLogText.getText());
        serverLogText.setEditable(false);
        serverLogText.setText("1234");

    }

}