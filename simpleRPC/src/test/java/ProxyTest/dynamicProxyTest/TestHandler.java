package ProxyTest.dynamicProxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestHandler implements InvocationHandler {
    private final Object target;

    public TestHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method"+method.getName());
        Object o = method.invoke(target, args);
        System.out.println("after method"+method.getName());
        return o;
    }
}
