package DAO;

import Database.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TecnicaCostoDAO {

    public List<String[]> listAll() throws SQLException {
        String sql = "SELECT id, nombre, codigo, descripcion, estado FROM tecnica_costo ORDER BY id";

        List<String[]> tecnicas = new ArrayList<>();

        try (Connection con = Conexion.getInstancia().EstablecerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tecnicas.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                });
            }
        }

        return tecnicas;
    }
}