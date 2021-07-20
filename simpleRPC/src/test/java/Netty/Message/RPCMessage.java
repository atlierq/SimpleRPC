package Netty.Message;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RPCMessage {
    private byte messageType;

    private byte codec;

    private int requestID;

    private Object data;
}
