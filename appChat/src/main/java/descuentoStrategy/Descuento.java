package descuentoStrategy;

/**
 * Esta funcion la vamos a utilizar para aplicar un descuento según el usuario cumpla una serie de condiciones
 */
public interface Descuento {
	
	/**
	 * Te devuelve el precio tras incluirle el descuento oportuno al descuento inicial
	 * @param precio
	 * @return precio que se aplica
	 */
	double getDescuento(double precio); 
}
