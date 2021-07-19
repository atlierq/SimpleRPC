package Netty.Message;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RPCResponse<T> {
    private String requestID;
    private Integer code;

    private String message;

    private T data;
}
