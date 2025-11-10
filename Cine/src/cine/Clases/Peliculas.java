/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cine.Clases;

/**
 *
 * @author 57300
 */
public class Peliculas {
    private int id;
    private String titulo;
    private String genero;
    private String duracion;
    private String Estreno;
    private String director;
    private String sinopsis;
    private String idioma;
    

    public Peliculas(int id, String titulo, String genero, String duracion, String Estreno, String director,String sinopsis,String idioma) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.Estreno = Estreno;
        this.director = director;
        this.sinopsis = sinopsis;
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getEstreno() {
        return Estreno;
    }

    public String getDirector() {
        return director;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getIdioma() {
        return idioma;
    }

    public int getId() {
        return id;
    }
    
    public String toString() {
        return titulo; 
    }
    
}
