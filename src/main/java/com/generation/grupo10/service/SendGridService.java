package com.generation.grupo10.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService {

    private final SendGrid sendGrid;
    private final String fromEmail;
    private final String fromName;

    public SendGridService(
            @Value("${sendgrid.api.key}") String apiKey,
            @Value("${sendgrid.from.email}") String fromEmail,
            @Value("${sendgrid.from.name}") String fromName
    ) {
        this.sendGrid = new SendGrid(apiKey);
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public void enviarCorreo(
            String destinatario,
            String asunto,
            String contenidoHtml
    ) {

        Email from = new Email(fromEmail, fromName);
        Email to = new Email(destinatario);

        Content content = new Content(
                "text/html",
                contenidoHtml
        );

        Mail mail = new Mail(from, asunto, to, content);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            var response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException(
                        "SendGrid rechazó el correo. " +
                                "Status: " + response.getStatusCode() +
                                " - " + response.getBody()
                );
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo enviar el correo mediante SendGrid",
                    e
            );
        }
    }
}