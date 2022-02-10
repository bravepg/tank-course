package com.tripp.tank.abstractFactory;

import com.tripp.tank.*;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class RectTank extends BaseTank {

    public Direction direction;
    public Group group;
    private FireStrategy fireStrategy;
    private final static int SPEED = 5;
    public final static int WIDTH = ResourceMgr.getResourceMgr().tankD.getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().tankD.getHeight();
    UUID uuid = UUID.randomUUID();

    private boolean moving = true;
    private boolean living = true;

    private final Random random = new Random();

    public RectTank(int x, int y, Direction direction, Group group) {
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

        if (group == Group.GOOD) {
            String goodFsName = (String) PropertyMgr.get("goodFS");
            try {
                fireStrategy = (FireStrategy) Class.forName(goodFsName).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            fireStrategy = new DefaultFireStrategy();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void paint(Graphics g) {
        if (!living) {
            return;
        }
        switch (direction) {
            case UP:
                g.drawImage(this.group == Group.BAD ? ResourceMgr.getResourceMgr().tankU : ResourceMgr.getResourceMgr().tankUG, this.x, this.y, null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.BAD ? ResourceMgr.getResourceMgr().tankR : ResourceMgr.getResourceMgr().tankRG, this.x, this.y, null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.BAD ? ResourceMgr.getResourceMgr().tankD : ResourceMgr.getResourceMgr().tankDG, this.x, this.y, null);
                break;
            case LEFT:
                g.drawImage(this.group == Group.BAD ? ResourceMgr.getResourceMgr().tankL : ResourceMgr.getResourceMgr().tankLG, this.x, this.y, null);
                break;
        }
        move();
    }

    private void move() {
        if (!moving) {
            return;
        }
        // 很多人在此处会栽跟头，认为应该将所有变量前面加上 myTank
        // 比如说 myTank.direction 等，但是这样做会破坏坦克的封装性
        // 那么要怎么做呢？
        // 坦克自己最知道该怎么画，将画笔传入即可
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

        if (this.group == Group.BAD && random.nextInt(100) > 95) {
            this.fire();
        }

        if (this.group == Group.BAD && random.nextInt(100) > 96) {
            this.randomDir();
        }

        checkBound();

        rectangle.x = this.x;
        rectangle.y = this.y;
    }

    private void checkBound() {
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.y < 30) {
            this.y = 30;
        }
        if (this.x > (TankFrame.GAME_WIDTH - Tank.WIDTH)) {
            this.x = TankFrame.GAME_WIDTH - Tank.WIDTH;
        }
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT) {
            this.y = TankFrame.GAME_HEIGHT - Tank.HEIGHT;
        }
    }

    private void randomDir() {
        this.direction = Direction.values()[random.nextInt(4)];
    }

    public void fire() {
        // fireStrategy.fire(Tank);
        int bX = this.x + (RectTank.WIDTH - RectBullet.WIDTH) / 2;
        int bY = this.y + (RectTank.HEIGHT - RectBullet.HEIGHT) / 2;

        GameModel.getInstance().gameFactory.createBullet(this.uuid, bX, bY, this.direction, this.group);
    }

    public void die() {
        this.living = false;
        GameModel.getInstance().remove(this);
    }
}
