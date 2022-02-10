package com.tripp.tank.abstractFactory;

import com.tripp.tank.*;
import com.tripp.tank.net.BulletNewMsg;
import com.tripp.tank.net.Client;

import java.util.UUID;

public class DefaultFactory extends GameFactory {
    @Override
    public BaseBullet createBullet(UUID uuid, int x, int y, Direction direction, Group group) {
        Bullet bullet = new Bullet(uuid, x, y, direction, group);
        Client.INSTANCE.send(new BulletNewMsg(bullet));
        return bullet;
    }

    @Override
    public BaseExplode createExplode(int x, int y) {
        return new Explode(x, y);
    }

    @Override
    public BaseTank createTank(int x, int y, Direction direction, Group group) {
        return new Tank(x, y, direction, group);
    }
}
