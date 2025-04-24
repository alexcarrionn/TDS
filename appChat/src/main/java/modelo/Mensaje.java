package modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa un mensaje. Los mensajes pueden ser de texto o emoticonos,
 * pueden ser enviados en un grupo o individualmente, y tienen un horario de envío.
 */
public class Mensaje implements Comparable<Mensaje> {
    
    // Atributos
    
    private String texto; // Texto del mensaje
    private TipoMensaje tipo; // Tipo de mensaje (texto, emoticono, etc.)
    private LocalDateTime hora; // Hora en que se envió el mensaje
    private int emoticono; // Emoticono, si el mensaje es de tipo emoticono
    private int id; // Identificador único del mensaje
    private boolean grupo = false; // Indica si el mensaje fue enviado en un grupo
    
    // Constructores
    
    /**
     * Constructor para crear un mensaje de texto.
     * 
     * @param texto Texto del mensaje.
     * @param tipo Tipo de mensaje (normal, emoticono, etc.).
     * @param hora Hora en que se envió el mensaje.
     */
    public Mensaje(String texto, TipoMensaje tipo, LocalDateTime hora) {
        this.texto = texto;
        this.tipo = tipo;
        this.hora = hora;
    }
    
    /**
     * Constructor para crear un mensaje de emoticono.
     * 
     * @param emoticono Código del emoticono.
     * @param tipo Tipo de mensaje (en este caso, emoticono).
     * @param hora Hora en que se envió el mensaje.
     */
    public Mensaje(int emoticono, TipoMensaje tipo, LocalDateTime hora) {
        this.tipo = tipo;
        this.emoticono = emoticono;
        this.hora = hora;
    }
    
    /**
     * Constructor para crear un mensaje con texto y la indicación de si es de grupo.
     * 
     * @param texto Texto del mensaje.
     * @param tipo Tipo de mensaje (normal, emoticono, etc.).
     * @param hora Hora en que se envió el mensaje.
     * @param b Indica si el mensaje fue enviado en un grupo.
     */
    public Mensaje(String texto, TipoMensaje tipo, LocalDateTime hora, boolean b) {
        this.texto = texto;
        this.tipo = tipo;
        this.hora = hora;
        this.grupo = b;
    }
    
    /**
     * Constructor para crear un mensaje con emoticono y la indicación de si es de grupo.
     * 
     * @param emoticono Código del emoticono.
     * @param tipo Tipo de mensaje (en este caso, emoticono).
     * @param hora Hora en que se envió el mensaje.
     * @param b Indica si el mensaje fue enviado en un grupo.
     */
    public Mensaje(int emoticono, TipoMensaje tipo, LocalDateTime hora, boolean b) {
        this.tipo = tipo;
        this.emoticono = emoticono;
        this.hora = hora;
        this.grupo = b;
    }
    
    // Getters y Setters
    
    /**
     * Obtiene el texto del mensaje.
     * 
     * @return Texto del mensaje.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene el tipo del mensaje.
     * 
     * @return Tipo del mensaje.
     */
    public TipoMensaje getTipo() {
        return tipo;
    }

    /**
     * Obtiene la hora en que se envió el mensaje.
     * 
     * @return Hora del mensaje.
     */
    public LocalDateTime getHora() {
        return hora;
    }

    /**
     * Obtiene el emoticono asociado al mensaje, si es de tipo emoticono.
     * 
     * @return Código del emoticono.
     */
    public int getEmoticono() {
        return emoticono;
    }

    /**
     * Obtiene el identificador único del mensaje.
     * 
     * @return Identificador del mensaje.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del mensaje.
     * 
     * @param id Identificador único del mensaje.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Indica si el mensaje fue enviado en un grupo.
     * 
     * @return true si el mensaje fue en un grupo, false si no.
     */
    public boolean isGrupo() {
        return grupo;
    }

    /**
     * Devuelve una representación en cadena del mensaje.
     * 
     * @return Representación en cadena del mensaje.
     */
    @Override
    public String toString() {
        return "Mensaje [texto=" + texto + ", TipoMensaje=" + tipo + ", hora=" + hora
                + ", emoticono=" + emoticono + ", id=" + id + ", grupo=" + grupo + "]";
    }

    /**
     * Compara el mensaje con otro basado en la hora de envío.
     * 
     * @param o El mensaje a comparar.
     * @return Un valor negativo si este mensaje es anterior, un valor positivo si es posterior, o cero si son iguales.
     */
    @Override
    public int compareTo(Mensaje o) {
        return hora.compareTo(o.hora);
    }
}
