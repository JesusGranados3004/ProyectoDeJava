package cine.Clases;

/**
 * Clase que representa una película dentro del sistema del cine.
 * Contiene la información principal como título, género, duración, director, sinopsis y más.
 */
public class Peliculas {
    // Atributos de la clase
    private int id;            // Identificador único de la película
    private String titulo;     // Título de la película
    private String genero;     // Género cinematográfico (acción, drama, comedia, etc.)
    private String duracion;   // Duración total de la película (por ejemplo, "2h 10min")
    private String Estreno;    // Fecha de estreno de la película
    private String director;   // Nombre del director de la película
    private String sinopsis;   // Resumen o descripción de la trama
    private String idioma;     // Idioma en que se proyecta la película

    /**
     * Constructor que inicializa una película con todos sus datos.
     * 
     * @param id Identificador de la película
     * @param titulo Título de la película
     * @param genero Género de la película
     * @param duracion Duración de la película
     * @param Estreno Fecha de estreno
     * @param director Director de la película
     * @param sinopsis Breve descripción de la película
     * @param idioma Idioma de la película
     */
    public Peliculas(int id, String titulo, String genero, String duracion, String Estreno, String director, String sinopsis, String idioma) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.Estreno = Estreno;
        this.director = director;
        this.sinopsis = sinopsis;
        this.idioma = idioma;
    }

    /**
     * Obtiene el título de la película.
     * @return título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el género de la película.
     * @return género
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Obtiene la duración de la película.
     * @return duración
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * Obtiene la fecha de estreno de la película.
     * @return fecha de estreno
     */
    public String getEstreno() {
        return Estreno;
    }

    /**
     * Obtiene el nombre del director de la película.
     * @return director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Obtiene la sinopsis o descripción de la película.
     * @return sinopsis
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * Obtiene el idioma en que se proyecta la película.
     * @return idioma
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Obtiene el identificador único de la película.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna el título de la película como representación textual del objeto.
     * Esto facilita su uso en listas o elementos visuales.
     * @return título de la película
     */
    @Override
    public String toString() {
        return titulo;
    }
}
