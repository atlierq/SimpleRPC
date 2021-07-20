package Netty.client.handler;

import Netty.Message.RPCMessage;
import Netty.Message.RPCResponse;
import Netty.Tools.Factory.SingletonFactory;
import Netty.client.unProcessedRequests;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final unProcessedRequests unProcessedRequests;

    public NettyClientHandler(){
        unProcessedRequests = SingletonFactory.getInstance(unProcessedRequests.class);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("client receive msg:[{}]",msg);
        if(msg instanceof RPCMessage){
            RPCMessage tmp = (RPCMessage) msg;
            RPCResponse<Object> data = (RPCResponse<Object>) tmp.getData();
            unProcessedRequests.complete(data);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client caught exception",cause);
        ctx.close();
    }
}
