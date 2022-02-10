package com.tripp.tank;
import com.tripp.tank.abstractFactory.BaseTank;
import com.tripp.tank.net.TankJoinMsg;
import com.tripp.tank.observer.FireEvent;
import com.tripp.tank.observer.FireHandler;
import com.tripp.tank.observer.FireObserver;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Tank extends BaseTank {
    public int prevX;
    public int prevY;
    public UUID uuid = UUID.randomUUID();

    public Direction direction;
    public Group group;
    private FireStrategy fireStrategy;
    private final static int SPEED = 5;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public final static int WIDTH = ResourceMgr.getResourceMgr().tankD.getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().tankD.getHeight();

    private boolean moving = false;
    private boolean living = true;

    private final Random random = new Random();

    public Tank(int x, int y, Direction direction, Group group) {
        this.x = x;
        this.y = y;
        this.w = WIDTH;
        this.h = HEIGHT;
        this.prevX = x;
        this.prevY = y;
        this.direction = direction;
        this.group = group;

        rectangle.x = x;
        rectangle.y = y;
        rectangle.width = WIDTH;
        rectangle.height = HEIGHT;

        // if (group == Group.GOOD) {
        //     String goodFsName = (String) PropertyMgr.get("goodFS");
        //     try {
        //         fireStrategy = (FireStrategy) Class.forName(goodFsName).getDeclaredConstructor().newInstance();
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // } else {
        //     fireStrategy = new DefaultFireStrategy();
        // }

        fireStrategy = new DefaultFireStrategy();

        GameModel.getInstance().add(this);
    }

    public Tank(TankJoinMsg tankJoinMsg) {
        this.x = tankJoinMsg.x;
        this.y = tankJoinMsg.y;
        this.w = WIDTH;
        this.h = HEIGHT;
        this.prevX = tankJoinMsg.x;
        this.prevY = tankJoinMsg.y;
        this.direction = tankJoinMsg.direction;
        this.group = tankJoinMsg.group;
        this.uuid = tankJoinMsg.uuid;
        this.moving = tankJoinMsg.moving;

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

        GameModel.getInstance().add(this);
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

    public Direction getReverseDirection() {
        if (this.direction == Direction.LEFT) {
            return Direction.RIGHT;
        } else if (this.direction == Direction.RIGHT) {
            return Direction.LEFT;
        } else if (this.direction == Direction.UP) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
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

        Color color = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(uuid.toString(), this.x, this.y - 10);
        g.setColor(color);

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
        this.prevX = this.x;
        this.prevY = this.y;
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
        fireStrategy.fire(this);
    }

    public void die() {
        this.living = false;
        GameModel.getInstance().remove(this);
    }

    public void handleFire() {
        List<FireObserver> fireObservers = Collections.singletonList(new FireHandler());

        for (FireObserver fireObserver: fireObservers) {
            fireObserver.actionOnFire(new FireEvent(this));
        }
    }
}
