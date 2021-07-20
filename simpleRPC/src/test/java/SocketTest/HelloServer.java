package SocketTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class HelloServer {
    private static final Logger logger = LoggerFactory.getLogger(HelloServer.class);
    public void start(int port){
        try(ServerSocket server = new ServerSocket(port)){
            Socket socket;
            while((socket=server.accept())!=null){
                log.info("client connected");
                try(ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
                    Message message = (Message) objectInputStream.readObject();
                    log.info("server receive message"+message.getContent());
                    message.setContent("new content");
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HelloServer helloServer = new HelloServer();
        helloServer.start(6666);
    }

}
