package br.com.alura.forum.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Answer;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {
	
	private MailSender mailSender;

    @Async
    public void sendNewAnswerReply(Answer answer) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Dúvida respondida em " +   answer.getCreationTime());
        mailMessage.setTo(answer.getTopic().getOwnerEmail());
        mailMessage.setText("Aqui vai um texto legal do corpo do email");

        try {
            mailSender.send(mailMessage);
        } catch (MailException ex) {
            ex.printStackTrace();
        }

    }

}
