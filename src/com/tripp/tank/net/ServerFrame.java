package com.tripp.tank.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {
    public static final ServerFrame INSTANCE = new ServerFrame();

    TextArea textAreaLeft = new TextArea();
    TextArea textAreaRight = new TextArea();
    Server server = new Server();

    public ServerFrame() {
        this.setSize(1600, 600);
        this.setLocation(300, 30);

        Panel panel = new Panel(new GridLayout(1, 2));
        panel.add(textAreaLeft);
        panel.add(textAreaRight);

        this.add(panel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void updateServerMsg(String message) {
        this.textAreaLeft.setText(this.textAreaLeft.getText() + System.getProperty("line.separator") + message);
    }

    public void updateClientMsg(String message) {
        this.textAreaRight.setText(this.textAreaRight.getText() + System.getProperty("line.separator") + message);
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.serverStart();
    }
}
