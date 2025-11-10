package cine.Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class ConexionBD {
    Connection conn = null;
    
    String USER = "root";
    String PASSWORD = "3004";
    String NAME = "Cine";
    String IP = "localhost";
    String PORT = "3306";
    
    String cadena = "jdbc:mysql://"+ IP +":"+PORT+"/"+NAME;
    
    public Connection Conexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(cadena,USER,PASSWORD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "la conexion no fue exitosa");
        }
        return conn;
    }
    
    public void CloseConnection(){
        try {
            if(conn != null && !conn.isClosed()){
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "la conexion no fue cerrada");
        }
    }
}
