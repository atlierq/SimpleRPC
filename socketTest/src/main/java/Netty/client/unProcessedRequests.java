package Netty.client;

import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class unProcessedRequests {

    private static final Map<String, CompletableFuture<RPCResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();
    public void put(String requestID,CompletableFuture<RPCResponse<Object>>future){
        UNPROCESSED_RESPONSE_FUTURES.put(requestID,future);
    }

    public void complete(RPCResponse<Object> rpcResponse){
        CompletableFuture<RPCResponse<Object>> remove = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestID());
        if(null!=remove){
            remove.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }
}
