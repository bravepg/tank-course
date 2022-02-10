package com.tripp.tank;

import com.tripp.tank.abstractFactory.BaseBullet;
import com.tripp.tank.net.BulletNewMsg;
import com.tripp.tank.net.Client;

import java.awt.*;
import java.util.UUID;

public class Bullet extends BaseBullet {
    public Rectangle rectangle = new Rectangle();
    private boolean living = true;
    public Direction direction;
    public Group group;
    private UUID playerID;

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private UUID uuid = UUID.randomUUID();

    private final static int SPEED = 8;
    public final static int WIDTH = ResourceMgr.getResourceMgr().bulletD.getWidth();
    public final static int HEIGHT = ResourceMgr.getResourceMgr().bulletD.getHeight();

    public Bullet(UUID playerID, int x, int y, Direction direction, Group group) {
        this.playerID = playerID;
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
        switch (direction) {
            case UP:
                g.drawImage(ResourceMgr.getResourceMgr().bulletU, this.x, this.y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.getResourceMgr().bulletR, this.x, this.y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.getResourceMgr().bulletD, this.x, this.y, null);
                break;
            case LEFT:
                g.drawImage(ResourceMgr.getResourceMgr().bulletL, this.x, this.y, null);
                break;
        }

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

    // public void collideWidth(Tank com.tripp.tank) {
    //     if (this.group == com.tripp.tank.getGroup()) {
    //         return;
    //     }
    //
    //     if (com.tripp.tank.rectangle != null && rectangle.intersects(com.tripp.tank.rectangle)) {
    //         this.die();
    //         com.tripp.tank.die();
    //         int eX = com.tripp.tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
    //         int eY = com.tripp.tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
    //         gameModel.gameObjects.add(gameModel.gameFactory.createExplode(eX, eY, gameModel));
    //     }
    //
    // }

    public void die() {
        this.living = false;
        GameModel.getInstance().remove(this);
    }
}
