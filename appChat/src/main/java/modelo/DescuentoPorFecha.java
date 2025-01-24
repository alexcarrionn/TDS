package modelo;

import java.time.LocalDate;

public class DescuentoPorFecha extends Descuento {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private final static double porcentajeDescuento = 0.7;

    //Constructor
    public DescuentoPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    //Para conseguir el descuento por fecha
    @Override
    public double getDescuento(double precio) {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaInicio) && hoy.isBefore(fechaFin)) {
            return porcentajeDescuento*precio;
        } else {
            return precio; // Sin descuento fuera del rango de fechas
        }
    }
}