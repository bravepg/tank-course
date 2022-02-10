package com.tripp.tank.net;

import com.tripp.tank.Direction;
import com.tripp.tank.GameModel;
import com.tripp.tank.Group;
import com.tripp.tank.Tank;

import java.io.*;
import java.util.UUID;

public class TankStopMsg extends Msg {
    UUID uuid;
    int x;
    int y;

    public TankStopMsg(UUID uuid, int x, int y) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
    }

    public TankStopMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.uuid = tank.getUuid();
    }

    public TankStopMsg() {
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
            tank.setMoving(false);
            tank.setX(this.x);
            tank.setY(this.y);
        }
    }

    @Override
    void parse(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = dataInputStream.readInt();
            this.y = dataInputStream.readInt();
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
        return MsgType.TankStop;
    }

    @Override
    public String toString() {
        return "TankStopMsg{" +
                "uuid=" + uuid +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
