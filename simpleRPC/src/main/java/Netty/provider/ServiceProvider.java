package Netty.provider;

public interface ServiceProvider {
    Object getService(String rpcServiceName);

}
