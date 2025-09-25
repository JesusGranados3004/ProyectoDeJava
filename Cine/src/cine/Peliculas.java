/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cine;

/**
 *
 * @author 57300
 */
public class Peliculas {
    private int id;
    private String titulo;
    private String genero;
    private String duracion;
    private int anioEstreno;
    private String director;

    public Peliculas(int id, String titulo, String genero, String duracion, int anioEstreno, String director) {
        this.titulo = titulo;
        this.id = id;
        this.genero = genero;
        this.duracion = duracion;
        this.anioEstreno = anioEstreno;
        this.director = director;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getId() {
        return id;
    }

    public String getGenero() {
        return genero;
    }

    public String getDuracion() {
        return duracion;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public String getDirector() {
        return director;
    }
    
    
    
}
