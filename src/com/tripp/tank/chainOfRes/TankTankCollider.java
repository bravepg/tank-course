package com.tripp.tank.chainOfRes;

import com.tripp.tank.Direction;
import com.tripp.tank.GameObject;
import com.tripp.tank.Tank;

public class TankTankCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Tank) {
            Tank tank1 = (Tank) o1;
            Tank tank2 = (Tank) o2;

            if (tank1.rectangle.intersects(tank2.rectangle)) {
                tank1.x = tank1.prevX;
                tank1.y = tank1.prevY;
                // 不用改变方向也行，有一定概率会自动改变
                // tank1.setDirection(tank1.getReverseDirection());

                // tank2.setDirection(tank2.getReverseDirection());
                tank2.x = tank2.prevX;
                tank2.y = tank2.prevY;
            }
        }
        return false;
    }
}
