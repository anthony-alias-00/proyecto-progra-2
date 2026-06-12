/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.estructuras.tads;

/**
 *
 * @author ALEJANDRO
 */
import com.mycompany.biblioteca_digital.modelo.Libro;
import java.util.ArrayList;
import java.util.List;

public class ArbolAVL {

    private NodoAVL raiz;
    private int cantidad;

    public ArbolAVL() {
        this.raiz = null;
        this.cantidad = 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    private int altura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private int factorBalance(NodoAVL nodo) {
        return (nodo == null) ? 0 : altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private void actualizarAltura(NodoAVL nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
    }

    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL t2 = x.derecho;

        x.derecho = y;
        y.izquierdo = t2;

        actualizarAltura(y);
        actualizarAltura(x);

        return x;
    }

    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL t2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = t2;

        actualizarAltura(x);
        actualizarAltura(y);

        return y;
    }

    private NodoAVL balancear(NodoAVL nodo) {
        actualizarAltura(nodo);
        int balance = factorBalance(nodo);

        if (balance > 1 && factorBalance(nodo.izquierdo) >= 0) {
            return rotarDerecha(nodo);
        }
        if (balance > 1 && factorBalance(nodo.izquierdo) < 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }
        if (balance < -1 && factorBalance(nodo.derecho) <= 0) {
            return rotarIzquierda(nodo);
        }
        if (balance < -1 && factorBalance(nodo.derecho) > 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public void insertar(Libro libro) {
        raiz = insertarRec(raiz, libro);
        cantidad++;
    }

    private NodoAVL insertarRec(NodoAVL nodo, Libro libro) {
        if (nodo == null) {
            return new NodoAVL(libro);
        }

        int cmp = libro.getIsbn().compareTo(nodo.libro.getIsbn());

        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, libro);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, libro);
        } else {
            nodo.libro = libro;
            cantidad--;
            return nodo;
        }

        return balancear(nodo);
    }

    public Libro buscarPorIsbn(String isbn) {
        return buscarRec(raiz, isbn);
    }

    private Libro buscarRec(NodoAVL nodo, String isbn) {
        if (nodo == null) {
            return null;
        }

        int cmp = isbn.compareTo(nodo.libro.getIsbn());

        if (cmp == 0) {
            return nodo.libro;
        } else if (cmp < 0) {
            return buscarRec(nodo.izquierdo, isbn);
        } else {
            return buscarRec(nodo.derecho, isbn);
        }
    }

    public Libro buscarPorTitulo(String titulo) {
        return buscarPorTituloRec(raiz, titulo.toLowerCase());
    }

    private Libro buscarPorTituloRec(NodoAVL nodo, String tituloLower) {
        if (nodo == null) {
            return null;
        }
        if (nodo.libro.getTitulo() != null &&
                nodo.libro.getTitulo().toLowerCase().contains(tituloLower)) {
            return nodo.libro;
        }

        Libro encontradoIzq = buscarPorTituloRec(nodo.izquierdo, tituloLower);
        if (encontradoIzq != null) {
            return encontradoIzq;
        }
        return buscarPorTituloRec(nodo.derecho, tituloLower);
    }

    public List<Libro> buscarCoincidencias(String texto) {
        List<Libro> resultado = new ArrayList<>();
        buscarCoincidenciasRec(raiz, texto.toLowerCase(), resultado);
        return resultado;
    }

    private void buscarCoincidenciasRec(NodoAVL nodo, String textoLower, List<Libro> resultado) {
        if (nodo == null) {
            return;
        }
        buscarCoincidenciasRec(nodo.izquierdo, textoLower, resultado);

        Libro l = nodo.libro;
        boolean coincide =
                (l.getTitulo() != null && l.getTitulo().toLowerCase().contains(textoLower)) ||
                (l.getAutor() != null && l.getAutor().toLowerCase().contains(textoLower)) ||
                (l.getCategoria() != null && l.getCategoria().toLowerCase().contains(textoLower)) ||
                (l.getIsbn() != null && l.getIsbn().toLowerCase().contains(textoLower));

        if (coincide) {
            resultado.add(l);
        }

        buscarCoincidenciasRec(nodo.derecho, textoLower, resultado);
    }

    public boolean eliminar(String isbn) {
        if (buscarPorIsbn(isbn) == null) {
            return false;
        }
        raiz = eliminarRec(raiz, isbn);
        cantidad--;
        return true;
    }

    private NodoAVL eliminarRec(NodoAVL nodo, String isbn) {
        if (nodo == null) {
            return null;
        }

        int cmp = isbn.compareTo(nodo.libro.getIsbn());

        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, isbn);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, isbn);
        } else {
            if (nodo.izquierdo == null) {
                return nodo.derecho;
            } else if (nodo.derecho == null) {
                return nodo.izquierdo;
            } else {
                NodoAVL sucesor = obtenerMinimo(nodo.derecho);
                nodo.libro = sucesor.libro;
                nodo.derecho = eliminarRec(nodo.derecho, sucesor.libro.getIsbn());
            }
        }

        return balancear(nodo);
    }

    private NodoAVL obtenerMinimo(NodoAVL nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    public List<Libro> listarOrdenadoPorIsbn() {
        List<Libro> resultado = new ArrayList<>();
        inOrderRec(raiz, resultado);
        return resultado;
    }

    private void inOrderRec(NodoAVL nodo, List<Libro> resultado) {
        if (nodo == null) {
            return;
        }
        inOrderRec(nodo.izquierdo, resultado);
        resultado.add(nodo.libro);
        inOrderRec(nodo.derecho, resultado);
    }

    public List<Libro> listarTodos() {
        return listarOrdenadoPorIsbn();
    }

    public int alturaArbol() {
        return altura(raiz);
    }
}
