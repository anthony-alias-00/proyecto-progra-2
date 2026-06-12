package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Usuario;
import com.mycompany.biblioteca_digital.modelo.Administrador;
import com.mycompany.biblioteca_digital.modelo.Persona;
import com.mycompany.biblioteca_digital.estructuras.tads.ListaEnlazada;
import java.util.ArrayList;
import java.util.List;

public class RegistroUsuario {

    private ListaEnlazada<Usuario> usuarios;
    private ListaEnlazada<Administrador> administradores;
    private int contadorId;

    public RegistroUsuario() {
        this.usuarios = new ListaEnlazada<>();
        this.administradores = new ListaEnlazada<>();
        this.contadorId = 1;
    }

    // ===================== REGISTRO =====================

    public boolean registrarUsuario(Usuario usuario) {
        if (buscarPorCedula(usuario.getCedula()) != null) {
            System.out.println("Error: Ya existe un usuario con esa cedula");
            return false;
        }
        if (buscarPorNombreUsuario(usuario.getUsuario()) != null) {
            System.out.println("Error: El nombre de usuario ya esta en uso");
            return false;
        }
        if (!validarDatosUsuario(usuario)) {
            return false;
        }
        usuario.setIdPersona(contadorId++);
        usuarios.insertar(usuario);
        System.out.println("Usuario registrado exitosamente: " + usuario.getNombre());
        return true;
    }

    public boolean registrarAdministrador(Administrador admin) {
        if (buscarPorCedula(admin.getCedula()) != null) {
            System.out.println("Error: Ya existe una persona con esa cedula");
            return false;
        }
        admin.setIdPersona(contadorId++);
        administradores.insertar(admin);
        System.out.println("Administrador registrado: " + admin.getNombre());
        return true;
    }

    // ===================== LOGIN =====================

    public Persona login(String nombreUsuario, String contraseña) {
        Usuario usuario = buscarUsuarioPorNombre(nombreUsuario);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            if (!usuario.isActivo()) {
                System.out.println("Error: Usuario inactivo");
                return null;
            }
            System.out.println("Login exitoso: " + usuario.getNombre());
            return usuario;
        }

        Administrador admin = buscarAdminPorNombre(nombreUsuario);
        if (admin != null && admin.getContraseña().equals(contraseña)) {
            if (!admin.isActivo()) {
                System.out.println("Error: Administrador inactivo");
                return null;
            }
            System.out.println("Login exitoso (admin): " + admin.getNombre());
            return admin;
        }

