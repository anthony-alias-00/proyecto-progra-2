package com.mycompany.biblioteca_digital.modelo;

public class Libro{
   
    private int idLibro;
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private int año;
    private String categoria;
    private int cantidadTotal;
    private int cantidadDisponible;
    private String ubicacion;
    private boolean activo;
    
    public Libro(){
        this.activo = true;
    }
    
    public Libro(String isbn, String titulo, String autor, String editorial, 
                 int año, String categoria, int cantidadTotal, String ubicacion) {
        
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.año = año;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadTotal;
        this.ubicacion = ubicacion;
        this.activo = true;
    }
    
    public int getIdLibro(){
        return idLibro;
    }
    public void setIdLibro(int idLibro){
        this.idLibro = idLibro;
    }
    
    public String getIsbn(){
        return isbn;
    }
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    
    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }
    
    public String getAutor(){
        return autor;
    }
    public void setAutor(String autor){
        this.autor = autor;
    }
    
    public String getEditorial(){
        return editorial;
    }
    public void setEditorial(String editorial){
        this.editorial = editorial;
    }
    
    public int getAño(){
        return año;
    }
    public void setAño(int año){
        this.año = año;
    }
    
    public String getCategoria(){
        return categoria;
    }
   public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    
    public int getCantidadTotal(){
        return cantidadTotal;
    }
    public void setCantidadTotal(int cantidadTotal){
        this.cantidadTotal = cantidadTotal;
    }
    
    public int getCantidadDisponible(){
        return cantidadDisponible;
    }
    public void setCantidadDisponible(int cantidadDisponible){
        this.cantidadDisponible = cantidadDisponible;
    }
    
    public String getUbicacion(){
        return ubicacion;
    }
    public void setUbicacion(String ubicacion){
        this.ubicacion = ubicacion;
    }
    
    public boolean isActivo(){
        return activo;
    }
    public void setActivo(boolean activo){
        this.activo = activo;
    }
    
    public boolean estaDisponible() {
        return activo && cantidadDisponible > 0;
    }
    
    public boolean prestar() {
        if (estaDisponible()) {
            cantidadDisponible--;
            return true;
        }
        return false;
    }
    
    public void devolver() {
        if (cantidadDisponible < cantidadTotal) {
            cantidadDisponible++;
        }
    }
    
    @Override
    public String toString() {
        return "Libro{" +
                "isbn:'" + isbn + '\'' +
                ", titulo:'" + titulo + '\'' +
                ", autor:'" + autor + '\'' +
                ", editorial:'" + editorial + '\'' +
                ", año:" + año +
                ", categoria:'" + categoria + '\'' +
                ", cantidadDisponible:" + cantidadDisponible +
                ", cantidadTotal:" + cantidadTotal +
                ", activo:" + activo +
                '}';
    }
}