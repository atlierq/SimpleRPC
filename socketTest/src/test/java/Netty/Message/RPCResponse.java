package Netty.Message;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RPCResponse<T> {
    private String message;
}
