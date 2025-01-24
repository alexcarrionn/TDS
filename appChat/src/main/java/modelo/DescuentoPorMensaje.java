package modelo;

public class DescuentoPorMensaje extends Descuento {
    private int cantidadMensajes;
    private int umbralMensajes;
    private final static double porcentajeDescuento = 0.5;
    
    //Constructor
    public DescuentoPorMensaje(int cantidadMensajes, int umbralMensajes) {
        this.cantidadMensajes = cantidadMensajes;
        this.umbralMensajes = umbralMensajes;
    }
    //operacion GetDescuento del descuento por Mensajes enviados
    @Override
    public double getDescuento(double precio) {
        if (cantidadMensajes >= umbralMensajes) {
            return porcentajeDescuento*precio;
        } else {
            return precio; // Sin descuento si no se alcanzó el umbral de mensajes
        }
    }
}