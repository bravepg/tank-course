package com.tripp.tank;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {
    public int x, y, w, h;
    public abstract void paint(Graphics g);
}
