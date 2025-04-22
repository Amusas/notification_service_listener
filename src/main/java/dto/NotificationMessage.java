package dto;

/**
 * Representa un mensaje de notificación genérico que será enviado a través de distintos canales.
 * <p>
 * Este record encapsula la estructura básica necesaria para enviar una notificación por
 * correo electrónico, SMS o WhatsApp.
 * </p>
 *
 * @param type    El tipo de canal de notificación. Los valores válidos son:
 *                <ul>
 *                    <li><b>EMAIL</b>: Correo electrónico</li>
 *                    <li><b>SMS</b>: Mensaje de texto</li>
 *                    <li><b>WHATSAPP</b>: Mensaje de WhatsApp</li>
 *                </ul>
 * @param to      El destinatario del mensaje. Para EMAIL es la dirección de correo electrónico;
 *                para SMS y WhatsApp, es el número de teléfono en formato internacional.
 * @param subject El asunto del mensaje. Solo se utiliza cuando {@code type} es EMAIL.
 * @param body    El contenido principal del mensaje a enviar.
 */
public record NotificationMessage(
        String type,
        String to,
        String subject,
        String body
) {
}

