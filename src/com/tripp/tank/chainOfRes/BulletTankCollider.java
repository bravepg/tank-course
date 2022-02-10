package com.tripp.tank.chainOfRes;

import com.tripp.tank.*;
import com.tripp.tank.net.Client;
import com.tripp.tank.net.TankDieMsg;

public class BulletTankCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Bullet  && o2 instanceof Tank) {
            Bullet bullet = (Bullet) o1;
            Tank tank = (Tank) o2;

            System.out.println("uuid" + bullet.getPlayerID() + "===" + tank.getUuid());

            if (bullet.getPlayerID() == tank.getUuid()) {
                return false;
            }

            // if (bullet.group == tank.getGroup()) {
            //     return false;
            // }

            if (bullet.rectangle.intersects(tank.rectangle)) {
                bullet.die();
                tank.die();
                Client.INSTANCE.send(new TankDieMsg(bullet.getUuid(), tank.getUuid()));
                int eX = tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
                int eY = tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
                GameModel.getInstance().add(GameModel.getInstance().gameFactory.createExplode(eX, eY));
                return true;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet) {
            return collide(o2, o1);
        }
        return false;
    }
}
