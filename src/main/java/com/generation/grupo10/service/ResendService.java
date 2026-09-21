package com.generation.grupo10.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendService {

    private final Resend resend;
    private final String fromEmail;

    public ResendService(
            @Value("${resend.api.key}") String apiKey,
            @Value("${resend.from.email}") String fromEmail
    ) {
        this.resend = new Resend(apiKey);
        this.fromEmail = fromEmail;
    }

    public void enviarCorreo(
            String destinatario,
            String asunto,
            String contenidoHtml
    ) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(destinatario)
                    .subject(asunto)
                    .html(contenidoHtml)
                    .build();

            resend.emails().send(params);

        } catch (ResendException e) {

            throw new RuntimeException(
                    "No se pudo enviar el correo mediante Resend",
                    e
            );
        }
    }
}