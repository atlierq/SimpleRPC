package Netty.client;


import Netty.Message.RPCMessage;
import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.client.handler.NettyClientHandler;
import Netty.codec.RpcMessageDecoder;
import Netty.codec.RpcMessageEncoder;
import Netty.registry.ServiceDiscovery;
import Netty.registry.zk.ServiceDiscoveryImpl;
import Netty.serialize.kyro.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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
import org.checkerframework.checker.units.qual.C;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor
public class NettyClient {
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup eventLoopGroup;
    private ServiceDiscovery serviceDiscovery;
    public NettyClient(){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
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

        serviceDiscovery = new ServiceDiscoveryImpl();
    }


    private static volatile Channel channel=null;
    private static final Object lock = new Object();
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        if(channel!=null){
            return channel;
        }
        synchronized(lock){
            if(channel!=null){
                return channel;
            }
            try {
                channel=doConnect(inetSocketAddress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return channel;
        }


    }
    public Channel doConnect(InetSocketAddress inetSocketAddress) throws InterruptedException {
        //这是链接部分
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        Channel channel = bootstrap.connect(inetSocketAddress).addListener(future -> {
            if (future.isSuccess()) {
                log.info("connect[{}] success", inetSocketAddress.toString());
            } else {
                throw new IllegalStateException();
            }
        }).sync().channel();
        return channel;


    }
    public Object sendRPCRequest(RPCRequest rpcRequest)  {

        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        Channel channel = getChannel(inetSocketAddress);
        if(channel.isActive()){
            RPCMessage rpcMessage = RPCMessage.builder()
                    .data(rpcRequest)
                    .build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if(future.isSuccess()){
                    log.info("client send message[{}]",rpcMessage);
                }else {
                    future.channel().close();
                    log.error("send failed",future.cause());
                }
            });
        }else {
            throw new IllegalStateException();
        }
        try {
//            getChannel();
//            System.out.println("奇怪的地方"+channel);
////            Thread.sleep(100);
            getChannel(inetSocketAddress).writeAndFlush(rpcRequest).addListener(future->{
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
