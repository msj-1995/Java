package com.msj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class Springboot09EmailApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;
    @Test
    void contextLoads() {
        SimpleMailMessage simpleMailMessage =new SimpleMailMessage();
        //设置邮件主题
        simpleMailMessage.setSubject("小孟，你好呀");
        //设置邮件内容
        simpleMailMessage.setText("谢谢你发给我的邮件");
        //发送给谁
        simpleMailMessage.setTo("2669159659@qq.com");
        //从哪里发送
        simpleMailMessage.setFrom("2669159659@qq.com");
        mailSender.send(simpleMailMessage);
    }

    //复杂邮件
    @Test
    void contextLoads2() throws MessagingException {
        //一个复杂的邮件，有两种方式，一种是使用MimeMessage类，一种是使用SimpleMailMessage的对象调用createMimeMessage
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装:使用MimeMessageHelper辅助创建复杂邮件,第二个参数为支持多文件
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setSubject("小孟你好呀");
        //支持html文档解析（使用彩色文本）,不使用第二个参数时，直接用文本也可以
        helper.setText("<p style='color:red'>谢谢你给我发邮件</p>",true);
        //附件
        helper.addAttachment("1.jpg",new File("E:\\文件\\文档\\以前的文件\\1.jpg"));
        helper.setTo("2669159659@qq.com");
        helper.setFrom("2669159659@qq.com");
        mailSender.send(mimeMessage);
    }


}
