package com.tripp.tank.observer;

import com.tripp.tank.Tank;

public class FireHandler implements FireObserver {
    @Override
    public void actionOnFire(FireEvent fireEvent) {
        Tank tank = fireEvent.getSource();
        tank.fire();
    }
}
