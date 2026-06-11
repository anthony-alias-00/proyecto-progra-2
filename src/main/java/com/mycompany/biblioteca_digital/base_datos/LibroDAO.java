package com.mycompany.biblioteca_digital.base_datos;

import com.mycompany.biblioteca_digital.modelo.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    
    /**
     * Insertar un nuevo libro en la base de datos
     */
  public boolean insertar(Libro libro) {
    String sql = "INSERT INTO libros (isbn, titulo, autor, editorial, año, categoria, " +
                 "ubicacion, cantidad_total, cantidad_disponible, activo) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, true)";
    
    try (Connection conn = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        System.out.println("Insertando nuevo libro: " + libro.getTitulo());
        
        pstmt.setString(1, libro.getIsbn());
        pstmt.setString(2, libro.getTitulo());
        pstmt.setString(3, libro.getAutor());
        pstmt.setString(4, libro.getEditorial());
        pstmt.setInt(5, libro.getAño());
        pstmt.setString(6, libro.getCategoria());
        pstmt.setString(7, libro.getUbicacion());
        pstmt.setInt(8, libro.getCantidadTotal());
        pstmt.setInt(9, libro.getCantidadDisponible());
        
        int filasAfectadas = pstmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            // Obtener ID generado
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                libro.setIdLibro(rs.getInt(1));
                System.out.println("Libro insertado con ID: " + libro.getIdLibro());
            }
            return true;
        } else {
            System.err.println("No se pudo insertar el libro");
            return false;
        }
        
    } catch (SQLException e) {
        System.err.println("Error SQL al insertar libro: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}public boolean existeISBN(String isbn) {
    String sql = "SELECT COUNT(*) FROM libros WHERE isbn = ? AND activo = true";
    
    try (Connection conn = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, isbn);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        
        return false;
        
    } catch (SQLException e) {
        System.err.println("Error al verificar ISBN: " + e.getMessage());
        return false;
    }

}
    /**
     * Buscar libro por ISBN
     */
    public Libro buscarPorISBN(String isbn) {
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, isbn);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return crearLibroDesdeResultSet(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ISBN: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Buscar libro por ID
     */
    public Libro buscarPorId(int id) {
        String sql = "SELECT * FROM libros WHERE id_libro = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return crearLibroDesdeResultSet(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Buscar libros por titulo 
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE titulo LIKE ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + titulo + "%");
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                libros.add(crearLibroDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por titulo: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            }
        }
        
        return libros;
    }
    
    /**
     * Obtener todos los libros
     */
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE activo = 1 ORDER BY titulo";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                libros.add(crearLibroDesdeResultSet(rs));
            }
            
            System.out.println("Se obtuvieron " + libros.size() + " libros");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            }
        }
        
        return libros;
    }
    
    /**
     * Actualizar libro
     */
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, editorial = ?, " +
                     "año = ?, categoria = ?, cantidad_total = ?, cantidad_disponible = ?, " +
                     "ubicacion = ?, activo = ? WHERE id_libro = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getEditorial());
            stmt.setInt(5, libro.getAño());
            stmt.setString(6, libro.getCategoria());
            stmt.setInt(7, libro.getCantidadTotal());
            stmt.setInt(8, libro.getCantidadDisponible());
            stmt.setString(9, libro.getUbicacion());
            stmt.setBoolean(10, libro.isActivo());
            stmt.setInt(11, libro.getIdLibro());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Libro actualizado: " + libro.getTitulo());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
    
    /**
     * Actualizar disponibilidad (cuando se presta o devuelve)
     */
    public boolean actualizarDisponibilidad(int idLibro, int nuevaCantidadDisponible) {
        String sql = "UPDATE libros SET cantidad_disponible = ? WHERE id_libro = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nuevaCantidadDisponible);
            stmt.setInt(2, idLibro);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar disponibilidad: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
    
    /**
     * Eliminar libro (marcar como inactivo)
     */
    public boolean eliminar(int idLibro) {
        String sql = "UPDATE libros SET activo = 0 WHERE id_libro = ?";
    
    try (Connection conn = ConexionBD.obtenerConexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        System.out.println("Eliminando libro ID:" +idLibro);

        pstmt.setInt(1, idLibro);
        
        int filasAfectadas = pstmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            System.out.println("Libro marcado como inactivo");
            return true;
        } else {
            System.err.println("No se encontro libro con ID: " + idLibro);
            return false;
        }
        
    } catch (SQLException e) {
        System.err.println("Error SQL al eliminar: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
    }
    
    /**
     * Crear objeto Libro 
     */
    private Libro crearLibroDesdeResultSet(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        
        libro.setIdLibro(rs.getInt("id_libro"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setAño(rs.getInt("año"));
        libro.setCategoria(rs.getString("categoria"));
        libro.setCantidadTotal(rs.getInt("cantidad_total"));
        libro.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        libro.setUbicacion(rs.getString("ubicacion"));
        libro.setActivo(rs.getBoolean("activo"));
        
        return libro;
    }
}