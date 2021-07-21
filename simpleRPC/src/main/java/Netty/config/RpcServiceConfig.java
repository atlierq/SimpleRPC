package Netty.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RpcServiceConfig {
    private Object service;
    public String getRpcServiceName(){
        return this.getServiceName();
    }
    public String getServiceName(){
        //这行代码没看懂
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
