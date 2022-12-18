package org.example;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private Socket socket;
    private JTextArea serverLogText;
    InputStream inputStream;
    BufferedOutputStream dataOut;
    PrintWriter out;
    byte[] buffer = new byte[10000];

    HttpConnectionWorkerThread(Socket socket, JTextArea serverLogText) throws IOException {
        this.socket = socket;
        this.serverLogText = serverLogText;
        inputStream = socket.getInputStream();
        dataOut = new BufferedOutputStream(socket.getOutputStream());
        out = new PrintWriter(socket.getOutputStream());
        inputStream.read(buffer);
    }
    @Override
    public void run() {
        try {
            HttpRequest httpRequest = new HttpRequest(inputStream, out, dataOut, socket, buffer);
            httpRequest.informationRequest(serverLogText);
            httpRequest.handleRequest();

            inputStream.close();
            out.close();
            dataOut.close();
            socket.close();
            sleep(3000);
            System.out.println("Connecting Processing Finished ...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    // doc file
//    private byte[] readfileData(File file, int filelength) throws IOException{
//        FileInputStream fileIn = null;
//        byte[] fileData = new byte[filelength];
//
//        try{
//            fileIn = new FileInputStream(file);
//            fileIn.read(fileData);
//        }finally {
//            if(fileIn != null)
//                fileIn.close();
//        }
//        return fileData;
//    }
//
//    // tra ve loai co ho tro
//    private String getContentType(String fileRequested){
//        if(fileRequested.endsWith(".htm")||fileRequested.endsWith(".html")){
//            return "text/html";
//        }else{
//            return "text/plain";
//        }
//    }
//
//    // file k tim thay || file khong ton tai
//    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequest) throws IOException{
//        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
//        int fileLength = (int) file.length();
//        String content = "text/html";
//        byte[] fileData = readfileData(file,fileLength);
//
//        out.println("HTTP/1.1 404 File Not Found");
//        out.println("Content-length: " + fileLength);
//        out.println();
//        out.flush();
//
//        dataOut.write(fileData, 0, fileLength);
//        dataOut.flush();
//
//        if(verbose){
//            System.out.println("File "+fileRequest+" not found");
//        }
//    }

}
