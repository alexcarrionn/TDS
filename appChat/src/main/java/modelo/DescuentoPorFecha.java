package modelo;


public class DescuentoPorFecha extends Descuento {
    private final static double porcentajeDescuento = 0.7;
    //Para conseguir el descuento por fecha
    @Override
    public double getDescuento(double precio) {
            return porcentajeDescuento*precio;
    }
}