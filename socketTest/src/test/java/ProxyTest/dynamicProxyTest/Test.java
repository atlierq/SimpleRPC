package ProxyTest.dynamicProxyTest;

import ProxyTest.Service.SmsService;
import ProxyTest.Service.SmsServiceImpl;
import ProxyTest.StaticProxyTest.SmsProxy;

public class Test {
    public static void main(String[] args) {
        SmsService smsService = (SmsService) ProxyFactory.getProxy(new SmsServiceImpl());
        smsService.send("aaa");



    }
}
