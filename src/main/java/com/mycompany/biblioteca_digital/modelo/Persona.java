package com.mycompany.biblioteca_digital.modelo;

public abstract class Persona {
    
    protected int idPersona;
    protected String cedula;
    protected String nombre;
    protected String apellido;
    protected String mail;
    protected String direccion;
    protected String usuario;      
    protected String contraseña;    
    protected boolean activo;
    
    public Persona(){
    } //constructor vacio para unir con la base de datos
    
    public Persona(String cedula, String nombre, String apellido, String mail, String dirrecion){
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.direccion = dirrecion; //Constructor con parametros para prueba
    }
    
    public int getIdPersona() {
        return idPersona;
    }    
    public void setIdPersona(int idPersona){
        this.idPersona = idPersona;
    }
    
    public String getCedula(){
        return cedula;
    }
    public void setCedula(String cedula){
        this.cedula = cedula;
    }
    
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getApellido(){
        return apellido;
    }
    public void setApellido(String apellido){
        this.apellido = apellido;
    }

    public String getMail(){
        return mail;
    }
    public void setMail(String mail){
        this.mail = mail;
    }
    

    public String getDireccion(){
        return direccion;
    }
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public abstract String getTipo();
    
    @Override
    public String toString(){
        return"Persona{" +
                "cedula:'" + cedula + '\'' +
                ", nombre:'" + nombre + '\'' +
                ", apellido.'" + apellido + '\'' +
                ", mail:'" + mail + '\'' +
                '}';
    }
}