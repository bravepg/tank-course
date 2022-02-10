package com.tripp.tank.abstractFactory;

import com.tripp.tank.*;

import java.util.UUID;

public class RectFactory extends GameFactory {
    @Override
    public BaseBullet createBullet(UUID uuid, int x, int y, Direction direction, Group group) {
        return new RectBullet(uuid, x, y, direction, group);
    }

    @Override
    public BaseExplode createExplode(int x, int y) {
        return new RectExplode(x, y);
    }

    @Override
    public BaseTank createTank(int x, int y, Direction direction, Group group) {
        return new RectTank(x, y, direction, group);
    }
}
