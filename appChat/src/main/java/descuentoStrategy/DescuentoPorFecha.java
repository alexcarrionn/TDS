package descuentoStrategy;

/**
 * Esta clase te va a permitir aplicar el descuento correspondiente a si un usuario cumple los requisitos de los plazos de fecha 
 */

public class DescuentoPorFecha implements Descuento {
    
	/**
	 * Porcentaje de descuento aplicado a productos en promoción (30% de descuento).
	 */
	private final static double porcentajeDescuento = 0.7;
    
    /**
     * Método Para conseguir el descuento por fecha
     */
    
    public double getDescuento(double precio) {
            return porcentajeDescuento*precio;
    }
}