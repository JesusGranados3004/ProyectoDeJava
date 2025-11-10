/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cine.Clases;

/**
 *
 * @author 57300
 */
public class Salas {
    private int id;
    private String nombre;
    private int capasidad;

    public Salas(int id, String nombre, int capasidad) {
        this.id = id;
        this.nombre = nombre;
        this.capasidad = capasidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapasidad() {
        return capasidad;
    }
    
    public String toString() {
        return nombre; 
    }
}
