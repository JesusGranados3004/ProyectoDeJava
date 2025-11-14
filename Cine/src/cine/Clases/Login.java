package cine.Clases;

/**
 * Clase que representa las credenciales de acceso de un usuario al sistema.
 * Contiene el nombre de usuario, la contraseña y el rango o nivel de acceso.
 */
public class Login {

    // Atributos de la clase
    private String user;      // Nombre de usuario o identificador de acceso
    private String password;  // Contraseña del usuario
    private int rango;        // Nivel de acceso o rol numérico (por ejemplo, 1 = administrador, 2 = cliente)

    /**
     * Constructor que inicializa un objeto Login con los datos proporcionados.
     * 
     * @param user Nombre de usuario
     * @param password Contraseña del usuario
     * @param rango Nivel o rango de acceso del usuario
     */
    public Login(String user, String password, int rango) {
        this.user = user;
        this.password = password;
        this.rango = rango;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return nombre de usuario
     */
    public String getUser() {
        return user;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el rango o nivel de acceso del usuario.
     * @return rango numérico
     */
    public int getRango() {
        return rango;
    }
}