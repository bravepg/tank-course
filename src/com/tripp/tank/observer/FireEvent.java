package com.tripp.tank.observer;

import com.tripp.tank.Tank;

public class FireEvent {
    Tank tank;

    public FireEvent(Tank tank) {
        this.tank = tank;
    }

    public Tank getSource() {
        return tank;
    }
}
