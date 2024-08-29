package com.Ankush.Bank.Service;

import com.Ankush.Bank.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void emailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderMail);
            message.setTo(emailDetails.getRecipient());
            message.setText(emailDetails.getMessageBody());
            message.setSubject(emailDetails.getSubject());

            mailSender.send(message);

            System.out.println("Email Sent");
        }
        catch (MailException e) {
            e.printStackTrace();
        }
    }

}
