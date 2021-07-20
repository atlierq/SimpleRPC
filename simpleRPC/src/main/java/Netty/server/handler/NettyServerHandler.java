package Netty.server.handler;

import Netty.Message.RPCMessage;
import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.Tools.Factory.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private final RpcRequestHandler rpcRequestHandler;
    public NettyServerHandler(){
        rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof RPCMessage){
                log.info("server receive msg:[{}]",msg);
                RPCMessage rpcMessage = new RPCMessage();
                RPCRequest rpcRequest = (RPCRequest) ((RPCMessage) msg).getData();
                Object result = rpcRequestHandler.handle(rpcRequest);
                RPCResponse rpcResponse = new RPCResponse(rpcRequest.getRequestID(),1,"success",result);
                rpcMessage.setData(rpcResponse);
                ctx.writeAndFlush(rpcMessage).addListener(future -> {
                    if(future.isSuccess()){
                        log.info("server send response");
                    }else {
                        log.error("server send response failed");
                    }
                });
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception",cause);
        ctx.close();
    }
}
