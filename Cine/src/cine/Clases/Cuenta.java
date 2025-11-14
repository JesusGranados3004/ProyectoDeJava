package cine.Clases;

/**
 * Clase que representa una cuenta dentro del sistema del cine.
 * Contiene la información básica del usuario, su identificación y rol.
 */
public class Cuenta {
    // Atributos de la clase
    private int id;         // Identificador único de la cuenta
    private int ide;        // Identificador relacionado (por ejemplo, ID de empleado o cliente)
    private String usuario; // Nombre de usuario de la cuenta
    private String rol;     // Rol asignado al usuario (por ejemplo, "Administrador" o "Cliente")

    /**
     * Constructor que inicializa una cuenta con los datos proporcionados.
     * 
     * @param id Identificador único de la cuenta
     * @param ide Identificador del usuario o empleado asociado
     * @param usuario Nombre de usuario
     * @param rol Rol asignado al usuario
     */
    public Cuenta(int id, int ide, String usuario, String rol) {
        this.id = id;
        this.ide = ide;
        this.usuario = usuario;
        this.rol = rol;
    }

    /**
     * Obtiene el identificador de la cuenta.
     * @return id de la cuenta
     */
    public int getId() {
        return id;
    }
    
    /**
     * Obtiene el identificador del usuario o empleado asociado.
     * @return ide relacionado con la cuenta
     */
    public int getIde() {
        return ide;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene el rol asignado al usuario.
     * @return rol del usuario
     */
    public String getRol() {
        return rol;
    }
}