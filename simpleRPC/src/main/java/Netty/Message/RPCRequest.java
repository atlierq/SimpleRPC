package Netty.Message;

import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class RPCRequest {

    private String interfaceName;
    private String requestID;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String rpcServiceName;
}
