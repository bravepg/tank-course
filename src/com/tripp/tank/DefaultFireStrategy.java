package com.tripp.tank;

import com.tripp.tank.decorator.LineDecorator;
import com.tripp.tank.decorator.RectDecorator;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank tank) {
        int bX = tank.x + (Tank.WIDTH - Bullet.WIDTH) / 2;
        int bY = tank.y + (Tank.HEIGHT - Bullet.HEIGHT) / 2;

        // GameModel.getInstance().add(new LineDecorator(new RectDecorator(GameModel.getInstance().gameFactory.createBullet(bX, bY, com.tripp.tank.direction, com.tripp.tank.group))));

        GameModel.getInstance().gameFactory.createBullet(tank.getUuid(), bX, bY, tank.direction, tank.group);
    }
}
