package modelo;

import java.time.LocalDate;

public class DescuentoPorFecha extends Descuento {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDescuento;

    //Constructor
    public DescuentoPorFecha(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeDescuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDescuento = porcentajeDescuento;
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