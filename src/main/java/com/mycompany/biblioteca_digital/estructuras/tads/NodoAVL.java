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

public class NodoAVL {
    public Libro libro;
    public NodoAVL izquierdo;
    public NodoAVL derecho;
    public int altura;

    public NodoAVL(Libro libro) {
        this.libro = libro;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 1;
    }
}
