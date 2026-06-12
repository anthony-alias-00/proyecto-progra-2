package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Prestamo;
import com.mycompany.biblioteca_digital.modelo.Usuario;
import com.mycompany.biblioteca_digital.modelo.Libro;
import com.mycompany.biblioteca_digital.estructuras.tads.Cola;
import com.mycompany.biblioteca_digital.estructuras.tads.Pila;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
public class PrestamoControl {

    // Prestamos activos (en orden de creacion -> Cola)
    private Cola<Prestamo> prestamosActivos;

    // Historial de prestamos devueltos (el ultimo devuelto queda arriba -> Pila)
    private Pila<Prestamo> historialDevueltos;

    private int contadorId;

    private static final int DIAS_PRESTAMO_DEFECTO = 7;

    public PrestamoControl() {
        this.prestamosActivos = new Cola<>();
        this.historialDevueltos = new Pila<>();
        this.contadorId = 1;
    }
    /**
 * Equivalente a PrestamoDAO.obtenerPorUsuario(int idUsuario)
 */
public List<Prestamo> obtenerPorUsuario(int idUsuario) {
    List<Prestamo> resultado = prestamosActivos.listarTodos().stream()
            .filter(p -> p.getUsuario().getIdPersona() == idUsuario)
            .collect(Collectors.toList());
    resultado.addAll(historialDevueltos.listarTodos().stream()
            .filter(p -> p.getUsuario().getIdPersona() == idUsuario)
            .collect(Collectors.toList()));
    return resultado;
}

/**
 * Equivalente a PrestamoDAO.obtenerActivos()
 */
public List<Prestamo> obtenerActivos() {
    return prestamosActivos.listarTodos();
}

/**
 * Equivalente a PrestamoDAO.obtenerTodos()
 */
public List<Prestamo> obtenerTodos() {
    List<Prestamo> resultado = new ArrayList<>(prestamosActivos.listarTodos());
    resultado.addAll(historialDevueltos.listarTodos());
    return resultado;
}

/**
 * Equivalente a PrestamoDAO.actualizar(Prestamo) - en memoria los objetos
 * ya estan referenciados, asi que solo se sincroniza estado/fecha si el
 * prestamo pasado es una copia
 */
public boolean actualizar(Prestamo prestamo) {
    Prestamo existente = buscarPrestamoPorId(prestamo.getIdPrestamo());
    if (existente == null) {
        return false;
    }
    existente.setEstado(prestamo.getEstado());
    existente.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());

    // Si paso a devuelto, mover de cola a pila
    if (existente.getFechaDevolucionReal() != null && prestamosActivos.listarTodos().contains(existente)) {
        prestamosActivos.eliminar(existente);
        historialDevueltos.apilar(existente);
    }
    return true;
}

/**
 * Equivalente a PrestamoDAO.insertar(Prestamo) - usa el mismo flujo
 * que realizarPrestamo, ya que el objeto ya viene armado
 */
