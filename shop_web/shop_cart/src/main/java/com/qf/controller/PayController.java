package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    private IOrderService orderService;

    /**
     * 调用阿里支付
     * @return
     */
    @RequestMapping("/alipay")
    @ResponseBody
    public void pay(String orderid, HttpServletResponse response) throws IOException {

        //通过orderid获得订单对象
        Orders orders = orderService.queryByOrderId(orderid);

        //调用支付宝进行支付

        //获得初始化的AlipayClient - 后续的支付请求、退款请求.... 都是基于这个客户端对象的，所以这个对象应该在项目中唯一保存
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016073000127352",//appid
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYK8iVAiYIBMabTR1Sz+PtJEK4wPRmYnwsT3DRuSiWvHwNC6RmdwI5kAar0yl/+wtCTN6HViGeUBSysVuUCjSFe5yHfqLKcpchlGJWeH3nR+/Uze2C2JQRSb0O2nRsJ8sNWMzRj0BokkQqLoz3h8GNH+o+eE3EJLWOvIwl9SrhN3rYLvEebPZ/Uy4OFaNyzaRWup6zCzXvvTzYmF0mj467t4ctln3zVnDNdWXfZISeElR4sCLdomUWS+B8V4xXnFSs1KFlSyYxy1ZXi2v1YHeGMzp2uQE80unR24FnRkixuSdo0lMwXJGY94W8WazqKgEZOSDNC3NuvbMqzeu9MNUDAgMBAAECggEBAI7CpM+G0H89W8ZfNl92wfK8zgvemmtjgfNgJAvdlgK0mfzzKy0mAWRXhqq01H5I22s8CpkVaTzJDGIImk3stRF1wogrejZMAxVxzEArkpLknlAbUfQrk94UO9YvhIHjY1AGO0FvD5ILhv+75GxlZGQtyOkKnIqjleFvalxCAhR/MUDNVcRG5mp8WclwzIT75Hp/r1GsCqbhQra8SszbBau1kAIiozJbPmTkkOhJxWirSU+4cVcytE6agZxLj/V9BueJjoGh5xK2I7nlH0DY9S4dBIO5cF6JkEOHs9oxEyF4bq6nLs2Wvu4oxw/5OucaEnQAOHqUFXZkl1+DH/qlPCECgYEA0RLbey4otwW5OvE7qJgV8SPRHRl90+jyBxsNQJLOJzo3sw5wMtNZJJL4BceuW9IxwUwLQ8lYgo9rhoiIDxMp/0D6/xRg7Hic1fy78vdiHNHGnxCV/ycJqqt+p64QTAh/+xulIGzv9AZPTYYI+OR2ubIsbhPYiAE3f93hyGanf6cCgYEAulNdD04pyfyYlTSwt8uClPorM53eVxsDVGsfjIaKaqAlsb9fsmFVHXYeOCaoB3GFu1ECu6w6x/UE5Bd/KZbu9Z7x6Hxh0fp61ijoyHXbWrEmmKGWBn+rPWWEgguIChL7lUv4OuvFsxZH3eg0C4qiZrRu2/LsAOs1gDYtpKvey0UCgYAu+0TU5meB0UhxhVn5pctwd7L7DgpnfzY8AIk21IZ87CkuFHWEBlhGzrvrWJbGzXIe1L7HScyY8i+geLfSY2URlgGRJnwtR2E++N8nVw32GfG/NtPuFVCnyIfamFPTMinIPMsbgT9en7hcXF1RyM0hJ3eyeMU0sslM84hUJzVGiwKBgHtbh8gDSr10o5xdBwaYEPsK6aFByptGwhyY04lY5bkUEACvH+9ca1qXAgjQ5vwOl6hdSP7xVvVdbYCJYeIqEEjDk4sFFkpLmBnDeeKFzNUtZc01ZbsluTxrwp/KfKWWzcOZSV3RDNG1VZ1AB30AquZuXNr36HHvRzt8IxnbxCQ1AoGAa/pz5881SkZaiZ4O0g+3WNMZ6Fh2NqJgf5XjFdsCK0nhNzCn4dGVkpaSuh2v35UUiSq3tLqmNmlbGBJArXTQ2wDkOUecLKJMeuWnMnUlPuyjxslZIOr6TVHupqNadGCMeDhvK3wfiwMfc1VzRdu47aonC03N8iAg/pZkbPFZ7y8=",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx+n+pEqTVi27mtj3CuUQXi2ixqeeTwE/0tGrW+sg6xtfajvJV67GYf2zzNxxBV0TYfhdbi70VI3DftEijg7GSNKoOilAu2DKQFqidnSxmN1Es1oRTaiaehqm1Uzs2uswpzBVR21iygLHujwthC8kNkMgxVFkjbE/qTn7z5wlsailtg6wF+hY3BcDCiaLyVLjEDngmrLyLXPLenjAuvXq20h9zV7CW9HXuhpPBDfsn4fv5TjgEl1smjJNr4O/VxICKDNPsvrCyNXhfroK9PEFFwH+4IWGeBUJAP2cSufNU0OA+UH+2xQnaR8Cz30QIgIslckBGuXQZvxqaY2mMMz14QIDAQAB",//支付宝的公钥
                "RSA2");

        //创建一个生产交易页面的请求对象
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //设置交易完成的同步请求地址
        alipayRequest.setReturnUrl("http://www.baidu.com");
        //设置交易完成的异步请求地址
        alipayRequest.setNotifyUrl("http://verygoodwlk.xicp.net/pay/paynotify");

        //通过交易请求对象，设置业务主体信息（订单号、支付金额）
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orders.getOrderid() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + orders.getAllprice().doubleValue() + "," +
                "    \"subject\":\"" + orders.getOrderid() + "\"," +
                "    \"body\":\"" + orders.getOrderid() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数

        //通过sdk的api调用，发送业务主体信息给支付宝服务器，并且返回一个支付页面
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }


    /**
     * 支付宝异步通知接收的方法
     */
    @RequestMapping("/paynotify")
    @ResponseBody
    public void payNotify(String out_trade_no, String trade_status){

        //根据订单号获得订单信息
        Orders orders = orderService.queryByOrderId(out_trade_no);
        if (trade_status.equals("TRADE_SUCCESS")){
            //交易成功
            orders.setStatus(1);
            orderService.updateOrders(orders);
        }

    }
}
