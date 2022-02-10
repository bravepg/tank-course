package com.tripp.tank;

import com.tripp.tank.net.Client;
import com.tripp.tank.net.TankDirChangedMsg;
import com.tripp.tank.net.TankStartMovingMsg;
import com.tripp.tank.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankFrame extends Frame {
    private static final GameModel gameModel = GameModel.getInstance();
    public final static int GAME_WIDTH = 1080;
    public final static int GAME_HEIGHT = 800;

    public TankFrame() {
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("tank war");
        this.setVisible(true);

        this.addKeyListener(new KeyListener());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 双缓冲
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }

        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 重新绘制
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g) {
        gameModel.paint(g);
    }

    static class KeyListener extends KeyAdapter {
        private boolean bU;
        private boolean bR;
        private boolean bD;
        private boolean bL;

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    // 不可以使用 this.y
                    // 需要使用 TankFrame.this.y -= 20;
                    bU = true;
                    setTankDirection();
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    setTankDirection();
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    setTankDirection();
                    break;
                case KeyEvent.VK_LEFT:
                    bL = true;
                    setTankDirection();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    bU = false;
                    setTankDirection();
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    setTankDirection();
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    setTankDirection();
                    break;
                case KeyEvent.VK_LEFT:
                    bL = false;
                    setTankDirection();
                    break;
                case KeyEvent.VK_SPACE:
                    gameModel.getMainTank().handleFire();
                    break;
                case KeyEvent.VK_S:
                    gameModel.save();
                    break;
                case KeyEvent.VK_L:
                    gameModel.load();
                    break;
                default:
                    break;
            }
        }

        private void setTankDirection() {
            Tank myTank = gameModel.getMainTank();
            Direction oldDirection = myTank.getDirection();

            if (!bU && !bR && !bD && !bL) {
                myTank.setMoving(false);
                Client.INSTANCE.send(new TankStopMsg(GameModel.getInstance().getMainTank()));
                return;
            }
            if (bU) {
                myTank.setDirection(Direction.UP);
            }
            if (bR) {
                myTank.setDirection(Direction.RIGHT);
            }
            if (bD) {
                myTank.setDirection(Direction.DOWN);
            }
            if (bL) {
                myTank.setDirection(Direction.LEFT);
            }

            if (!myTank.isMoving()) {
                Client.INSTANCE.send(new TankStartMovingMsg(GameModel.getInstance().getMainTank()));
            }

            myTank.setMoving(true);

            if(oldDirection != myTank.getDirection()) {
                Client.INSTANCE.send(new TankDirChangedMsg(GameModel.getInstance().getMainTank()));
            }
        }
    }
}
