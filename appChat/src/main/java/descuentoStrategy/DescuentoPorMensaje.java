package descuentoStrategy;

/**
 * Esta clase te va a permitir aplicar el escuento correspondiente a los usuarios que cumplan la condición que se pida, 
 * en este caso haber mandado mas de 100 mensajes
 */
public class DescuentoPorMensaje implements Descuento {
	/**
	 * Porcentaje de descuento aplicado a productos en promoción (30% de descuento).
	 */
	private final static double porcentajeDescuento = 0.5;
    /**
     * Método GetDescuento del descuento por Mensajes enviados
     */
    public double getDescuento(double precio) {
            return porcentajeDescuento*precio;
    }
}