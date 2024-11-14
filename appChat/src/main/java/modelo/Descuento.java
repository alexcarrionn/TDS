package modelo;

public abstract class Descuento {
	
	//Te devuelve el precio tras incluirle el descuento oportuno al descuento inicial
	public abstract double getDescuento(double precio); 
}
