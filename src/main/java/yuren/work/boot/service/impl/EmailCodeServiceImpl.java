package yuren.work.boot.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import yuren.work.boot.service.EmailCodeService;

/**
 * 实现类
 * @author liyuzhen
 */
@Service
@Slf4j
public class EmailCodeServiceImpl implements EmailCodeService {
    /**
     * springboot专门发送邮件接口
     */
    @Autowired
    private JavaMailSender mailSender;

    private String from = "yuren.lin@qq.com";

    @Override
    public boolean sendCode(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        //发送邮件的邮箱
        msg.setFrom(from);
        //发送到哪(邮箱)
        msg.setTo(to);
        //邮箱标题
        msg.setSubject("校园论坛验证码");
        //邮箱文本
        msg.setText("您的验证码为："+code+"。三分钟内有效。");
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            log.info(ex.getMessage());
            return false;
        }
        return true;
    }

}

