package com.qf.listener;

import com.qf.entity.Email;
import com.qf.util.MailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @Autowired
    private MailUtil mailUtil;

    /**
     * 处理rabbitMQ中的邮件对象
     * @param email
     */
    @RabbitListener(queues = "email_queue")
    public void emailHandler(Email email){
        //发送邮件
        try {
            mailUtil.sendMail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
