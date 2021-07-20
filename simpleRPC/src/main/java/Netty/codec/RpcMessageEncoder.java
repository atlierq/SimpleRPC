package Netty.codec;

import Netty.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

import static Netty.Tools.LogTools.log;

@AllArgsConstructor
public class RpcMessageEncoder extends MessageToByteEncoder {

    private final Serializer serializer;


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

            //将对象转换为byte
        byte[] body = serializer.serialize(o);
        //2.获取长度
        int dataLength = body.length;

        //写入长度
        byteBuf.writeInt(dataLength);

        //写入数据
        byteBuf.writeBytes(body);
        log(byteBuf);
    }
}
