package com.mycompany.biblioteca_digital.base_datos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {
    
    // Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";
    
    
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexion a base de datos exitosa");
            return conexion;
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL no encontrado");
         
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
         
            System.err.println("URL: " + URL);
            throw e;
        }
    }
    
    /**
     * Cierra a la base de datos
     * @param conexion la conexión a cerrar
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }
    
    
}