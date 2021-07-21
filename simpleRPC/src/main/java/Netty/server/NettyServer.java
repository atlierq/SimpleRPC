package Netty.server;

import Netty.Message.RPCRequest;
import Netty.Tools.Factory.SingletonFactory;
import Netty.codec.RpcMessageDecoder;
import Netty.codec.RpcMessageEncoder;
import Netty.config.RpcServiceConfig;
import Netty.provider.Imlp.ZkServiceProviderImpl;
import Netty.provider.ServiceProvider;
import Netty.serialize.kyro.KryoSerializer;
import Netty.server.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;


public class NettyServer {
    public static final int port = 8080;

    private final ServiceProvider serviceProvider;
    public NettyServer(){
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }
    public void registerService(RpcServiceConfig rpcServiceConfig){
        serviceProvider.publishService(rpcServiceConfig);
    }
    public void start(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        KryoSerializer kryoSerializer = new KryoSerializer();
        try {
            ChannelFuture sync = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new RpcMessageDecoder(kryoSerializer, RPCRequest.class));
                            ch.pipeline().addLast(new RpcMessageEncoder(kryoSerializer));
                            ch.pipeline().addLast(new NettyServerHandler());

                        }
                    })
                    .bind(port).sync();
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

}
