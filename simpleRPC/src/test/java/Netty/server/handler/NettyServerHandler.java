package Netty.server.handler;

import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCRequest rpcRequest = (RPCRequest) msg;
        log.info("server receive msg");
        RPCResponse message = RPCResponse.builder().message("message").build();
        ctx.writeAndFlush(message).addListener(future -> {
            if(future.isSuccess()){
                log.info("server response success");
            }else {
                log.error("server response fail");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}
