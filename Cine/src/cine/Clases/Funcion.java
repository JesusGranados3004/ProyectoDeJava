/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cine.Clases;

/**
 *
 * @author 57300
 */
public class Funcion {
    int pelicula; 
    int sala;
    String inicio; 
    String fin;
    double precio;

    public Funcion(int pelicula, int sala, String inicio, String fin, double precio) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.inicio = inicio;
        this.fin = fin;
        this.precio = precio;
    }

    public int getPelicula() {
        return pelicula;
    }

    public int getSala() {
        return sala;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public double getPrecio() {
        return precio;
    }
    
    
}
