package com.generation.grupo10.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void enviarCodigo(String destinatario, String codigo) {

        try {

            Context context = new Context();
            context.setVariable("codigo", codigo);
            String html = templateEngine.process("email/verificacion", context);
            MimeMessage correo = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(correo, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject("Código de verificación - Cancheros");

            helper.setText(html, true);
            mailSender.send(correo);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo", e);
        }
    }

    public void enviarEnlaceRecuperacion(String destinatario, String enlace) {
        try {

            Context context = new Context();
            context.setVariable("enlace", enlace);

            String html = templateEngine.process("email/recuperacion", context);
            MimeMessage correo = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(correo, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject("Recuperación de contraseña - Cancheros");
            helper.setText(html, true);
            mailSender.send(correo);

        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo", e);
        }
    }

    public void enviarEnlaceCambioPassword(
            String destinatario,
            String enlace) {
        try {
            Context context = new Context();
            context.setVariable("enlace", enlace);
            String html = templateEngine.process("email/cambio-password", context);
            MimeMessage correo = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(correo, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject("Validación de cambio de contraseña - Cancheros");
            helper.setText(html, true);
            mailSender.send(correo);

        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo", e);
        }
    }

    public void enviarEnlaceEdicionPerfil(
            String destinatario,
            String enlace) {

        try {

            Context context = new Context();
            context.setVariable("enlace", enlace);

            String html = templateEngine.process(
                    "email/edicion-perfil",
                    context
            );

            MimeMessage correo =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            correo,
                            true,
                            "UTF-8"
                    );

            helper.setTo(destinatario);

            helper.setSubject(
                    "Validación de edición de perfil - Cancheros"
            );

            helper.setText(html, true);

            mailSender.send(correo);

        } catch (MessagingException e) {

            throw new RuntimeException(
                    "No se pudo enviar el correo",
                    e
            );
        }
    }

    public void enviarContacto(
            String destinatario,
            String nombre,
            String email,
            String telefono,
            String mensaje) {

        try {

            Context context = new Context();

            context.setVariable("nombre", nombre);
            context.setVariable("email", email);
            context.setVariable("telefono", telefono);
            context.setVariable("mensaje", mensaje);

            String html = templateEngine.process(
                    "email/contacto",
                    context
            );

            MimeMessage correo =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            correo,
                            true,
                            "UTF-8"
                    );

            helper.setTo(destinatario);

            helper.setSubject(
                    "Nuevo mensaje de contacto - Cancheros"
            );

            helper.setText(html, true);

            mailSender.send(correo);

        } catch (MessagingException e) {

            throw new RuntimeException(
                    "No se pudo enviar el correo de contacto",
                    e
            );
        }
    }
}