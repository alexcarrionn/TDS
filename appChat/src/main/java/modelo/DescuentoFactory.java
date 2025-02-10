package modelo;

//clase factoria que te sirve para poder crear el tipo de descuento que haya seleccionado el usuario
public class DescuentoFactory {
    public static Descuento crearDescuento(String tipo) {
        if ("Descuento Fecha".equals(tipo)) {
            return new DescuentoPorFecha();
        } else {
            return new DescuentoPorMensaje();

}}}