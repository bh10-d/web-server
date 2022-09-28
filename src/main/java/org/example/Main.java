package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Server starting ....");
        Server h = new Server();
        h.serverLogText.setLineWrap(true);
        System.out.println(h.serverLogText);
        h.setContentPane(h.panelMain);
        h.setTitle("Hello");
        h.setSize(600, 600);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        h.serverLogText.setEditable(false);
    }
}