package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.NotificationMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * El `NotificationListener` es un servicio que escucha los mensajes entrantes
 * a través del canal de mensajes reactivos y los procesa para enviarlos a través
 * del despachador de notificaciones adecuado.
 * <p>
 * Utiliza `ObjectMapper` para convertir los mensajes JSON a objetos de tipo
 * `NotificationMessage` y luego los despacha al servicio adecuado utilizando
 * el `NotificationDispatcher`.
 */
@ApplicationScoped
public class NotificationListener {

    private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(NotificationListener.class);

    @Inject
    NotificationDispatcher dispatcher;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Método que recibe un mensaje de tipo JSON desde el canal de notificaciones
     * y lo convierte en un objeto `NotificationMessage`, luego lo despacha al servicio
     * adecuado para su envío.
     *
     * @param json El mensaje de notificación en formato JSON.
     */
    @Incoming("notifications")
    public void receive(String json) {
        LOGGER.info("Recibiendo mensaje de notificación.");

        try {
            // Convertir el JSON recibido en un objeto NotificationMessage
            NotificationMessage msg = mapper.readValue(json, NotificationMessage.class);
            LOGGER.debugf("Mensaje recibido: %s", msg);

            // Despachar el mensaje al servicio adecuado
            dispatcher.dispatch(msg);
            LOGGER.info("Mensaje despachado con éxito.");

        } catch (Exception e) {
            LOGGER.error("Error al procesar el mensaje de notificación.", e);
            // Log de auditoría para errores
            LOGGER.info("[AUDITORÍA] Error al procesar mensaje de notificación: %s");
        }
    }
}