public boolean insertar(Prestamo prestamo) {
    if (!prestamo.getLibro().estaDisponible()) {
        return false;
    }
    if (!prestamo.getUsuario().puedePedirLibros()) {
        return false;
    }
    prestamo.setIdPrestamo(prestamosActivos.getTamano() + historialDevueltos.getTamano() + 1);
    prestamo.getLibro().prestar();
    prestamo.getUsuario().incrementarLibrosPrestados();
    prestamosActivos.encolar(prestamo);
    return true;
}
    public Prestamo realizarPrestamo(Usuario usuario, Libro libro) {
        return realizarPrestamo(usuario, libro, DIAS_PRESTAMO_DEFECTO);
    }

    public Prestamo realizarPrestamo(Usuario usuario, Libro libro, int diasPrestamo) {
        if (!usuario.puedePedirLibros()) {
            System.out.println("Error: El usuario no puede pedir mas libros");
            System.out.println("Razon: " + obtenerRazonRechazo(usuario));
            return null;
        }

        if (!libro.estaDisponible()) {
            System.out.println("Error: El libro no esta disponible");
            return null;
        }

        if (tienePrestamosVencidos(usuario)) {
            System.out.println("Error: El usuario tiene prestamos vencidos");
            return null;
        }

        Prestamo prestamo = new Prestamo(usuario, libro, LocalDate.now(), diasPrestamo);
        prestamo.setIdPrestamo(contadorId++);

        libro.prestar();
        usuario.incrementarLibrosPrestados();

        prestamosActivos.encolar(prestamo);

        System.out.println("Prestamo realizado exitosamente");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Libro: " + libro.getTitulo());
        System.out.println("Fecha devolucion esperada: " + prestamo.getFechaDevolucionEsperada());

        return prestamo;
    }

    /**
     * Devolver un libro. Lo saca de la cola de activos y lo
     * apila en el historial de devueltos.
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

        prestamo.devolver();

        // Sacar de la cola de activos
        prestamosActivos.eliminar(prestamo);

        // Agregar al historial (pila)
        historialDevueltos.apilar(prestamo);

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

    public Prestamo buscarPrestamoPorId(int idPrestamo) {
        for (Prestamo p : prestamosActivos.listarTodos()) {
            if (p.getIdPrestamo() == idPrestamo) {
                return p;
            }
        }
        for (Prestamo p : historialDevueltos.listarTodos()) {
            if (p.getIdPrestamo() == idPrestamo) {
                return p;
            }
        }
        return null;
    }

    public List<Prestamo> obtenerPrestamosActivosUsuario(Usuario usuario) {
        return prestamosActivos.listarTodos().stream()
                .filter(p -> p.getUsuario().getCedula().equals(usuario.getCedula()))
                .collect(Collectors.toList());
    }

    public List<Prestamo> obtenerPrestamosLibro(Libro libro) {
        List<Prestamo> resultado = prestamosActivos.listarTodos().stream()
                .filter(p -> p.getLibro().getIsbn().equals(libro.getIsbn()))
                .collect(Collectors.toList());
        resultado.addAll(historialDevueltos.listarTodos().stream()
                .filter(p -> p.getLibro().getIsbn().equals(libro.getIsbn()))
                .collect(Collectors.toList()));
        return resultado;
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamosActivos.listarTodos();
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamosActivos.listarTodos().stream()
                .filter(Prestamo::estaVencido)
                .collect(Collectors.toList());
    }

    /**
     * Historial de devueltos (orden: ultimo devuelto primero)
     */
    public List<Prestamo> obtenerPrestamosDevueltos() {
        return historialDevueltos.listarTodos();
    }

    public boolean tienePrestamosVencidos(Usuario usuario) {
        return prestamosActivos.listarTodos().stream()
                .anyMatch(p -> p.getUsuario().getCedula().equals(usuario.getCedula()) &&
                              p.estaVencido());
    }

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

        LocalDate nuevaFecha = prestamo.getFechaDevolucionEsperada().plusDays(diasAdicionales);
        prestamo.setFechaDevolucionEsperada(nuevaFecha);

        System.out.println("Prestamo renovado exitosamente");
        System.out.println("Nueva fecha de devolucion: " + nuevaFecha);

        return true;
    }

    public List<Prestamo> obtenerHistorialUsuario(Usuario usuario) {
        List<Prestamo> resultado = prestamosActivos.listarTodos().stream()
                .filter(p -> p.getUsuario().getCedula().equals(usuario.getCedula()))
                .collect(Collectors.toList());
        resultado.addAll(historialDevueltos.listarTodos().stream()
                .filter(p -> p.getUsuario().getCedula().equals(usuario.getCedula()))
                .collect(Collectors.toList()));
        return resultado;
    }

    private String obtenerRazonRechazo(Usuario usuario) {
        if (!usuario.isActivo()) {
            return "Usuario inactivo";
        }
        if (usuario.getLibrosPrestados() >= usuario.getMaxLibrosPrestamo()) {
            return "Ha alcanzado el limite de " + usuario.getMaxLibrosPrestamo() + " libros";
        }
        return "Razon desconocida";
    }

    public String obtenerEstadisticas() {
        int prestamosActivosCount = prestamosActivos.getTamano();
        int prestamosVencidos = obtenerPrestamosVencidos().size();
        int prestamosDevueltos = historialDevueltos.getTamano();
        int totalPrestamos = prestamosActivosCount + prestamosDevueltos;

        return "Total de prestamos: " + totalPrestamos + "\n" +
               "Prestamos activos: " + prestamosActivosCount + "\n" +
               "Prestamos vencidos: " + prestamosVencidos + "\n" +
               "Prestamos devueltos: " + prestamosDevueltos;
    }

    public void actualizarEstadosPrestamos() {
        for (Prestamo prestamo : prestamosActivos.listarTodos()) {
            prestamo.actualizarEstado();
        }
    }

    public List<Prestamo> obtenerPrestamosProximosAVencer(int dias) {
        return prestamosActivos.listarTodos().stream()
                .filter(p -> !p.estaVencido())
                .filter(p -> p.getDiasRestantes() <= dias && p.getDiasRestantes() > 0)
                .collect(Collectors.toList());
    }

    public boolean puedeRenovar(int idPrestamo) {
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);

        if (prestamo == null || prestamo.getFechaDevolucionReal() != null) {
            return false;
        }

        return !prestamo.estaVencido();
    }
}