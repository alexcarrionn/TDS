package modelo;

import java.time.LocalDate;

public class DescuentoPorFecha extends Descuento {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDescuento;

    public DescuentoPorFecha(LocalDate fechaInicio, LocalDate fechaFin, double porcentajeDescuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double getDescuento() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaInicio) && hoy.isBefore(fechaFin)) {
            return porcentajeDescuento;
        } else {
            return 0.0; // Sin descuento fuera del rango de fechas
        }
    }
}