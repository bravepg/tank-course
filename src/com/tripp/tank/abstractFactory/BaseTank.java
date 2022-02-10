package com.tripp.tank.abstractFactory;

import com.tripp.tank.GameObject;
import com.tripp.tank.Group;

import java.awt.*;

public abstract class BaseTank extends GameObject {
    public Rectangle rectangle = new Rectangle();

    public abstract void paint(Graphics g);

    public abstract Group getGroup();

    public abstract void die();

    public abstract int getX();

    public abstract int getY();
}
