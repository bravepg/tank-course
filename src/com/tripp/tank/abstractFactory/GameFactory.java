package com.tripp.tank.abstractFactory;

import com.tripp.tank.Direction;
import com.tripp.tank.Group;

import java.util.UUID;

public abstract class GameFactory {
    public abstract BaseBullet createBullet(UUID uuid, int x, int y, Direction direction, Group group);
    public abstract BaseExplode createExplode(int x, int y);
    public abstract BaseTank createTank(int x, int y, Direction direction, Group group);
}
