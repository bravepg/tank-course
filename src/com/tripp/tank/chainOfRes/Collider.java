package com.tripp.tank.chainOfRes;

import com.tripp.tank.GameObject;

public interface Collider {
    boolean collide(GameObject o1, GameObject o2);
}
