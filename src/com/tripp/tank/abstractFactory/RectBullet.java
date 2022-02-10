package com.tripp.tank.abstractFactory;

import com.tripp.tank.*;

import java.awt.*;
import java.util.UUID;

public class RectBullet extends BaseBullet {
    public Rectangle rectangle = new Rectangle();
    private boolean living = true;
    private final Direction direction;
    private final Group group;

    private final static int SPEED = 8;
    public final static int WIDTH = ResourceMgr.getResourceMgr().bulletD.getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().bulletD.getHeight();

    public RectBullet(UUID playerID, int x, int y, Direction direction, Group group) {
        this.x = x;
        this.y = y;
        this.w = WIDTH;
        this.h = HEIGHT;
        this.direction = direction;
        this.group = group;

        rectangle.x = x;
        rectangle.y = y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        GameModel.getInstance().add(this);
    }

    public void paint(Graphics g) {
        if (!living) {
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 20, 20);
        g.setColor(c);

        move();
    }

    private void move() {
        if (!this.living) {
            return;
        }
        switch (direction) {
            case UP:
                this.y -= SPEED;
                break;
            case RIGHT:
                this.x += SPEED;
                break;
            case DOWN:
                this.y += SPEED;
                break;
            case LEFT:
                this.x -= SPEED;
                break;
        }

        rectangle.x = this.x;
        rectangle.y = this.y;

        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            this.living = false;
            GameModel.getInstance().remove(this);
        }
    }

    public void collideWidth(BaseTank tank) {
        if (this.group == tank.getGroup()) {
            return;
        }

        if (tank.rectangle != null && rectangle.intersects(tank.rectangle)) {
            this.die();
            tank.die();
            int eX = tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
            int eY = tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
            GameModel.getInstance().add(GameModel.getInstance().gameFactory.createExplode(eX, eY));
        }

    }

    private void die() {
        this.living = false;
        GameModel.getInstance().remove(this);
    }
}
