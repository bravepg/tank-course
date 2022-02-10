package com.tripp.tank.decorator;

import com.tripp.tank.GameObject;

import java.awt.*;

public class RectDecorator extends GameObjectDecorator {

    public RectDecorator(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void paint(Graphics g) {
        gameObject.paint(g);

        // 动态改变
        this.x = gameObject.x;
        this.y = gameObject.y;

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawRect(gameObject.x, gameObject.y, gameObject.w, gameObject.h);
        g.setColor(c);
    }
}
