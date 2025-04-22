package service;

import dto.NotificationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import service.interfaces.NotificationService;
import java.util.Properties;
import org.eclipse.microprofile.config.Config;
import org.jboss.logging.Logger;


/**
 * Servicio de notificación encargado del envío de correos electrónicos.
 * Implementa la interfaz {@link NotificationService}.
 */
@ApplicationScoped
public class EmailNotificationService implements NotificationService {

    @Inject
    Config config;

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationService.class);

    /**
     * Retorna el tipo de canal de notificación implementado.
     *
     * @return el string "EMAIL".
     */
    @Override
    public String getType() {
        return "EMAIL";
    }

    /**
     * Envía un mensaje de notificación por correo electrónico utilizando la configuración
     * definida en el archivo de propiedades.
     *
     * @param message El mensaje de notificación que contiene destinatario, asunto y cuerpo.
     */
    @Override
    public void send(NotificationMessage message) {
        LOGGER.debug("Iniciando envío de correo electrónico a: " + message.to());

        try {
            String username = config.getValue("mail.username", String.class);
            String password = config.getValue("mail.password", String.class);

            Properties props = new Properties();
            props.put("mail.smtp.auth", config.getValue("mail.smtp.auth", String.class));
            props.put("mail.smtp.starttls.enable", config.getValue("mail.smtp.starttls.enable", String.class));
            props.put("mail.smtp.host", config.getValue("mail.smtp.host", String.class));
            props.put("mail.smtp.port", config.getValue("mail.smtp.port", String.class));

            LOGGER.debug("Propiedades SMTP configuradas correctamente");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress(username));
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.to()));
            email.setSubject(message.subject());
            email.setText(message.body());

            Transport.send(email);

            LOGGER.infof("[AUDITORÍA] Email enviado a: %s, Asunto: %s", message.to(), message.subject());
            LOGGER.debug("Correo enviado exitosamente");

        } catch (Exception e) {
            LOGGER.errorf(e, "Error al enviar correo a %s", message.to());
        }
    }
}
