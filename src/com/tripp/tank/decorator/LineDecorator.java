package com.tripp.tank.decorator;

import com.tripp.tank.GameObject;

import java.awt.*;

public class LineDecorator extends GameObjectDecorator {

    public LineDecorator(GameObject gameObject) {
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
        g.drawLine(gameObject.x, gameObject.y, gameObject.x + gameObject.w, gameObject.y + gameObject.h);
        g.setColor(c);
    }
}
