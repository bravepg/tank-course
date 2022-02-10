package com.tripp.tank;

import com.tripp.tank.abstractFactory.*;
import com.tripp.tank.chainOfRes.ColliderChain;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameModel {
    private static final GameModel GAME_MODEL = new GameModel();

    static {
        // 防止套娃式依赖
        // GameModel 依赖 Tank
        // Tank 依赖 GameModel
        GAME_MODEL.init();
    }

    private Tank myTank;

    public List<GameObject> gameObjects = new ArrayList<>();

    public GameFactory gameFactory = new DefaultFactory();

    public ColliderChain colliderChain = new ColliderChain();

    Random r = new Random();

    private GameModel() {}

    private void init() {
        // 初始化主坦克
        myTank = new Tank(r.nextInt(TankFrame.GAME_WIDTH), r.nextInt(TankFrame.GAME_HEIGHT), Direction.DOWN, Group.GOOD);

        // int count = Integer.parseInt((String) Objects.requireNonNull(PropertyMgr.get("initTankCount")));
        //
        // for (int i = 0; i < count; i++) {
        //     this.gameFactory.createTank(30 + 80 * i, 200, Direction.DOWN, Group.BAD);
        // }

        // 初始化墙
        new Wall(150, 150, 200, 50);
        new Wall(550, 150, 200, 50);
        new Wall(300, 300, 50, 200);
        new Wall(550, 300, 50, 200);
    }

    public static GameModel getInstance() {
        return GAME_MODEL;
    }

    public void add(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void remove(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public void paint(Graphics g) {
        // 很多人在此处会栽跟头，认为应该将所有变量前面加上 myTank
        // 比如说 myTank.direction 等，但是这样做会破坏坦克的封装性
        // 那么要怎么做呢？
        // 坦克自己最知道该怎么画，将画笔传入即可
        myTank.paint(g);

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        // g.drawString("子弹的数量: " + bullets.size(), 10, 60);
        // g.drawString("敌人的数量: " + enemies.size(), 10, 80);
        // g.drawString("爆炸的数量: " + explodes.size(), 10, 100);
        g.drawString("游戏物体的数量: " + gameObjects.size(), 10, 60);
        g.setColor(c);

        // AWT-EventQueue-0
        // 迭代器遍历不能在其他地方删除集合元素
        // for (Bullet bullet: bullets) {
        //     bullet.paint(g);
        // }
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).paint(g);
        }

        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = i + 1; j < gameObjects.size(); j++) {
                GameObject gameObject1 = gameObjects.get(i);
                GameObject gameObject2 = gameObjects.get(j);

                colliderChain.collide(gameObject1, gameObject2);
            }
        }

        // for (int i = 0; i < enemies.size(); i++) {
        //     enemies.get(i).paint(g);
        // }
        //
        // for (int i = 0; i < explodes.size(); i++) {
        //     explodes.get(i).paint(g);
        // }

        // for (int i = 0; i < bullets.size(); i++) {
        //     for (int j = 0; j < enemies.size(); j++) {
        //         bullets.get(i).collideWidth(enemies.get(j));
        //     }
        // }
    }

    public Tank getMainTank() {
        return myTank;
    }

    public Tank findByUUID(UUID uuid) {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject instanceof Tank && ((Tank) gameObject).getUuid().equals(uuid)) {
                return (Tank) gameObject;
            }
        }

        return null;
    }

    public Bullet findByBulletUUID(UUID uuid) {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject instanceof Bullet && ((Bullet) gameObject).getUuid().equals(uuid)) {
                return (Bullet) gameObject;
            }
        }

        return null;
    }

    public void save() {
        File file = new File("src/data/com.tripp.tank.data");
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(myTank);
            objectOutputStream.writeObject(gameObjects);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File("src/data/com.tripp.tank.data");
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            myTank = (Tank) objectInputStream.readObject();
            gameObjects = (List<GameObject>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
