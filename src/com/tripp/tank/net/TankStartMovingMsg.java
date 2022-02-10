package com.tripp.tank.net;

import com.tripp.tank.Direction;
import com.tripp.tank.GameModel;
import com.tripp.tank.Group;
import com.tripp.tank.Tank;

import java.io.*;
import java.util.UUID;

public class TankStartMovingMsg extends Msg {
    UUID uuid;
    int x;
    int y;
    Direction direction;

    public TankStartMovingMsg(Tank tank) {
        this.uuid = tank.getUuid();
        this.x = tank.getX();
        this.y = tank.getY();
        this.direction = tank.getDirection();
    }

    public TankStartMovingMsg() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    @Override
    byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        DataOutputStream dataOutputStream = null;
        byte[] bytes = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeInt(x);
            dataOutputStream.writeInt(y);
            dataOutputStream.writeInt(direction.ordinal());
            dataOutputStream.writeLong(uuid.getMostSignificantBits());
            dataOutputStream.writeLong(uuid.getLeastSignificantBits());

            dataOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    void handle() {
        if (this.uuid.equals(GameModel.getInstance().getMainTank().uuid)) {
            return;
        }
        Tank tank = GameModel.getInstance().findByUUID(this.uuid);

        if (tank != null) {
            tank.setMoving(true);
            tank.setX(this.x);
            tank.setY(this.y);
            tank.setDirection(this.direction);
        }
    }

    @Override
    void parse(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = dataInputStream.readInt();
            this.y = dataInputStream.readInt();
            this.direction = Direction.values()[dataInputStream.readInt()];
            this.uuid = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    MsgType getMsgType() {
        return MsgType.TankStartMoving;
    }

    @Override
    public String toString() {
        return "TankStartMovingMsg{" +
                "uuid=" + uuid +
                ", x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
