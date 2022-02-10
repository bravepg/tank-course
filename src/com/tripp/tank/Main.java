package com.tripp.tank;

import com.tripp.tank.net.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        TankFrame tankFrame = new TankFrame();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tankFrame.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
