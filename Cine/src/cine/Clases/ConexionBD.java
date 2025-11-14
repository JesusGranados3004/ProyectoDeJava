package cine.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 * Clase encargada de manejar la conexión con la base de datos MySQL.
 */
public class ConexionBD {
    // Objeto para almacenar la conexión
    Connection conn = null;
    
    // Credenciales y parámetros de conexión
    String USER = "root";       // Usuario de la base de datos
    String PASSWORD = "3004";   // Contraseña del usuario
    String NAME = "Cine";       // Nombre de la base de datos
    String IP = "localhost";    // Dirección del servidor (en este caso, local)
    String PORT = "3306";       // Puerto de conexión predeterminado de MySQL
    
    // Cadena de conexión JDBC construida con los datos anteriores
    String cadena = "jdbc:mysql://" + IP + ":" + PORT + "/" + NAME;
    
    /**
     * Método que establece la conexión con la base de datos.
     * @return un objeto Connection si la conexión es exitosa, de lo contrario retorna null.
     */
    public Connection Conexion() {
        try {
            // Cargar el controlador de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión con la base de datos
            conn = DriverManager.getConnection(cadena, USER, PASSWORD);
        } catch (Exception e) {
            // Mensaje en caso de error al conectar
            JOptionPane.showMessageDialog(null, "La conexión no fue exitosa");
        }
        return conn;
    }
    
    /**
     * Método que cierra la conexión con la base de datos si está abierta.
     */
    public void CloseConnection() {
        try {
            // Verifica si la conexión está activa antes de cerrarla
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            // Mensaje en caso de error al cerrar la conexión
            JOptionPane.showMessageDialog(null, "La conexión no fue cerrada");
        }
    }
}
