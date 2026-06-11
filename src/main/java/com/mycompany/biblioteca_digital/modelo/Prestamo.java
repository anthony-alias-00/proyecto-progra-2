package com.mycompany.biblioteca_digital.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo{
    
    private int idPrestamo;
    private Usuario usuario;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado; 
    
    // Constructor vacío
    public Prestamo() {
    this.estado = "ACTIVO";
}
    public Prestamo(Usuario usuario, Libro libro) {
    this.usuario = usuario;
    this.libro = libro;
    this.estado = "ACTIVO";
}
    
    public Prestamo(Usuario usuario, Libro libro, LocalDate fechaPrestamo, int diasPrestamo) {
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaPrestamo.plusDays(diasPrestamo);
        this.fechaDevolucionReal = null;
        this.estado = "ACTIVO";
    }

    public int getIdPrestamo(){
        return idPrestamo;
    }
    public void setIdPrestamo(int idPrestamo){
        this.idPrestamo = idPrestamo;
    }
    
    public Usuario getUsuario(){
        return usuario;
    }
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }
    
    public Libro getLibro(){
        return libro;
    }
    public void setLibro(Libro libro){
        this.libro = libro;
    }
    
    public LocalDate getFechaPrestamo(){
        return fechaPrestamo;
    }
    public void setFechaPrestamo(LocalDate fechaPrestamo){
        this.fechaPrestamo = fechaPrestamo;
    }
    
    public LocalDate getFechaDevolucionEsperada(){
        return fechaDevolucionEsperada;
    }
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada){
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }
    
    public LocalDate getFechaDevolucionReal(){
        return fechaDevolucionReal;
    }
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal){
        this.fechaDevolucionReal = fechaDevolucionReal;
    }
    
    public String getEstado(){
        return estado;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public boolean estaVencido() {
        if (fechaDevolucionReal != null) {
            return false;
        }
        return LocalDate.now().isAfter(fechaDevolucionEsperada);
    }
    
    public long getDiasRestantes() {
        if (fechaDevolucionReal != null) {
            return 0; 
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaDevolucionEsperada);
    }
    
    public long getDiasVencidos() {
        if (!estaVencido()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fechaDevolucionEsperada, LocalDate.now());
    }
    
    public void devolver() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = "DEVUELTO";
        
        if (libro != null) {
            libro.devolver();
        }
        if (usuario != null) {
            usuario.decrementarLibrosPrestados();
        }
    }
    
    public void actualizarEstado() {
        if (fechaDevolucionReal != null) {
            estado = "DEVUELTO";
        } else if (estaVencido()) {
            estado = "VENCIDO";
        } else {
            estado = "ACTIVO";
        }
    }
    
    @Override
    public String toString() {
        return "Prestamo{" +
                "idPrestamo:" + idPrestamo +
                ", usuario:" + (usuario != null ? usuario.getUsuario() : "null") +
                ", libro:" + (libro != null ? libro.getTitulo() : "null") +
                ", fechaPrestamo:" + fechaPrestamo +
                ", fechaDevolucionEsperada:" + fechaDevolucionEsperada +
                ", fechaDevolucionReal:" + fechaDevolucionReal +
                ", estado:'" + estado + '\'' +
                '}';
    }
}