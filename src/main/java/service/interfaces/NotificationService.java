package service.interfaces;

import dto.NotificationMessage;

/**
 * Interfaz que define el contrato para los servicios de envío de notificaciones
 * a través de diferentes canales como correo electrónico, SMS o WhatsApp.
 */
public interface NotificationService {

    /**
     * Devuelve el tipo de canal de notificación que implementa el servicio.
     * <p>
     * Los valores posibles son:
     * <ul>
     *     <li><b>EMAIL</b></li>
     *     <li><b>SMS</b></li>
     *     <li><b>WHATSAPP</b></li>
     * </ul>
     *
     * @return el tipo de canal como una cadena en mayúsculas.
     */
    String getType();

    /**
     * Envía el mensaje de notificación utilizando el canal correspondiente.
     *
     * @param message El mensaje de notificación que contiene el destinatario,
     *                el cuerpo del mensaje, y opcionalmente un asunto (para EMAIL).
     */
    void send(NotificationMessage message);
}

