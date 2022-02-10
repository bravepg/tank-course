package com.tripp.tank.decorator;

import com.tripp.tank.GameObject;

import java.awt.*;

public abstract class GameObjectDecorator extends GameObject {
    GameObject gameObject;

    public GameObjectDecorator(GameObject gameObject) {
        this.gameObject = gameObject;
        this.w = gameObject.w;
        this.h = gameObject.h;
    }

    @Override
    public abstract void paint(Graphics g);
}
