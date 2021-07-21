package Netty.Message;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RPCMessage {
    private byte messageType;

    private byte codec;

    private int requestID=1;

    private Object data;
}
