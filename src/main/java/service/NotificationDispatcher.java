package service;

import dto.NotificationMessage;
import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import service.interfaces.NotificationService;

import java.util.List;

/**
 * El despachador de notificaciones gestiona el envío de mensajes
 * utilizando el servicio adecuado en función del tipo de notificación.
 * <p>
 * Este servicio filtra la lista de servicios disponibles para encontrar
 * el servicio que coincida con el tipo de notificación y lo utiliza
 * para enviar el mensaje.
 */
@ApplicationScoped
public class NotificationDispatcher {

    private static final Logger LOGGER = Logger.getLogger(NotificationDispatcher.class);

    private final List<NotificationService> services;

    /**
     * Constructor que inyecta todos los servicios de notificación disponibles.
     *
     * @param services Lista de servicios de notificación disponibles.
     */
    @Inject
    public NotificationDispatcher(@All List<NotificationService> services) {
        this.services = services;
    }

    /**
     * Despacha el mensaje de notificación al servicio adecuado en función
     * del tipo de notificación que se desea enviar.
     *
     * @param message El mensaje de notificación a enviar.
     */
    public void dispatch(NotificationMessage message) {
        LOGGER.infof("Intentando despachar mensaje de tipo: %s", message.type());

        services.stream()
                .filter(s -> s.getType().equalsIgnoreCase(message.type())) // Filtra por tipo
                .findFirst()
                .ifPresentOrElse(
                        service -> {
                            LOGGER.infof("Servicio encontrado para el tipo %s. Enviando mensaje...", message.type());
                            service.send(message);
                            LOGGER.debug("Mensaje enviado exitosamente");
                        },
                        () -> {
                            LOGGER.errorf("No se encontró un servicio para el tipo de mensaje: %s", message.type());
                            // Log de auditoría
                            LOGGER.infof("[AUDITORÍA] No se encontró servicio para tipo: %s", message.type());
                        }
                );
    }
}
