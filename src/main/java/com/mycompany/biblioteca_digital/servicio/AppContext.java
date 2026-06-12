/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.servicio;

/**
 *
 * @author ALEJANDRO
 */


import com.mycompany.biblioteca_digital.modelo.Administrador;

public class AppContext {

    private static AppContext instancia;

    private final RegistroUsuario registroUsuario;
    private final LibroOpciones libroOpciones;
    private final PrestamoControl prestamoControl;

    private AppContext() {
        this.registroUsuario = new RegistroUsuario();
        this.libroOpciones = new LibroOpciones();
        this.prestamoControl = new PrestamoControl();
        cargarDatosIniciales();
    }

    public static AppContext getInstance() {
        if (instancia == null) {
            instancia = new AppContext();
        }
        return instancia;
    }

    private void cargarDatosIniciales() {
        // Admin por defecto para poder hacer login
 

    // ===================== ADMINISTRADOR =====================
    Administrador admin = new Administrador();
    admin.setCedula("0000000000");
    admin.setNombre("Admin");
    admin.setApellido("Principal");
    admin.setMail("admin@biblioteca.com");
    admin.setDireccion("Biblioteca UET");
    admin.setUsuario("admin");
    admin.setContraseña("admin123");
    admin.setActivo(true);
    registroUsuario.registrarAdministrador(admin);

    // ===================== USUARIOS =====================
    com.mycompany.biblioteca_digital.modelo.Usuario u1 = new com.mycompany.biblioteca_digital.modelo.Usuario();
    u1.setCedula("1234567890");
    u1.setNombre("Juan");
    u1.setApellido("Pérez");
    u1.setMail("juan@mail.com");
    u1.setDireccion("Quito");
    u1.setUsuario("juan123");
    u1.setContraseña("juan123");
    u1.setActivo(true);
    registroUsuario.registrarUsuario(u1);

    com.mycompany.biblioteca_digital.modelo.Usuario u2 = new com.mycompany.biblioteca_digital.modelo.Usuario();
    u2.setCedula("0987654321");
    u2.setNombre("Maria");
    u2.setApellido("García");
    u2.setMail("maria@mail.com");
    u2.setDireccion("Quito");
    u2.setUsuario("maria123");
    u2.setContraseña("maria123");
    u2.setActivo(true);
    registroUsuario.registrarUsuario(u2);

    // ===================== LIBROS =====================
    com.mycompany.biblioteca_digital.modelo.Libro l1 = new com.mycompany.biblioteca_digital.modelo.Libro();
    l1.setIsbn("9780061965784");
    l1.setTitulo("Clean Code");
    l1.setAutor("Robert Martin");
    l1.setEditorial("Prentice Hall");
    l1.setAño(2008);
    l1.setCategoria("Programación");
    l1.setUbicacion("A1");
    l1.setCantidadTotal(5);
    l1.setCantidadDisponible(5);
    l1.setActivo(true);
    libroOpciones.agregarLibro(l1);

    com.mycompany.biblioteca_digital.modelo.Libro l2 = new com.mycompany.biblioteca_digital.modelo.Libro();
    l2.setIsbn("9780132350884");
    l2.setTitulo("The Pragmatic Programmer");
    l2.setAutor("Andrew Hunt");
    l2.setEditorial("Addison Wesley");
    l2.setAño(1999);
    l2.setCategoria("Programación");
    l2.setUbicacion("A2");
    l2.setCantidadTotal(3);
    l2.setCantidadDisponible(3);
    l2.setActivo(true);
    libroOpciones.agregarLibro(l2);

    com.mycompany.biblioteca_digital.modelo.Libro l3 = new com.mycompany.biblioteca_digital.modelo.Libro();
    l3.setIsbn("9780201633610");
    l3.setTitulo("Design Patterns");
    l3.setAutor("Gang of Four");
    l3.setEditorial("Addison Wesley");
    l3.setAño(1994);
    l3.setCategoria("Programación");
    l3.setUbicacion("A3");
    l3.setCantidadTotal(4);
    l3.setCantidadDisponible(4);
    l3.setActivo(true);
    libroOpciones.agregarLibro(l3);

    }

    public RegistroUsuario getRegistroUsuario() { return registroUsuario; }
    public LibroOpciones getLibroOpciones() { return libroOpciones; }
    public PrestamoControl getPrestamoControl() { return prestamoControl; }
}