        System.out.println("Error: Usuario o contraseña incorrectos");
        return null;
    }

    // ===================== BÚSQUEDAS PÚBLICAS =====================

    public Persona buscarPorCedula(String cedula) {
        Usuario u = buscarUsuarioPorCedula(cedula);
        if (u != null) return u;
        return buscarAdminPorCedula(cedula);
    }

    public Persona buscarPorNombreUsuario(String nombreUsuario) {
        Usuario u = buscarUsuarioPorNombre(nombreUsuario);
        if (u != null) return u;
        return buscarAdminPorNombre(nombreUsuario);
    }

    public Persona buscarPorId(int idPersona) {
        return buscarPorIdInterno(idPersona);
    }

    // ===================== BÚSQUEDAS PRIVADAS =====================

    private Usuario buscarUsuarioPorCedula(String cedula) {
        for (Usuario u : usuarios.listarTodos()) {
            if (u.getCedula() != null && u.getCedula().equals(cedula)) return u;
        }
        return null;
    }

    private Administrador buscarAdminPorCedula(String cedula) {
        for (Administrador a : administradores.listarTodos()) {
            if (a.getCedula() != null && a.getCedula().equals(cedula)) return a;
        }
        return null;
    }

    private Usuario buscarUsuarioPorNombre(String nombreUsuario) {
        for (Usuario u : usuarios.listarTodos()) {
            if (u.getUsuario() != null && u.getUsuario().equals(nombreUsuario)) return u;
        }
        return null;
    }

    private Administrador buscarAdminPorNombre(String nombreUsuario) {
        for (Administrador a : administradores.listarTodos()) {
            if (a.getUsuario() != null && a.getUsuario().equals(nombreUsuario)) return a;
        }
        return null;
    }

    private Persona buscarPorIdInterno(int id) {
        for (Usuario u : usuarios.listarTodos()) {
            if (u.getIdPersona() == id) return u;
        }
        for (Administrador a : administradores.listarTodos()) {
            if (a.getIdPersona() == id) return a;
        }
        return null;
    }

    // ===================== LISTAR =====================

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarios.listarTodos();
    }

    public List<Administrador> obtenerTodosLosAdministradores() {
        return administradores.listarTodos();
    }

    public List<Persona> obtenerTodos() {
        List<Persona> resultado = new ArrayList<>();
        resultado.addAll(usuarios.listarTodos());
        resultado.addAll(administradores.listarTodos());
        return resultado;
    }

    // ===================== ACTUALIZAR =====================

    public boolean insertar(Usuario usuario) {
        return registrarUsuario(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        Usuario existente = buscarUsuarioPorCedula(usuario.getCedula());
        if (existente == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setMail(usuario.getMail());
        existente.setDireccion(usuario.getDireccion());
        existente.setUsuario(usuario.getUsuario());
        existente.setMaxLibrosPrestamo(usuario.getMaxLibrosPrestamo());
        existente.setActivo(usuario.isActivo());
        System.out.println("Usuario actualizado: " + existente.getNombre());
        return true;
    }

    public boolean cambiarContraseña(String cedula, String contraseñaActual, String contraseñaNueva) {
        Persona persona = buscarPorCedula(cedula);
        if (persona == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        if (!persona.getContraseña().equals(contraseñaActual)) {
            System.out.println("Error: Contraseña actual incorrecta");
            return false;
        }
        if (contraseñaNueva.length() < 6) {
            System.out.println("Error: La nueva contraseña debe tener al menos 6 caracteres");
            return false;
        }
        persona.setContraseña(contraseñaNueva);
        System.out.println("Contraseña actualizada para: " + persona.getNombre());
        return true;
    }

    // ===================== ELIMINAR / DESACTIVAR =====================

    public boolean desactivarUsuario(String cedula) {
        Persona persona = buscarPorCedula(cedula);
        if (persona == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        if (persona instanceof Usuario) {
            Usuario u = (Usuario) persona;
            if (u.getLibrosPrestados() > 0) {
                System.out.println("Error: El usuario tiene libros prestados");
                return false;
            }
        }
        persona.setActivo(false);
        System.out.println("Usuario desactivado: " + persona.getNombre());
        return true;
    }

    public boolean eliminar(int idPersona) {
        Persona persona = buscarPorId(idPersona);
        if (persona == null) return false;
        return desactivarUsuario(persona.getCedula());
    }

    public boolean eliminarUsuarioDefinitivo(String nombreUsuario) {
        Usuario u = buscarUsuarioPorNombre(nombreUsuario);
        if (u == null) return false;
        return usuarios.eliminar(u);
    }

    // ===================== VALIDACIÓN =====================

    private boolean validarDatosUsuario(Usuario usuario) {
        if (usuario.getCedula() == null || usuario.getCedula().length() != 10) {
            System.out.println("Error: La cedula debe tener 10 digitos");
            return false;
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return false;
        }
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            System.out.println("Error: El apellido es obligatorio");
            return false;
        }
        if (usuario.getMail() == null || !usuario.getMail().contains("@")) {
            System.out.println("Error: El email es invalido");
            return false;
        }
        if (usuario.getUsuario() == null || usuario.getUsuario().length() < 4) {
            System.out.println("Error: El nombre de usuario debe tener al menos 4 caracteres");
            return false;
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 6) {
            System.out.println("Error: La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }
}