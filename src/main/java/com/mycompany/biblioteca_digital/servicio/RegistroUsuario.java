package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class RegistroUsuario {
    // Lista temporal de usuarios (
    private List<Usuario> usuarios;
    
    public RegistroUsuario() {
        this.usuarios = new ArrayList<>();
    }
    
    /**
     * Registrar un nuevo usuario
     */
    public boolean registrarUsuario(Usuario usuario) {
        // Validar que no exista la cedula
        if (buscarPorCedula(usuario.getCedula()) != null) {
            System.out.println("Error: Ya existe un usuario con esa cedula");
            return false;
        }
        
        // Validar que no exista el nombre de usuario
        if (buscarPorNombreUsuario(usuario.getUsuario()) != null) {
            System.out.println("Error: El nombre de usuario ya este en uso");
            return false;
        }
        
        // Validar datos
        if (!validarDatosUsuario(usuario)) {
            return false;
        }
        
        // Agregar a la lista 
        usuarios.add(usuario);
        System.out.println("Usuario registrado exitosamente: " + usuario.getNombre());
        return true;
    }
    
    /**
     * Validar login de usuario
     */
    public Usuario login(String nombreUsuario, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(nombreUsuario) && 
                usuario.getContraseña().equals(contraseña)) {
                
                // Verificar que esté activo
                if (!usuario.isActivo()) {
                    System.out.println("Error: Usuario inactivo");
                    return null;
                }
                
                System.out.println("Login exitoso: " + usuario.getNombre());
                return usuario;
            }
        }
        
        System.out.println("Error: Usuario o contraseña incorrectos");
        return null;
    }
    
    /**
     * Buscar usuario por cedula
     */
    public Usuario buscarPorCedula(String cedula) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(cedula)) {
                return usuario;
            }
        }
        return null;
    }
    
    /**
     * Buscar usuario por nombre de usuario
     */
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(nombreUsuario)) {
                return usuario;
            }
        }
        return null;
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * Actualizar informacion de usuario
     */
    public boolean actualizarUsuario(Usuario usuario) {
        Usuario existente = buscarPorCedula(usuario.getCedula());
        
        if (existente == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        
        // Actualizar datos
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
    
    /**
     * Desactivar usuario 
     */
    public boolean desactivarUsuario(String cedula) {
        Usuario usuario = buscarPorCedula(cedula);
        
        if (usuario == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        
        // Verificar que no tenga libros prestados
        if (usuario.getLibrosPrestados() > 0) {
            System.out.println("Error: El usuario tiene libros prestados");
            return false;
        }
        
        usuario.setActivo(false);
        System.out.println("Usuario desactivado: " + usuario.getNombre());
        return true;
    }
    
    /**
     * Validar datos de usuario
     */
    private boolean validarDatosUsuario(Usuario usuario) {
        // Validar cedula
        if (usuario.getCedula() == null || usuario.getCedula().length() != 10) {
            System.out.println("Error: La cedula debe tener 10 digitos");
            return false;
        }
        
        // Validar nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            System.out.println("Error: El nombre es obligatorio");
            return false;
        }
        
        // Validar apellido
        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            System.out.println("Error: El apellido es obligatorio");
            return false;
        }
        
        // Validar email
        if (usuario.getMail() == null || !usuario.getMail().contains("@")) {
            System.out.println("Error: El email es invalido");
            return false;
        }
        
        // Validar nombre de usuario
        if (usuario.getUsuario() == null || usuario.getUsuario().length() < 4) {
            System.out.println("Error: El nombre de usuario debe tener al menos 4 caracteres");
            return false;
        }
        
        // Validar contraseña
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 6) {
            System.out.println("Error: La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        return true;
    }
    
    /**
     * Cambiar contraseña
     */
    public boolean cambiarContraseña(String cedula, String contraseñaActual, String contraseñaNueva) {
        Usuario usuario = buscarPorCedula(cedula);
        
        if (usuario == null) {
            System.out.println("Error: Usuario no encontrado");
            return false;
        }
        
        // Verificar contraseña actual
        if (!usuario.getContraseña().equals(contraseñaActual)) {
            System.out.println("Error: Contraseña actual incorrecta");
            return false;
        }
        
        // Validar nueva contraseña
        if (contraseñaNueva.length() < 6) {
            System.out.println("Error: La nueva contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        usuario.setContraseña(contraseñaNueva);
        System.out.println("Contraseña actualizada para: " + usuario.getNombre());
        return true;
    }
}