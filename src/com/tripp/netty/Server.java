package com.tripp.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void serverStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel
                                .pipeline()
                                .addLast(new TankMsgDecoder())
                                .addLast(new Handler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8888)
                    .sync();

            ServerFrame.INSTANCE.updateServerMsg("server started");

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server().serverStart();
    }
}

class Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            TankMsg tankMsg = (TankMsg) msg;
            System.out.println(tankMsg);
        } finally {
            if (msg != null) {
                ReferenceCountUtil.release(msg);
            }
        }
        // ByteBuf byteBuf = (ByteBuf) msg;
        // byte[] bytes = new byte[byteBuf.readableBytes()];
        // byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        //
        // String msgAccepted = new String(bytes);
        //
        // System.out.println("server: channel read -- " + msgAccepted);
        //
        // ServerFrame.INSTANCE.updateClientMsg(msgAccepted);
        //
        // if (msgAccepted.equals("bye")) {
        //     System.out.println("客户端要求退出");
        //     Server.clients.remove(ctx.channel());
        //     ctx.close();
        //     return;
        // }
        //
        // Server.clients.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 删除异常客户端 channel
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}
