package org.example;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HttpConnectionWorkerThread extends Thread{

    private Socket socket;
    private JTextArea serverLogText;
    HttpConnectionWorkerThread(Socket socket, JTextArea serverLogText) {
        this.socket = socket;
        this.serverLogText = serverLogText;
    }
    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            byte[] buffer = new byte[10000];
            inputStream.read(buffer);
            InetAddress myIp = InetAddress.getLocalHost();
            String request = new String(buffer);
            String text =serverLogText.getText() + "\n" + myIp + "\n" + request;
            serverLogText.setText(text);
            String htmls = "<html><head><title>Web Server Basic</title></head><body><h1>Hello World</h1></body></html>";
            final String CRLF = "\n\r";

            String response =
                    "HTTP/1.1.200 OK" + CRLF +
                            "Content-Length: " + htmls.getBytes().length + CRLF +
                            CRLF +
                            htmls +
                            CRLF + CRLF;
            outputStream.write(response.getBytes());
            inputStream.close();
            outputStream.close();
            socket.close();

            sleep(3000);
            System.out.println("Connecting Processing Finished ...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(outputStream != null) {
                    outputStream.close();
                }
                if(socket != null) {
                    socket.close();
                }
            } catch (Exception e) {

            }
        }

    }
}
