package com.tripp.tank.abstractFactory;

import com.tripp.tank.GameModel;
import com.tripp.tank.ResourceMgr;

import java.awt.*;

public class RectExplode extends BaseExplode {
    public final static int WIDTH = ResourceMgr.getResourceMgr().explodes[0].getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().explodes[0].getHeight();

    private int size = 0;

    public RectExplode(int x, int y) {
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
