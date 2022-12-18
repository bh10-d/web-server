package org.example;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequest {
    static final File WEB_ROOT =new File(".");
    static final String DIR = "system/";
    static final String DEFAULT_FILE ="system/index.html";
    static final String FILE_NOT_FOUND ="system/404.html";
    static final String METHOD_NOT_SUPPORTED = "system/not_supported.html";
    static final String CRLF = "\n\r";

    InputStream inputStream;
    BufferedOutputStream dataOut;
    PrintWriter out;
    String fileRequest;
    Socket socket;
    String request;

    public HttpRequest(InputStream inputStream, PrintWriter out, BufferedOutputStream dataOut, Socket socket, byte[] buffer) {
        this.inputStream = inputStream;
        this.out = out;
        this.dataOut = dataOut;
        this.socket = socket;
        this.request = new String(buffer);
    }

    public void informationRequest(JTextArea serverLogText){
        InetSocketAddress ClientIp = (InetSocketAddress) socket.getRemoteSocketAddress();
        String clientIpAddress = ClientIp.getAddress().getHostAddress();
        String text = serverLogText.getText() + "\n" + "Ip: " + clientIpAddress + "\n" +"Port: "+ ClientIp.getPort() + "\n" + request;
        serverLogText.setText(text);
    }

    public void handleRequest(){
        try {
            StringTokenizer parse = new StringTokenizer(request); // get the http method of client
            String method = parse.nextToken().toUpperCase(); // GET
            fileRequest = parse.nextToken().toLowerCase();
            if (!method.equals("GET") && !method.equals("HEAD")) {
                // tra ve messages k ho tro cho client
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED); // web_root: la dau . duong dan file
                int fileLength = (int) file.length();
                // tra ve noi dung cho client
                byte[] fileData = readFileData(file, fileLength);
                // tra ve HTTP headers
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Content-length: " + fileLength);
                out.println();
                out.flush();
                // tra ve data cho client
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();
            } else {
                if (fileRequest.endsWith("/")) {
                    fileRequest += DEFAULT_FILE;
                }
                File file = new File(WEB_ROOT, fileRequest);
                int fileLength = (int) file.length();
                System.out.println(fileLength);
//            String content = getContentType(fileRequest);

                if (method.equals("GET")) {
                    byte[] fileData = readFileData(file, fileLength);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }
            }
        }catch (FileNotFoundException e){
            try{
                fileNotFound(out, dataOut, fileRequest);
            }catch (IOException ex){
                System.out.println(ex);
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }


    public byte[] readFileData(File file, int fileLength) throws IOException{
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try{
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        }finally {
            if(fileIn != null)
                fileIn.close();
        }
        return fileData;
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequest) throws IOException{
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file,fileLength);
        out.println("HTTP/1.1 404 File Not Found");
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }

    public String buidFileRequestByFileExtension(String dir, String fileRequest){
        String fileExtension = fileRequest.split("\\.")[1];
        String buildFile = "";
        if(fileExtension.equals("css")) {
            buildFile = DIR + dir + "/css/" + fileRequest;
        }
        if(fileExtension.equals("js")) {
            buildFile = DIR + dir + "/js/" + fileRequest;
        }
        if(fileExtension.equals("jpg") || fileExtension.equals("jpeg")
                || fileExtension.equals("gif") || fileExtension.equals("png")) {
            buildFile = DIR + dir + "/image/" + fileRequest;
        }
        System.out.println(fileExtension);
        return buildFile;
    }


}
