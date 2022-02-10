package com.tripp.tank.net;

import com.tripp.tank.Direction;
import com.tripp.tank.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;


public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 8) {
            return;
        }

        byteBuf.markReaderIndex();

        MsgType msgType = MsgType.values()[byteBuf.readInt()];
        int length = byteBuf.readInt();

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Msg msg = null;

        System.out.println("msgType --- " + msgType);

        switch (msgType) {
            case TankJoin:
                msg = new TankJoinMsg();
                break;
            case TankStartMoving:
                msg = new TankStartMovingMsg();
                break;
            case TankStop:
                msg = new TankStopMsg();
                break;
            case TankDirChanged:
                msg = new TankDirChangedMsg();
                break;
            case TankDie:
                msg = new TankDieMsg();
                break;
            case BulletNew:
                msg = new BulletNewMsg();
                break;
            default:
                break;
        }
        if (msg != null) {
            msg.parse(bytes);
            list.add(msg);
        }
    }
}
