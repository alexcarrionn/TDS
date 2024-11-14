package modelo;

public class DescuentoPorMensaje extends Descuento {
    private int cantidadMensajes;
    private int umbralMensajes;
    private double porcentajeDescuento;

    public DescuentoPorMensaje(int cantidadMensajes, int umbralMensajes, double porcentajeDescuento) {
        this.cantidadMensajes = cantidadMensajes;
        this.umbralMensajes = umbralMensajes;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double getDescuento() {
        if (cantidadMensajes >= umbralMensajes) {
            return porcentajeDescuento;
        } else {
            return 0.0; // Sin descuento si no se alcanzó el umbral de mensajes
        }
    }
}