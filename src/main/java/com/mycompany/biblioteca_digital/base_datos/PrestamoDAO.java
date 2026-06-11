package com.mycompany.biblioteca_digital.base_datos;

import com.mycompany.biblioteca_digital.modelo.Prestamo;
import com.mycompany.biblioteca_digital.modelo.Libro;
import com.mycompany.biblioteca_digital.modelo.Usuario;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones con la tabla prestamos
 */
public class PrestamoDAO {
    
    /**
     * Crear un nuevo prestamo
     */
    public boolean insertar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, " +
                     "fecha_devolucion_esperada, estado) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, prestamo.getUsuario().getIdPersona());
            stmt.setInt(2, prestamo.getLibro().getIdLibro());
            stmt.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            stmt.setDate(4, Date.valueOf(prestamo.getFechaDevolucionEsperada()));
            stmt.setString(5, prestamo.getEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Prestamo registrado en BD");
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al crear préstamo: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar : " + e.getMessage());
            }
        }
    }
    /**
 * Buscar prestamo por ID
 */
public Prestamo buscarPorId(int idPrestamo) {
    String sql = "SELECT p.*, " +
                 "per.id_persona, per.cedula, per.nombre, per.apellido, per.mail, " +
                 "l.id_libro, l.isbn, l.titulo, l.autor, l.editorial " +
                 "FROM prestamos p " +
                 "INNER JOIN personas per ON p.id_usuario = per.id_persona " +  
                 "INNER JOIN libros l ON p.id_libro = l.id_libro " +
                 "WHERE p.id_prestamo = ?";
    
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        conn = ConexionBD.obtenerConexion();
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idPrestamo);
        
        rs = stmt.executeQuery();
        
        if (rs.next()) {
            return crearPrestamoDesdeResultSet(rs);
        }
        
        return null;
        
    } catch (SQLException e) {
        System.err.println("Error al buscar prestamo por ID: " + e.getMessage());
        e.printStackTrace();
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
 * Actualizar un prestamo existente
 */
public boolean actualizar(Prestamo prestamo) {
    String sql = "UPDATE prestamos SET " +
                 "fecha_devolucion_real = ?, " +
                 "estado = ? " +
                 "WHERE id_prestamo = ?";
    
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try {
        conn = ConexionBD.obtenerConexion();
        stmt = conn.prepareStatement(sql);
        
        // Convertir LocalDate a java.sql.Date
        if (prestamo.getFechaDevolucionReal() != null) {
            stmt.setDate(1, java.sql.Date.valueOf(prestamo.getFechaDevolucionReal()));
        } else {
            stmt.setNull(1, java.sql.Types.DATE);
        }
        
        stmt.setString(2, prestamo.getEstado());
        stmt.setInt(3, prestamo.getIdPrestamo());
        
        int filasAfectadas = stmt.executeUpdate();
        
        if (filasAfectadas > 0) {
            System.out.println("Préstamo actualizado: ID " + prestamo.getIdPrestamo());
            return true;
        }
        
        return false;
        
    } catch (SQLException e) {
        System.err.println("Error al actualizar préstamo: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
    
    /**
     * Obtener prestamos de un usuario específico
     */
    public List<Prestamo> obtenerPorUsuario(int idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.*, per.nombre, per.apellido, l.titulo, l.autor " +
                     "FROM prestamos p " +
                     "INNER JOIN personas per ON p.id_usuario = per.id_persona " +
                     "INNER JOIN libros l ON p.id_libro = l.id_libro " +
                     "WHERE p.id_usuario = ? " +
                     "ORDER BY p.fecha_prestamo DESC";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                prestamos.add(crearPrestamoDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos del usuario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return prestamos;
    }
    
    /**
     * Obtener préstamos activos (no devueltos)
     */
    public List<Prestamo> obtenerActivos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.*, per.nombre, per.apellido, per.cedula, " +
                     "l.titulo, l.autor, l.isbn " +
                     "FROM prestamos p " +
                     "INNER JOIN personas per ON p.id_usuario = per.id_persona " +
                     "INNER JOIN libros l ON p.id_libro = l.id_libro " +
                     "WHERE p.estado = 'ACTIVO' " +
                     "ORDER BY p.fecha_prestamo DESC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                prestamos.add(crearPrestamoDesdeResultSet(rs));
            }
            
            System.out.println("Se obtuvieron " + prestamos.size() + " préstamos activos");
            
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos activos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return prestamos;
    }
    
    /**
     * Obtener prestamos vencidos
     */
    public List<Prestamo> obtenerVencidos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.*, per.nombre, per.apellido, per.cedula, " +
                     "l.titulo, l.autor, l.isbn " +
                     "FROM prestamos p " +
                     "INNER JOIN personas per ON p.id_usuario = per.id_persona " +
                     "INNER JOIN libros l ON p.id_libro = l.id_libro " +
                     "WHERE p.estado = 'ACTIVO' AND p.fecha_devolucion_esperada < CURDATE() " +
                     "ORDER BY p.fecha_devolucion_esperada ASC";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Prestamo prestamo = crearPrestamoDesdeResultSet(rs);
                prestamo.setEstado("VENCIDO");
                prestamos.add(prestamo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener prestamos vencidos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        
        return prestamos;
    }
    
    /**
     * Obtener todos los prestamos 
     */
    public List<Prestamo> obtenerTodos() {
    List<Prestamo> prestamos = new ArrayList<>();
    String sql = "SELECT p.*, per.nombre, per.apellido, per.cedula, " +
                 "l.titulo, l.autor, l.isbn " +
                 "FROM prestamos p " +
                 "INNER JOIN personas per ON p.id_usuario = per.id_persona " +  // ✅ CORRECTO
                 "INNER JOIN libros l ON p.id_libro = l.id_libro " +
                 "ORDER BY p.fecha_prestamo DESC";
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
        conn = ConexionBD.obtenerConexion();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            prestamos.add(crearPrestamoDesdeResultSet(rs));
        }
        
        System.out.println("Se obtuvieron " + prestamos.size() + " prestamos");
        
    } catch (SQLException e) {
        System.err.println("Error al obtener todos los prestamos: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }
    
    return prestamos;
}
    
    /**
     * Devolver un libro (marcar prestamo como DEVUELTO)
     */
    public boolean devolver(int idPrestamo) {
        String sql = "UPDATE prestamos SET estado = 'DEVUELTO', " +
                     "fecha_devolucion_real = CURDATE() WHERE id_prestamo = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idPrestamo);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Prestamo devuelto: ID " + idPrestamo);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al devolver libro: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Renovar prestamo (extender fecha de devolucion)
     */
    public boolean renovar(int idPrestamo, LocalDate nuevaFechaDevolucion) {
        String sql = "UPDATE prestamos SET fecha_devolucion_esperada = ? WHERE id_prestamo = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexionBD.obtenerConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(nuevaFechaDevolucion));
            stmt.setInt(2, idPrestamo);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al renovar prestamo: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    
    /**
 * Crear objeto Prestamo desde un ResultSet
 */
private Prestamo crearPrestamoDesdeResultSet(ResultSet rs) throws SQLException {
    // Crear Usuario
    Usuario usuario = new Usuario();
    // Leer id_usuario de la tabla prestamos (columna del JOIN)
    if (columnExists(rs, "id_usuario")) {
        usuario.setIdPersona(rs.getInt("id_usuario"));
    } else if (columnExists(rs, "id_persona")) {
        usuario.setIdPersona(rs.getInt("id_persona"));
    }
    
    usuario.setNombre(rs.getString("nombre"));
    usuario.setApellido(rs.getString("apellido"));
    if (columnExists(rs, "cedula")) {
        usuario.setCedula(rs.getString("cedula"));
    }
    
    // Crear Libro
    Libro libro = new Libro();
    libro.setIdLibro(rs.getInt("id_libro"));
    libro.setTitulo(rs.getString("titulo"));
    libro.setAutor(rs.getString("autor"));
    if (columnExists(rs, "isbn")) {
        libro.setIsbn(rs.getString("isbn"));
    }
    if (columnExists(rs, "editorial")) {
        libro.setEditorial(rs.getString("editorial"));
    }
    
    // Crear Prestamo
    Prestamo prestamo = new Prestamo();
    prestamo.setUsuario(usuario);
    prestamo.setLibro(libro);
    prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
    prestamo.setFechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada").toLocalDate());
    
    Date fechaDevReal = rs.getDate("fecha_devolucion_real");
    if (fechaDevReal != null) {
        prestamo.setFechaDevolucionReal(fechaDevReal.toLocalDate());
    }
    
    prestamo.setEstado(rs.getString("estado"));
    
    return prestamo;
}
    
    /**
     * Verificar si una columna existe 
     */
    private boolean columnExists(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}