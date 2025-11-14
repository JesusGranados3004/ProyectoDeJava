package cine.Clases;

/**
 * Clase que representa una sala de proyección dentro del cine.
 * Contiene información básica como su identificador, nombre y capacidad.
 */
public class Salas {
    // Atributos de la clase
    private int id;          // Identificador único de la sala
    private String nombre;   // Nombre o número de la sala
    private int capasidad;   // Capacidad máxima de personas que puede albergar

    /**
     * Constructor que inicializa una sala con los datos proporcionados.
     * 
     * @param id Identificador único de la sala
     * @param nombre Nombre o número de la sala
     * @param capasidad Capacidad máxima de la sala
     */
    public Salas(int id, String nombre, int capasidad) {
        this.id = id;
        this.nombre = nombre;
        this.capasidad = capasidad;
    }

    /**
     * Obtiene el identificador único de la sala.
     * @return id de la sala
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre o número asignado a la sala.
     * @return nombre de la sala
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la capacidad máxima de la sala.
     * @return capacidad en número de personas
     */
    public int getCapasidad() {
        return capasidad;
    }

    /**
     * Retorna el nombre de la sala como representación textual del objeto.
     * Esto facilita su uso en listas o interfaces gráficas.
     * @return nombre de la sala
     */
    @Override
    public String toString() {
        return nombre;
    }
}
