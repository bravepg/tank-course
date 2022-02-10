package com.tripp.tank;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ResourceMgr {
    private static ResourceMgr resourceMgr = null;
    public BufferedImage tankU, tankR, tankD, tankL;
    public BufferedImage tankUG, tankRG, tankDG, tankLG;
    public BufferedImage bulletU, bulletR, bulletD, bulletL;
    public BufferedImage[] explodes = new BufferedImage[16];
    public BufferedImage[] walls = new BufferedImage[7];

    private ResourceMgr() {}

    public static ResourceMgr getResourceMgr() {
        if (resourceMgr == null) {
            synchronized (ResourceMgr.class) {
                if (resourceMgr == null) {
                    resourceMgr = new ResourceMgr();
                    try {
                        resourceMgr.tankU = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("images/BadTank1.png")));
                        resourceMgr.tankR = ImageUtil.rotateImage(resourceMgr.tankU, 90);
                        resourceMgr.tankD = ImageUtil.rotateImage(resourceMgr.tankU, 180);;
                        resourceMgr.tankL = ImageUtil.rotateImage(resourceMgr.tankU, -90);;

                        resourceMgr.tankUG = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("images/GoodTank1.png")));
                        resourceMgr.tankRG = ImageUtil.rotateImage(resourceMgr.tankUG, 90);
                        resourceMgr.tankDG = ImageUtil.rotateImage(resourceMgr.tankUG, 180);;
                        resourceMgr.tankLG = ImageUtil.rotateImage(resourceMgr.tankUG, -90);;

                        resourceMgr.bulletU = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("images/bulletU.png")));
                        resourceMgr.bulletR = ImageUtil.rotateImage(resourceMgr.bulletU, 90);
                        resourceMgr.bulletD = ImageUtil.rotateImage(resourceMgr.bulletU, 180);
                        resourceMgr.bulletL = ImageUtil.rotateImage(resourceMgr.bulletU, -90);

                        for (int i = 0; i < 15; i++) {
                            resourceMgr.explodes[i] = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("images/e" + (i + 1) + ".gif")));
                        }

                        for (int i = 0; i < 6; i++) {
                            resourceMgr.walls[i] = ImageIO.read(Objects.requireNonNull(ResourceMgr.class.getClassLoader().getResourceAsStream("images/square" + i + ".jpg")));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return resourceMgr;
    }
}
