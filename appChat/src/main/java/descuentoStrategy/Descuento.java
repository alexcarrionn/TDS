package descuentoStrategy;

public /*Interface */abstract class Descuento {
	
	//Te devuelve el precio tras incluirle el descuento oportuno al descuento inicial
	public /* NO abstract*/ abstract double getDescuento(double precio); 
}
