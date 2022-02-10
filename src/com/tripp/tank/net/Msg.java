package com.tripp.tank.net;

abstract class Msg {
    abstract byte[] toBytes();
    abstract void handle();
    abstract void parse(byte[] bytes);
    abstract MsgType getMsgType();
}
