package Netty.client;


import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.client.handler.NettyClientHandler;
import Netty.codec.RpcMessageDecoder;
import Netty.codec.RpcMessageEncoder;
import Netty.serialize.kyro.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor
public class NettyClient {


    private static Channel channel=null;
    private static final Object lock = new Object();
    public Channel getChannel(){
        if(channel!=null){
            return channel;
        }
        synchronized(lock){
            if(channel!=null){
                return channel;
            }
            initCh();
            return channel;
        }


    }
    private static void initCh(){
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        NettyClientHandler nettyClientHandler = new NettyClientHandler();
        KryoSerializer kryoSerializer = new KryoSerializer();
        RpcMessageDecoder rpcMessageDecoder = new RpcMessageDecoder(kryoSerializer,RPCResponse.class);
        RpcMessageEncoder rpcMessageEncoder = new RpcMessageEncoder(kryoSerializer);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(rpcMessageEncoder);
                        ch.pipeline().addLast(rpcMessageDecoder);
                        ch.pipeline().addLast(nettyClientHandler);
                    }


                });
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        try {
            channel=bootstrap.connect("127.0.0.1", 8080).sync().addListener(future -> {
                if (future.isSuccess()) {
                    log.info("The client has connected  successful!");
                } else {
                    throw new IllegalStateException();
                }

            }).channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public Object sendRPCRequest(RPCRequest rpcRequest)  {
        try {
//            getChannel();
//            System.out.println("奇怪的地方"+channel);
////            Thread.sleep(100);
            getChannel().writeAndFlush(rpcRequest).addListener(future->{
                if(future.isSuccess()){
                    log.info("client send message");
                }else{
                    log.info("send failed");
                }
            });
            channel.closeFuture().sync();
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("rpcResponse");
            return channel.attr(key).get();
        } catch (InterruptedException e) {
            log.error("occur exception when connect server",e);
        }
        return null;
    }









}
