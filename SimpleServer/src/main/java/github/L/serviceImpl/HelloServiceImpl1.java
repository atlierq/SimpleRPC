package github.L.serviceImpl;

import Service1.Hello;
import Service1.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl1 implements HelloService {
    static{
        System.out.println("HelloServiceImpl1创建");
    }
    @Override
    public String hello(Hello hello) {
        log.info("helloServiceImpl1收到{}",hello.getMessage());
        String result = "hello description is"+hello.getDescription();
        log.info("helloServiceImpl1返回：{}",result);
        return result;
    }
}
