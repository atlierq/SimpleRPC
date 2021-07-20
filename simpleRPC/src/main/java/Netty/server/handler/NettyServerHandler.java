package Netty.server.handler;

import Netty.Message.RPCMessage;
import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RPCMessage){
            log.info("server receive msg:[{}]",msg);
            RPCRequest rpcRequest = (RPCRequest) ((RPCMessage) msg).getData();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}
