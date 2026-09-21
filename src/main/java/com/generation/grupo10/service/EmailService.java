package com.generation.grupo10.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final ResendService resendService;
    private final SpringTemplateEngine templateEngine;

    public void enviarCodigo(String destinatario, String codigo) {

        Context context = new Context();
        context.setVariable("codigo", codigo);

        String html = templateEngine.process(
                "email/verificacion",
                context
        );

        resendService.enviarCorreo(
                destinatario,
                "Código de verificación - Cancheros",
                html
        );
    }

    public void enviarEnlaceRecuperacion(
            String destinatario,
            String enlace
    ) {

        Context context = new Context();
        context.setVariable("enlace", enlace);

        String html = templateEngine.process(
                "email/recuperacion",
                context
        );

        resendService.enviarCorreo(
                destinatario,
                "Recuperación de contraseña - Cancheros",
                html
        );
    }

    public void enviarEnlaceCambioPassword(
            String destinatario,
            String enlace
    ) {

        Context context = new Context();
        context.setVariable("enlace", enlace);

        String html = templateEngine.process(
                "email/cambio-password",
                context
        );

        resendService.enviarCorreo(
                destinatario,
                "Validación de cambio de contraseña - Cancheros",
                html
        );
    }

    public void enviarEnlaceEdicionPerfil(
            String destinatario,
            String enlace
    ) {

        Context context = new Context();
        context.setVariable("enlace", enlace);

        String html = templateEngine.process(
                "email/edicion-perfil",
                context
        );

        resendService.enviarCorreo(
                destinatario,
                "Validación de edición de perfil - Cancheros",
                html
        );
    }

    public void enviarContacto(
            String destinatario,
            String nombre,
            String email,
            String telefono,
            String mensaje
    ) {

        Context context = new Context();

        context.setVariable("nombre", nombre);
        context.setVariable("email", email);
        context.setVariable("telefono", telefono);
        context.setVariable("mensaje", mensaje);

        String html = templateEngine.process(
                "email/contacto",
                context
        );

        resendService.enviarCorreo(
                destinatario,
                "Nuevo mensaje de contacto - Cancheros",
                html
        );
    }
}