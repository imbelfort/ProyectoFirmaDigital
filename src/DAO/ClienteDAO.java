package DAO;

import Database.Conexion;
import Models.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO CLIENTE (id_usuario, tipo_cliente, limite_credito, credito_disponible, credito_utilizado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cliente.getIdUsuario());
                ps.setString(2, cliente.getTipoCliente());
                ps.setBigDecimal(3, cliente.getLimiteCredito());
                ps.setBigDecimal(4, cliente.getCreditoDisponible());
                ps.setBigDecimal(5, cliente.getCreditoUtilizado());
                ps.executeUpdate();
            }
        }
    }

    public Cliente readById(int id) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE id_cliente = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        }
        return null;
    }

    public Cliente readByUsuarioId(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM CLIENTE WHERE id_usuario = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        }
        return null;
    }

    public List<Cliente> listAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    clientes.add(mapRow(rs));
                }
            }
        }
        return clientes;
    }

    public List<Cliente> listByTipo(String tipoCliente) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE WHERE tipo_cliente = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tipoCliente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        clientes.add(mapRow(rs));
                    }
                }
            }
        }
        return clientes;
    }

    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE CLIENTE SET id_usuario = ?, tipo_cliente = ?, limite_credito = ?, credito_disponible = ?, credito_utilizado = ? WHERE id_cliente = ?";
        try (Connection con = Conexion.getInstancia().EstablecerConexion()) {
            asegurarEsquema(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cliente.getIdUsuario());
                ps.setString(2, cliente.getTipoCliente());
                ps.setBigDecimal(3, cliente.getLimiteCredito());
                ps.setBigDecimal(4, cliente.getCreditoDisponible());
                ps.setBigDecimal(5, cliente.getCreditoUtilizado());
                ps.setInt(6, cliente.getIdCliente());
                ps.executeUpdate();
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM CLIENTE WHERE id_cliente = ?";
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
            stmt.execute("ALTER TABLE cliente ADD COLUMN IF NOT EXISTS id_usuario INTEGER");
            stmt.execute("UPDATE cliente SET id_usuario = id_cliente WHERE id_usuario IS NULL");
            stmt.execute("ALTER TABLE cliente DROP CONSTRAINT IF EXISTS fk_cliente_usuario");
            stmt.execute("ALTER TABLE cliente ADD CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)");
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS uq_cliente_id_usuario ON cliente(id_usuario)");
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS cliente_id_cliente_seq OWNED BY cliente.id_cliente");
            stmt.execute("ALTER TABLE cliente ALTER COLUMN id_cliente SET DEFAULT nextval('cliente_id_cliente_seq')");
            stmt.execute("SELECT setval('cliente_id_cliente_seq', COALESCE((SELECT MAX(id_cliente) FROM cliente), 0) + 1, false)");
        }
    }

    private Cliente mapRow(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id_cliente"),
            rs.getInt("id_usuario"),
            rs.getString("tipo_cliente"),
            rs.getBigDecimal("limite_credito"),
            rs.getBigDecimal("credito_disponible"),
            rs.getBigDecimal("credito_utilizado")
        );
    }

    public boolean existsById(int idCliente) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE id_cliente = ?";

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
