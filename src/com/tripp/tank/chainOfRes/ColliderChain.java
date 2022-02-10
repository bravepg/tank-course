package com.tripp.tank.chainOfRes;

import com.tripp.tank.GameObject;
import com.tripp.tank.PropertyMgr;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ColliderChain implements Collider {
    private final List<Collider> colliderList = new LinkedList<>();
    String colliders = (String) Objects.requireNonNull(PropertyMgr.get("colliders"));
    String[] strings = colliders.split(",");

    public ColliderChain() {
        for (String str: strings) {
            System.out.println(str);
            try {
                colliderList.add((Collider) Class.forName(str).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Collider collider) {
        colliderList.add(collider);
    }

    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        for (int i = 0; i < colliderList.size(); i++) {
            if (colliderList.get(i).collide(o1, o2)) {
                return true;
            }
        }
        return false;
    }
}
