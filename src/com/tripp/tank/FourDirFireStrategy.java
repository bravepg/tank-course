package com.tripp.tank;

public class FourDirFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank tank) {
        int bX = tank.x + (Tank.WIDTH - Bullet.WIDTH) / 2;
        int bY = tank.y + (Tank.HEIGHT - Bullet.HEIGHT) / 2;

        Direction[] directions = Direction.values();

        for (Direction direction: directions) {
            GameModel.getInstance().gameFactory.createBullet(tank.getUuid(), bX, bY, direction, tank.group);
        }
    }
}
