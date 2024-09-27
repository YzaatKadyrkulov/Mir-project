package kg.mir.Mirproject.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String email, String resetPasswordToken) {
        String resetPasswordLink = "http://localhost:8080/reset-password?token=" + resetPasswordToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Сброс пароля");
        message.setText("Чтобы сбросить пароль, перейдите по следующей ссылке: " + resetPasswordLink);
        mailSender.send(message);
    }
}
