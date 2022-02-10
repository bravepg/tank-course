package com.tripp.tank;

import com.tripp.tank.abstractFactory.BaseExplode;

import java.awt.*;

public class Explode extends BaseExplode {
    public final static int WIDTH = ResourceMgr.getResourceMgr().explodes[0].getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().explodes[0].getHeight();

    private int size = 0;

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        this.w = WIDTH;
        this.h = HEIGHT;
    }

    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.getResourceMgr().explodes[size++], this.x, this.y, null);
        if (this.size >= ResourceMgr.getResourceMgr().explodes.length) {
            this.size = 0;
            GameModel.getInstance().remove(this);
        }
    }
}
