package registry;

import Netty.Message.RPCRequest;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress lookupService(RPCRequest rpcRequest);
}
