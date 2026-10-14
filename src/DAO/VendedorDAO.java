package DAO;

import Database.Conexion;
import Models.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    public void create(Vendedor vendedor) throws SQLException {
        String sql = "INSERT INTO VENDEDOR (id_usuario, comision_porcentaje, total_ventas) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, vendedor.getIdUsuario());
                ps.setBigDecimal(2, vendedor.getComisionPorcentaje());
                ps.setBigDecimal(3, vendedor.getTotalVentas());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        vendedor.setIdVendedor(rs.getInt(1));
                    }
                }
            }
        }
    }

    public String[] readById(int id) throws SQLException {
        String sql = "SELECT * FROM VENDEDOR WHERE id_vendedor = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToVendedor(rs);
                    }
                }
            }
        }
        return null;
    }

    public String[] readByUsuarioId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM VENDEDOR WHERE id_usuario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToVendedor(rs);
                    }
                }
            }
        }
        return null;
    }

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT * FROM VENDEDOR";
        List<String[]> vendedores = new ArrayList<>();
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    vendedores.add(mapResultSetToVendedor(rs));
                }
            }
        }
        return vendedores;
    }

    public void update(Vendedor vendedor) throws SQLException {
        String sql = "UPDATE VENDEDOR SET id_usuario = ?, comision_porcentaje = ?, total_ventas = ? WHERE id_vendedor = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, vendedor.getIdUsuario());
                ps.setBigDecimal(2, vendedor.getComisionPorcentaje());
                ps.setBigDecimal(3, vendedor.getTotalVentas());
                ps.setInt(4, vendedor.getIdVendedor());
                ps.executeUpdate();
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM VENDEDOR WHERE id_vendedor = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    private void asegurarEsquema(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("ALTER TABLE vendedor ADD COLUMN IF NOT EXISTS id_usuario INTEGER");
            stmt.execute("UPDATE vendedor SET id_usuario = id_vendedor WHERE id_usuario IS NULL");
            stmt.execute("ALTER TABLE vendedor DROP CONSTRAINT IF EXISTS fk_vendedor_usuario");
            stmt.execute("ALTER TABLE vendedor ADD CONSTRAINT fk_vendedor_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)");
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_vendedor_id_usuario ON vendedor(id_usuario)");
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS vendedor_id_vendedor_seq OWNED BY vendedor.id_vendedor");
            stmt.execute("ALTER TABLE vendedor ALTER COLUMN id_vendedor SET DEFAULT nextval('vendedor_id_vendedor_seq')");
            stmt.execute("SELECT setval('vendedor_id_vendedor_seq', COALESCE((SELECT MAX(id_vendedor) FROM vendedor), 0) + 1, false)");
        }
    }

    private String[] mapResultSetToVendedor(ResultSet rs) throws SQLException {
        return new String[] {
            String.valueOf(rs.getInt("id_vendedor")),
            String.valueOf(rs.getInt("id_usuario")),
            rs.getBigDecimal("comision_porcentaje").toString(),
            rs.getBigDecimal("total_ventas").toString()
        };
    }

    public boolean existsById(int idVendedor) throws SQLException {
        String sql = "SELECT 1 FROM vendedor WHERE id_vendedor = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVendedor);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
