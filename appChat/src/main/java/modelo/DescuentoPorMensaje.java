package modelo;

public class DescuentoPorMensaje extends Descuento {
    private final static double porcentajeDescuento = 0.5;
    //operacion GetDescuento del descuento por Mensajes enviados
    @Override
    public double getDescuento(double precio) {
            return porcentajeDescuento*precio;
    }
}