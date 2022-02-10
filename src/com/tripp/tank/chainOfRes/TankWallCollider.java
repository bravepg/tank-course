package com.tripp.tank.chainOfRes;

import com.tripp.tank.Direction;
import com.tripp.tank.GameObject;
import com.tripp.tank.Tank;
import com.tripp.tank.Wall;

public class TankWallCollider implements Collider {

    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Wall && o2 instanceof Tank) {
            Wall wall = (Wall) o1;
            Tank tank = (Tank) o2;

            if (wall.rectangle.intersects(tank.rectangle)) {
                // com.tripp.tank.setDirection(com.tripp.tank.getReverseDirection());
                tank.x = tank.prevX;
                tank.y = tank.prevY;
            }
        } else if (o1 instanceof Tank && o2 instanceof Wall) {
            return collide(o2, o1);
        }
        return false;
    }
}
