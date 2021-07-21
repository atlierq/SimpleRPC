package Netty.codec;

import Netty.Message.RPCMessage;
import Netty.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static Netty.Tools.LogTools.log;

@Slf4j
@AllArgsConstructor
public class RpcMessageDecoder extends ByteToMessageDecoder {
    private final Serializer serializer;
    private final Class<?> genericClass;
    private static final int BODY_LENGTH = 4;
//    @Override
//    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        log(byteBuf);
//        if(byteBuf.readableBytes()>=BODY_LENGTH){
//            byteBuf.markReaderIndex();
//            int dataLength = byteBuf.readInt();
//            if(dataLength<0||byteBuf.readableBytes()<0){
//                RpcMessageDecoder.log.error("数据长度不合理");
//                return;
//            }
//            if(byteBuf.readableBytes()<dataLength){
//                byteBuf.resetReaderIndex();
//                return;
//            }
//            byte[] body = new byte[dataLength];
//            byteBuf.readBytes(body);
//
//            Object obj = serializer.deserialize(body,genericClass);
//            list.add(obj);
//            RpcMessageDecoder.log.info("success decode byteBuf to Obj");
//        }
//
//
//    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        RPCMessage message = (RPCMessage) serializer.deserialize(body, genericClass);
        RPCMessage rpcMessage = RPCMessage.builder().data(message.getData()).build();
        list.add(rpcMessage);
        log.info("success decode bytebuf to Obj");

    }
}
