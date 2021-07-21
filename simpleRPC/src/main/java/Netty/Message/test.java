package Netty.Message;

public class test {


    public static void main(String[] args) {
        RPCMessage rpcMessage = new RPCMessage().builder().data(new RPCRequest()).build();
        System.out.println(rpcMessage.getData());
    }
}
