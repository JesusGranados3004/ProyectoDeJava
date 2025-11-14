package cine.Clases;

/**
 * Clase que representa una función o proyección de una película en el cine.
 * Contiene información sobre la película, sala, horarios y precio.
 */
public class Funcion {
    // Atributos de la clase
    int pelicula;    // Identificador de la película que se proyectará
    int sala;        // Número o identificador de la sala donde se realizará la función
    String inicio;   // Hora de inicio de la función
    String fin;      // Hora de finalización de la función
    double precio;   // Precio de la entrada para esta función

    /**
     * Constructor que inicializa una función con los datos especificados.
     * 
     * @param pelicula Identificador de la película
     * @param sala Número o identificador de la sala
     * @param inicio Hora de inicio de la función
     * @param fin Hora de finalización de la función
     * @param precio Precio del boleto para esta función
     */
    public Funcion(int pelicula, int sala, String inicio, String fin, double precio) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.inicio = inicio;
        this.fin = fin;
        this.precio = precio;
    }

    /**
     * Obtiene el identificador de la película asociada a la función.
     * @return id de la película
     */
    public int getPelicula() {
        return pelicula;
    }

    /**
     * Obtiene el número o identificador de la sala donde se proyecta la película.
     * @return número de sala
     */
    public int getSala() {
        return sala;
    }

    /**
     * Obtiene la hora de inicio de la función.
     * @return hora de inicio en formato de texto
     */
    public String getInicio() {
        return inicio;
    }

    /**
     * Obtiene la hora de finalización de la función.
     * @return hora de fin en formato de texto
     */
    public String getFin() {
        return fin;
    }

    /**
     * Obtiene el precio del boleto para esta función.
     * @return precio de la entrada
     */
    public double getPrecio() {
        return precio;
    }
}
