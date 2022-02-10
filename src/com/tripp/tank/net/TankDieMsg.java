package com.tripp.tank.net;

import com.tripp.tank.*;

import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Msg {
    UUID bulletID;
    UUID uuid;

    public TankDieMsg(UUID bulletID, UUID uuid) {
        this.bulletID = bulletID;
        this.uuid = uuid;
    }

    public TankDieMsg() {
    }

    @Override
    byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        DataOutputStream dataOutputStream = null;
        byte[] bytes = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeLong(bulletID.getMostSignificantBits());
            dataOutputStream.writeLong(bulletID.getLeastSignificantBits());
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
        Bullet bullet = GameModel.getInstance().findByBulletUUID(this.bulletID);

        if (bullet != null) {
            bullet.die();
        }

        if (this.uuid.equals(GameModel.getInstance().getMainTank().getUuid())) {
            GameModel.getInstance().getMainTank().die();
            return;
        }

        Tank tank = GameModel.getInstance().findByUUID(this.uuid);

        if (tank != null) {
            tank.die();
        }
    }

    @Override
    void parse(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.bulletID = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
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
        return MsgType.TankDie;
    }

    @Override
    public String toString() {
        return "TankDieMsg{" +
                "bulletID=" + bulletID +
                ", uuid=" + uuid +
                '}';
    }
}
