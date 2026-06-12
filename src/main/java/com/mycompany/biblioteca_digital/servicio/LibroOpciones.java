package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Libro;
import com.mycompany.biblioteca_digital.estructuras.tads.ArbolAVL;
import com.mycompany.biblioteca_digital.busqueda.Busquedas;
import com.mycompany.biblioteca_digital.ordenamiento.Ordenamientos;
import java.util.List;
import java.util.stream.Collectors;

public class LibroOpciones {

    private ArbolAVL catalogo;
    private int contadorId;

    public LibroOpciones() {
        this.catalogo = new ArbolAVL();
        this.contadorId = 1;
    }
    /**
 * Equivalente a LibroDAO.existeISBN()
 */
public boolean existeISBN(String isbn) {
    return buscarPorISBN(isbn) != null;
}

/**
 * Equivalente a LibroDAO.insertar() - usa el mismo flujo que agregarLibro
 */
public boolean insertar(Libro libro) {
    return agregarLibro(libro);
}

/**
 * Equivalente a LibroDAO.buscarPorId()
 */
public Libro buscarPorId(int idLibro) {
    return catalogo.listarTodos().stream()
            .filter(l -> l.getIdLibro() == idLibro)
            .findFirst()
            .orElse(null);
}

/**
 * Equivalente a LibroDAO.actualizarDisponibilidad()
 */
public boolean actualizarDisponibilidad(int idLibro, int nuevaCantidadDisponible) {
    Libro libro = buscarPorId(idLibro);
    if (libro == null) {
        return false;
    }
    libro.setCantidadDisponible(nuevaCantidadDisponible);
    return true;
}

/**
 * Equivalente a LibroDAO.eliminar(int) - elimina por id
 */
public boolean eliminar(int idLibro) {
    Libro libro = buscarPorId(idLibro);
    if (libro == null) {
        return false;
    }
    return eliminarLibro(libro.getIsbn());
}

/**
 * Equivalente a LibroDAO.actualizar() - actualiza usando el objeto completo
 */
public boolean actualizar(Libro libro) {
    return actualizarLibro(libro);
}
    public boolean agregarLibro(Libro libro) {
        if (buscarPorISBN(libro.getIsbn()) != null) {
            System.out.println("Error: Ya existe un libro con ese ISBN");
            return false;
        }

        if (!validarDatosLibro(libro)) {
            return false;
        }

        libro.setIdLibro(contadorId++);
        catalogo.insertar(libro);
        System.out.println("Libro agregado: " + libro.getTitulo());
        return true;
    }

    /**
     * Busqueda binaria por ISBN usando el AVL (O(log n))
     */
    public Libro buscarPorISBN(String isbn) {
        Libro libro = catalogo.buscarPorIsbn(isbn);
        return (libro != null && libro.isActivo()) ? libro : null;
    }

    /**
     * Busqueda lineal recursiva por titulo
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        return catalogo.listarTodos().stream()
                .filter(libro -> libro.isActivo() &&
                        libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Libro> buscarPorAutor(String autor) {
        return catalogo.listarTodos().stream()
                .filter(libro -> libro.isActivo() &&
                        libro.getAutor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Libro> buscarPorCategoria(String categoria) {
        return catalogo.listarTodos().stream()
                .filter(libro -> libro.isActivo() &&
                        libro.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public List<Libro> obtenerTodosLosLibros() {
        return catalogo.listarTodos().stream()
                .filter(Libro::isActivo)
                .collect(Collectors.toList());
    }

    public List<Libro> obtenerLibrosDisponibles() {
        return catalogo.listarTodos().stream()
                .filter(Libro::estaDisponible)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve el catalogo ordenado por el criterio indicado
     * usando MergeSort (recursivo).
     */
    public List<Libro> obtenerLibrosOrdenados(String criterio) {
        return Ordenamientos.mergeSort(catalogo.listarTodos(), criterio);
    }

    public boolean actualizarLibro(Libro libro) {
        Libro existente = buscarPorISBN(libro.getIsbn());

        if (existente == null) {
            System.out.println("Error: Libro no encontrado");
            return false;
        }

        existente.setTitulo(libro.getTitulo());
        existente.setAutor(libro.getAutor());
        existente.setEditorial(libro.getEditorial());
        existente.setAño(libro.getAño());
        existente.setCategoria(libro.getCategoria());
        existente.setCantidadTotal(libro.getCantidadTotal());
        existente.setUbicacion(libro.getUbicacion());

        if (existente.getCantidadDisponible() > existente.getCantidadTotal()) {
            existente.setCantidadDisponible(existente.getCantidadTotal());
        }

        System.out.println("Libro actualizado: " + existente.getTitulo());
        return true;
    }

    /**
     * Eliminar (desactivar) libro. No se elimina del arbol para
     * mantener historial; solo se marca como inactivo.
     */
    public boolean eliminarLibro(String isbn) {
        Libro libro = buscarPorISBN(isbn);

        if (libro == null) {
            System.out.println("Error: Libro no encontrado");
            return false;
        }

        int prestados = libro.getCantidadTotal() - libro.getCantidadDisponible();
        if (prestados > 0) {
            System.out.println("Error: Hay " + prestados + " ejemplares prestados");
            return false;
        }

        libro.setActivo(false);
        System.out.println("Libro eliminado: " + libro.getTitulo());
        return true;
    }

    /**
     * Eliminar definitivamente del arbol (uso administrativo)
     */
    public boolean eliminarLibroDefinitivo(String isbn) {
        return catalogo.eliminar(isbn);
    }

    private boolean validarDatosLibro(Libro libro) {
        if (libro.getIsbn() == null || libro.getIsbn().length() < 10) {
            System.out.println("Error: ISBN inválido");
            return false;
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            System.out.println("Error: El título es obligatorio");
            return false;
        }

        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            System.out.println("Error: El autor es obligatorio");
            return false;
        }

        if (libro.getCantidadTotal() < 1) {
            System.out.println("Error: La cantidad total debe ser al menos 1");
            return false;
        }

        return true;
    }

    public String obtenerEstadisticas() {
        List<Libro> todos = catalogo.listarTodos();
        int totalLibros = todos.size();
        int librosActivos = (int) todos.stream().filter(Libro::isActivo).count();
        int librosDisponibles = (int) todos.stream().filter(Libro::estaDisponible).count();

        return "Total de libros: " + totalLibros + "\n" +
               "Libros activos: " + librosActivos + "\n" +
               "Libros disponibles: " + librosDisponibles + "\n" +
               "Altura del arbol AVL: " + catalogo.alturaArbol();
    }
}