package Services;

import DAO.UsuarioDAO;
import Models.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void crearUsuario(List<String> params) throws SQLException {
        crearUsuario(params, null);
    }

    public void crearUsuario(List<String> params, String fotoPerfil) throws SQLException {
        // Formato esperado: nombre, apellidos, ci, registro, email, rol, telefono, unidad_academica, cargo
        if (params.size() < 6) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: usuario agregar [nombre;apellidos;ci;registro;email;rol;telefono;unidad_academica;cargo]"
            );
        }

        String nombre = params.get(0) != null ? params.get(0).trim() : "";
        String apellidos = params.get(1) != null ? params.get(1).trim() : "";
        String ci = params.get(2) != null ? params.get(2).trim().toUpperCase() : "";
        String registro = params.get(3) != null ? params.get(3).trim() : "";
        String email = params.get(4) != null ? params.get(4).trim().toLowerCase() : "";
        String rol = params.get(5) != null ? params.get(5).trim().toUpperCase() : "ALUMNO";
        String telefono = params.size() > 6 && params.get(6) != null ? params.get(6).trim() : "";
        String unidad = params.size() > 7 && params.get(7) != null ? params.get(7).trim() : "UAGRM";
        String cargo = params.size() > 8 && params.get(8) != null ? params.get(8).trim() : rol;

        if (nombre.isEmpty() || apellidos.isEmpty()) {
            throw new IllegalArgumentException("El nombre y apellidos son obligatorios");
        }

        if (ci.isEmpty()) {
            throw new IllegalArgumentException("El CI es obligatorio. (Actua como contraseña inicial)");
        }

        if (registro.isEmpty()) {
            throw new IllegalArgumentException("El Registro / Codigo es obligatorio para Alumnos, Docentes y Funcionarios");
        }

        if (usuarioDAO.existsByCi(ci)) {
            throw new SQLException("Ya existe un usuario registrado con el CI: " + ci);
        }

        if (usuarioDAO.existsByRegistro(registro)) {
            throw new SQLException("Ya existe un usuario registrado con el Registro: " + registro);
        }

        if (usuarioDAO.existsByEmail(email)) {
            throw new SQLException("Ya existe un usuario registrado con el email: " + email);
        }

        // Regla: Contraseña inicial = CI
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setCi(ci);
        usuario.setRegistro(registro);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setTelefono(telefono);
        usuario.setUnidadAcademica(unidad);
        usuario.setCargo(cargo);
        usuario.setContrasena(ci);
        usuario.setEstado("ACTIVO");

        usuarioDAO.create(usuario);
    }

    public String[] obtenerUsuarioPorId(int id) throws SQLException {
        return usuarioDAO.readById(id);
    }

    public List<String[]> listarUsuarios() throws SQLException {
        return usuarioDAO.listAll();
    }

    public void actualizarUsuario(List<String> params) throws SQLException {
        actualizarUsuario(params, null);
    }

    public void actualizarUsuario(List<String> params, String fotoPerfil) throws SQLException {
        if (params.size() < 7) {
            throw new IllegalArgumentException(
                    "Parametros insuficientes. Formato: usuario modificar [id;nombre;apellidos;ci;registro;email;rol;telefono;unidad;cargo;estado]"
            );
        }

        int idUsuario = Integer.parseInt(params.get(0).trim());
        String nombre = params.get(1) != null ? params.get(1).trim() : "";
        String apellidos = params.get(2) != null ? params.get(2).trim() : "";
        String ci = params.get(3) != null ? params.get(3).trim().toUpperCase() : "";
        String registro = params.get(4) != null ? params.get(4).trim() : "";
        String email = params.get(5) != null ? params.get(5).trim().toLowerCase() : "";
        String rol = params.get(6) != null ? params.get(6).trim().toUpperCase() : "ALUMNO";
        String telefono = params.size() > 7 && params.get(7) != null ? params.get(7).trim() : "";
        String unidad = params.size() > 8 && params.get(8) != null ? params.get(8).trim() : "";
        String cargo = params.size() > 9 && params.get(9) != null ? params.get(9).trim() : "";
        String estado = params.size() > 10 && params.get(10) != null ? params.get(10).trim().toUpperCase() : "ACTIVO";

        if (!usuarioDAO.existsById(idUsuario)) {
            throw new SQLException("No existe ningun usuario con el ID: " + idUsuario);
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setCi(ci);
        usuario.setRegistro(registro);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setTelefono(telefono);
        usuario.setUnidadAcademica(unidad);
        usuario.setCargo(cargo);
        usuario.setEstado(estado);

        usuarioDAO.update(usuario);
    }

    public void eliminarUsuario(int id) throws SQLException {
        if (!usuarioDAO.existsById(id)) {
            throw new SQLException("No existe ningun usuario con el ID: " + id);
        }
        usuarioDAO.delete(id);
    }

    public String getComandosUsuario() {
        return "Comandos Gestion de Usuarios (CU1):\n" +
                "- usuario listar\n" +
                "- usuario agregar [nombre;apellidos;ci;registro;email;rol;telefono;unidad_academica;cargo]\n" +
                "- usuario modificar [id;nombre;apellidos;ci;registro;email;rol;telefono;unidad_academica;cargo;estado]\n" +
                "- usuario eliminar [id]\n\n" +
                "Nota: Para Alumnos, Docentes y Administrativos, su login es su Registro y su CI es su contraseña inicial.\n" +
                "Roles: VICERRECTOR, ENCARGADO, DOCENTE, ALUMNO, ADMINISTRATIVO.";
    }
}