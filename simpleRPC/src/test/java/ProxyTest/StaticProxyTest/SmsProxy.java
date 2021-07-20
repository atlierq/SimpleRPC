package ProxyTest.StaticProxyTest;

import ProxyTest.Service.SmsService;
import ProxyTest.Service.SmsServiceImpl;

public class SmsProxy implements SmsService{
    private final SmsService smsService;

    public SmsProxy(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override

    public String send(String message) {
        System.out.println("before send");
        smsService.send(message);
        System.out.println("after send");
        return null;
    }

    public static void main(String[] args) {
        SmsServiceImpl smsService = new SmsServiceImpl();
        SmsProxy smsProxy = new SmsProxy(smsService);
        smsProxy.send("sss");


    }
}
