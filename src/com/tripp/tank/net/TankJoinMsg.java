package com.tripp.tank.net;

import com.tripp.tank.Direction;
import com.tripp.tank.GameModel;
import com.tripp.tank.Group;
import com.tripp.tank.Tank;

import java.io.*;
import java.util.UUID;

public class TankJoinMsg extends Msg {
    public int x, y;
    public Direction direction;
    public boolean moving;
    public Group group;
    public UUID uuid;

    public TankJoinMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.direction = tank.getDirection();
        this.moving = tank.isMoving();
        this.group = tank.getGroup();
        this.uuid = tank.getUuid();
    }

    public TankJoinMsg(int x, int y, Direction direction, boolean moving, Group group, UUID uuid) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.moving = moving;
        this.group = group;
        this.uuid = uuid;
    }

    public TankJoinMsg() {}

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream byteArrayOutputStream = null;
        DataOutputStream dataOutputStream = null;
        byte[] bytes = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            dataOutputStream.writeInt(x);
            dataOutputStream.writeInt(y);
            dataOutputStream.writeInt(direction.ordinal());
            dataOutputStream.writeBoolean(moving);
            dataOutputStream.writeInt(group.ordinal());
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
        if (this.uuid.equals(GameModel.getInstance().getMainTank().uuid) ||
                GameModel.getInstance().findByUUID(this.uuid) != null
        ) {
            return;
        }
        System.out.println(this);
        Tank tank = new Tank(this);

        GameModel.getInstance().add(tank);

        Client.INSTANCE.channel.writeAndFlush(new TankJoinMsg(GameModel.getInstance().getMainTank()));
    }

    @Override
    void parse(byte[] bytes) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));

        try {
            this.x = dataInputStream.readInt();
            this.y = dataInputStream.readInt();
            this.direction = Direction.values()[dataInputStream.readInt()];
            this.moving = dataInputStream.readBoolean();
            this.group = Group.values()[dataInputStream.readInt()];
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
        return MsgType.TankJoin;
    }

    @Override
    public String toString() {
        return "TankJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", moving=" + moving +
                ", group=" + group +
                ", uuid=" + uuid +
                '}';
    }
}
