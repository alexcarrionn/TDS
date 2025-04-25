package persistencia;

import modelo.Mensaje;

/**
 * Interfaz del Adaptador DAO (Data Access Object) para la clase {@link Mensaje}.
 * Define las operaciones básicas de persistencia necesarias para manejar objetos de tipo Mensaje.
 * Forma parte del patrón DAO que separa la lógica de acceso a datos de la lógica de negocio.
 */
public interface IAdaptadorMensajeDAO {

    /**
     * Registra un nuevo mensaje en el sistema de persistencia.
     * 
     * @param mensaje el mensaje a registrar
     */
    public void registrarMensaje(Mensaje mensaje);

    /**
     * Recupera un mensaje a partir de su código identificador único.
     * 
     * @param codigo el código del mensaje
     * @return el objeto {@link Mensaje} correspondiente al código, o {@code null} si no se encuentra
     */
    public Mensaje recuperarMensaje(int codigo);
}
