package service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dto.NotificationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import service.interfaces.NotificationService;

/**
 * Servicio de notificación para enviar mensajes SMS utilizando Twilio.
 * Este servicio usa la API de Twilio para enviar mensajes de texto a los números
 * de teléfono proporcionados en el objeto `NotificationMessage`.
 * <p>
 * Se configura utilizando las credenciales de Twilio almacenadas en las propiedades
 * de la configuración.
 */
@ApplicationScoped
public class SmsNotificationService implements NotificationService {

    private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(SmsNotificationService.class);

    @Inject
    Config config;

    /**
     * Obtiene el tipo de notificación que este servicio maneja, en este caso "SMS".
     *
     * @return El tipo de notificación, "SMS".
     */
    @Override
    public String getType() {
        return "SMS";
    }

    /**
     * Envía un mensaje SMS utilizando la API de Twilio.
     * Utiliza las credenciales de Twilio y el número de teléfono configurado
     * en las propiedades para enviar el mensaje a través de Twilio.
     *
     * @param message El objeto `NotificationMessage` que contiene la información
     *                del mensaje, como el destinatario y el cuerpo del mensaje.
     */
    @Override
    public void send(NotificationMessage message) {
        LOGGER.info("Enviando mensaje SMS.");

        try {
            // Inicializar Twilio con las credenciales desde la configuración
            Twilio.init(
                    config.getValue("twilio.accountSid", String.class),
                    config.getValue("twilio.authToken", String.class)
            );

            // Crear y enviar el mensaje SMS
            Message.creator(
                    new PhoneNumber(message.to()),
                    new PhoneNumber(config.getValue("twilio.phoneNumber", String.class)),
                    message.body()
            ).create();

            // Log para confirmar el envío
            LOGGER.infof("[SMS] Mensaje enviado a: %s", message.to());

        } catch (Exception e) {
            // Log de error en caso de excepción
            LOGGER.error("Error al enviar el mensaje SMS.", e);

            // Log de auditoría para el error
            LOGGER.info("[AUDITORÍA] Error al enviar mensaje SMS: %s");
        }
    }
}
