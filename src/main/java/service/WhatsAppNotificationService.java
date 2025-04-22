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
 * Servicio de notificación para enviar mensajes de WhatsApp utilizando Twilio.
 * Este servicio usa la API de Twilio para enviar mensajes de WhatsApp a los números
 * de teléfono proporcionados en el objeto `NotificationMessage`.
 * <p>
 * Se configura utilizando las credenciales de Twilio y el número de WhatsApp
 * configurado en las propiedades de la configuración.
 */
@ApplicationScoped
public class WhatsAppNotificationService implements NotificationService {

    private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(WhatsAppNotificationService.class);

    @Inject
    Config config;

    /**
     * Obtiene el tipo de notificación que este servicio maneja, en este caso "WHATSAPP".
     *
     * @return El tipo de notificación, "WHATSAPP".
     */
    @Override
    public String getType() {
        return "WHATSAPP";
    }

    /**
     * Envía un mensaje de WhatsApp utilizando la API de Twilio.
     * Utiliza las credenciales de Twilio y el número de WhatsApp configurado
     * en las propiedades para enviar el mensaje a través de Twilio.
     *
     * @param message El objeto `NotificationMessage` que contiene la información
     *                del mensaje, como el destinatario y el cuerpo del mensaje.
     */
    @Override
    public void send(NotificationMessage message) {
        LOGGER.info("Enviando mensaje WhatsApp.");

        try {
            // Inicializar Twilio con las credenciales desde la configuración
            Twilio.init(
                    config.getValue("twilio.accountSid", String.class),
                    config.getValue("twilio.authToken", String.class)
            );

            // Crear y enviar el mensaje de WhatsApp
            Message.creator(
                    new PhoneNumber("whatsapp:" + message.to()),
                    new PhoneNumber(config.getValue("twilio.whatsappNumber", String.class)),
                    message.body()
            ).create();

            // Log para confirmar el envío
            LOGGER.infof("[WHATSAPP] Mensaje enviado a: %s", message.to());

        } catch (Exception e) {
            // Log de error en caso de excepción
            LOGGER.error("Error al enviar el mensaje WhatsApp.", e);

            // Log de auditoría para el error
            LOGGER.info("[AUDITORÍA] Error al enviar mensaje WhatsApp: %s");
        }
    }
}
