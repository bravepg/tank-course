package com.tripp.tank.net;

import com.tripp.tank.*;

import java.io.*;
import java.util.UUID;

public class BulletNewMsg extends Msg {
    UUID playerID;
    UUID uuid;
    int x, y;
    Direction direction;
    Group group;

    public BulletNewMsg(Bullet bullet) {
        this.playerID = bullet.getPlayerID();
        this.uuid = bullet.getUuid();
        this.x = bullet.x;
        this.y = bullet.y;
        this.direction = bullet.direction;
        this.group = bullet.group;
    }

    public BulletNewMsg() {
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
            dataOutputStream.writeInt(group.ordinal());
            dataOutputStream.writeLong(playerID.getMostSignificantBits());
            dataOutputStream.writeLong(playerID.getLeastSignificantBits());
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
        if (this.playerID.equals(GameModel.getInstance().getMainTank().uuid)) {
            return;
        }
        System.out.println(this);
        Bullet bullet = new Bullet(this.playerID, this.x, this.y, this.direction, this.group);

        GameModel.getInstance().add(bullet);
    }

    @Override
    void parse(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = dataInputStream.readInt();
            this.y = dataInputStream.readInt();
            this.direction = Direction.values()[dataInputStream.readInt()];
            this.group = Group.values()[dataInputStream.readInt()];
            this.playerID = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
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
        return MsgType.BulletNew;
    }
}
