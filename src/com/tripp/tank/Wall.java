package com.tripp.tank;

import java.awt.*;

public class Wall extends GameObject {
    public final static int WIDTH = ResourceMgr.getResourceMgr().walls[0].getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().walls[0].getHeight();

    public Rectangle rectangle = new Rectangle();

    public Wall(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        rectangle.x = x;
        rectangle.y = y;
        rectangle.width = w;
        rectangle.height = h;
        // rectangle.width = WIDTH;
        // rectangle.height = HEIGHT;

        GameModel.getInstance().add(this);
    }

    @Override
    public void paint(Graphics g) {
        // g.drawImage(ResourceMgr.getResourceMgr().walls[0], this.x, this.y, null);
        Color c = g.getColor();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, w, h);
        g.setColor(c);
    }
}
