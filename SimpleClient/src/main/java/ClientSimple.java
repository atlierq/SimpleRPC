import Netty.client.NettyClient;
import Netty.proxy.RpcClientProxy;
import Service1.Hello;
import Service1.HelloService;

public class ClientSimple {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111","222"));
        System.out.println(hello);
    }
}


