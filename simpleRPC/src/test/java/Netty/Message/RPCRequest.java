package Netty.Message;

import lombok.*;
import org.junit.Test;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RPCRequest {

    private String interfaceName;

    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String rpcServiceName;
}
