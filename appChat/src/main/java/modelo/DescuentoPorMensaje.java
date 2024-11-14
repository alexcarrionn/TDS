package modelo;

public class DescuentoPorMensaje extends Descuento {
    private int cantidadMensajes;
    private int umbralMensajes;
    private double porcentajeDescuento;
    
    //Constructor
    public DescuentoPorMensaje(int cantidadMensajes, int umbralMensajes, double porcentajeDescuento) {
        this.cantidadMensajes = cantidadMensajes;
        this.umbralMensajes = umbralMensajes;
        this.porcentajeDescuento = porcentajeDescuento;
    }
    //operacion GetDescuento del descuento por Mensajes enviados
    @Override
    public double getDescuento(double precio) {
        if (cantidadMensajes >= umbralMensajes) {
            return porcentajeDescuento*precio;
        } else {
            return 0.0; // Sin descuento si no se alcanzó el umbral de mensajes
        }
    }
}