package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Prestamo;
import com.mycompany.biblioteca_digital.modelo.Usuario;
import com.mycompany.biblioteca_digital.modelo.Libro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrestamoControl {
    // Lista temporal de prestamos
    private List<Prestamo> prestamos;
    private int contadorId;
    
    // Dias de prestamo por defecto
    private static final int DIAS_PRESTAMO_DEFECTO = 7;
    
    // Constructor
    public PrestamoControl() {
        this.prestamos = new ArrayList<>();
        this.contadorId = 1;
    }
    
    /**
     * Realizar un prestamo
     */
    public Prestamo realizarPrestamo(Usuario usuario, Libro libro) {
        return realizarPrestamo(usuario, libro, DIAS_PRESTAMO_DEFECTO);
    }
    
    /**
     * Realizar un prestamo con dias personalizados
     */
    public Prestamo realizarPrestamo(Usuario usuario, Libro libro, int diasPrestamo) {
        // Validación 1: Usuario puede pedir libros
        if (!usuario.puedePedirLibros()) {
            System.out.println("Error: El usuario no puede pedir mas libros");
            System.out.println("Razon: " + obtenerRazonRechazo(usuario));
            return null;
        }
        
        // Validación 2: Libro esta disponible
        if (!libro.estaDisponible()) {
            System.out.println("Error: El libro no esta disponible");
            return null;
        }
        
        // Validación 3: Usuario no tiene prestamos vencidos
        if (tienePrestamosVencidos(usuario)) {
            System.out.println("Error: El usuario tiene prestamos vencidos");
            return null;
        }
        
        // Crear el prestamo
        Prestamo prestamo = new Prestamo(usuario, libro, LocalDate.now(), diasPrestamo);
        prestamo.setIdPrestamo(contadorId++);
        
        // Actualizar libro
        libro.prestar();
        
        // Actualizar usuario
        usuario.incrementarLibrosPrestados();
        
        // Guardar préstamo
        prestamos.add(prestamo);
        
        System.out.println("Prestamo realizado exitosamente");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Libro: " + libro.getTitulo());
        System.out.println("Fecha devolucion esperada: " + prestamo.getFechaDevolucionEsperada());
        
        return prestamo;
    }
    
    /**
     * Devolver un libro
     */
    public boolean devolverLibro(int idPrestamo) {
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);
        
        if (prestamo == null) {
            System.out.println("Error: Prestamo no encontrado");
            return false;
        }
        
        if (prestamo.getFechaDevolucionReal() != null) {
            System.out.println("Error: Este libro ya fue devuelto");
            return false;
        }
        
        // Marcar como devuelto
        prestamo.devolver();
        
        // Calcular si hubo retraso
        if (prestamo.getDiasVencidos() > 0) {
            System.out.println("Advertencia: El libro fue devuelto con " + 
                             prestamo.getDiasVencidos() + " dias de retraso");
        }
        
        System.out.println("Libro devuelto exitosamente");
        System.out.println("Usuario: " + prestamo.getUsuario().getNombre());
        System.out.println("Libro: " + prestamo.getLibro().getTitulo());
        System.out.println("Fecha devolucion: " + prestamo.getFechaDevolucionReal());
        
        return true;
    }
    
    /**
     * Buscar prestamo por ID
     */
    public Prestamo buscarPrestamoPorId(int idPrestamo) {
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getIdPrestamo() == idPrestamo) {
                return prestamo;
            }
        }
        return null;
    }
    
    /**
     * Obtener prestamos activos de un usuario
     */
    public List<Prestamo> obtenerPrestamosActivosUsuario(Usuario usuario) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().getCedula().equals(usuario.getCedula()))
                .filter(p -> p.getFechaDevolucionReal() == null)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener prestamos de un libro específico
     */
    public List<Prestamo> obtenerPrestamosLibro(Libro libro) {
        return prestamos.stream()
                .filter(p -> p.getLibro().getIsbn().equals(libro.getIsbn()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener todos los prestamos activos
     */
    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamos.stream()
                .filter(p -> p.getFechaDevolucionReal() == null)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener prestamos vencidos
     */
    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamos.stream()
                .filter(Prestamo::estaVencido)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener prestamos devueltos
     */
    public List<Prestamo> obtenerPrestamosDevueltos() {
        return prestamos.stream()
                .filter(p -> p.getFechaDevolucionReal() != null)
                .collect(Collectors.toList());
    }
    
    /**
     * Verificar si un usuario tiene prestamos vencidos
     */
    public boolean tienePrestamosVencidos(Usuario usuario) {
        return prestamos.stream()
                .anyMatch(p -> p.getUsuario().getCedula().equals(usuario.getCedula()) && 
                              p.estaVencido());
    }
    
    /**
     * Renovar un prestamo 
     */
    public boolean renovarPrestamo(int idPrestamo, int diasAdicionales) {
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);
        
        if (prestamo == null) {
            System.out.println("Error: Préstamo no encontrado");
            return false;
        }
        
        if (prestamo.getFechaDevolucionReal() != null) {
            System.out.println("Error: El libro ya fue devuelto");
            return false;
        }
        
        if (prestamo.estaVencido()) {
            System.out.println("Error: No se puede renovar un préstamo vencido");
            return false;
        }
        
        // Extender fecha
        LocalDate nuevaFecha = prestamo.getFechaDevolucionEsperada().plusDays(diasAdicionales);
        prestamo.setFechaDevolucionEsperada(nuevaFecha);
        
        System.out.println("Prestamo renovado exitosamente");
        System.out.println("Nueva fecha de devolucion: " + nuevaFecha);
        
        return true;
    }
    
    /**
     * Obtener historial completo de prestamos de un usuario
     */
    public List<Prestamo> obtenerHistorialUsuario(Usuario usuario) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().getCedula().equals(usuario.getCedula()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener usuario no puede pedir libros
     */
    private String obtenerRazonRechazo(Usuario usuario) {
        if (!usuario.isActivo()) {
            return "Usuario inactivo";
        }
        if (usuario.getLibrosPrestados() >= usuario.getMaxLibrosPrestamo()) {
            return "Ha alcanzado el limite de " + usuario.getMaxLibrosPrestamo() + " libros";
        }
        return "Razon desconocida";
    }
    
    /**
     * Obtener estadisticas generales de prestamos
     */
    public String obtenerEstadisticas() {
        int totalPrestamos = prestamos.size();
        int prestamosActivos = obtenerPrestamosActivos().size();
        int prestamosVencidos = obtenerPrestamosVencidos().size();
        int prestamosDevueltos = obtenerPrestamosDevueltos().size();
        
        return "Total de prestamos: " + totalPrestamos + "\n" +
               "Prestamos activos: " + prestamosActivos + "\n" +
               "Prestamos vencidos: " + prestamosVencidos + "\n" +
               "Prestamos devueltos: " + prestamosDevueltos;
    }
    
    /**
     * Actualizar estado de todos los prestamos
     */
    public void actualizarEstadosPrestamos() {
        for (Prestamo prestamo : prestamos) {
            prestamo.actualizarEstado();
        }
    }
    
    /**
     * Obtener prestamos que vencen pronto 
     */
    public List<Prestamo> obtenerPrestamosProximosAVencer(int dias) {
        return prestamos.stream()
                .filter(p -> p.getFechaDevolucionReal() == null)
                .filter(p -> !p.estaVencido())
                .filter(p -> p.getDiasRestantes() <= dias && p.getDiasRestantes() > 0)
                .collect(Collectors.toList());
    }
    
    /**
     * Verificar si un usuario puede renovar un prestamo
     */
    public boolean puedeRenovar(int idPrestamo) {
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);
        
        if (prestamo == null || prestamo.getFechaDevolucionReal() != null) {
            return false;
        }
        
        // No puede renovar si esta vencido
        if (prestamo.estaVencido()) {
            return false;
        }
        
        return true;
    }
}