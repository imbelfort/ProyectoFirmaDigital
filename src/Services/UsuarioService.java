package Services;

import DAO.UsuarioDAO;
import Models.Usuario;
import Utils.ParamValidator;

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
        String sintaxis = "usuario agregar [nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo]";
        String ejemplo = "usuario agregar [Juan;Perez;4567890;219064748;juan@alu.uagrm.edu.bo;ALUMNO;71000004;FICCT;Ingenieria Informatica]";

        String nombre = ParamValidator.requireString(params, 0, "nombre", sintaxis, ejemplo);
        String apellidos = ParamValidator.requireString(params, 1, "apellidos", sintaxis, ejemplo);
        String ci = ParamValidator.requireString(params, 2, "ci", sintaxis, ejemplo).toUpperCase();
        String registro = ParamValidator.requireNumericCode(params, 3, "registro", sintaxis, ejemplo);
        String email = ParamValidator.requireString(params, 4, "email", sintaxis, ejemplo).toLowerCase();
        String rol = ParamValidator.optionalString(params, 5, "ALUMNO").toUpperCase();
        String telefono = ParamValidator.optionalString(params, 6, "");
        
        // Regla: Para ALUMNO, parámetro 7 es su Facultad y parámetro 8 es su Carrera (los alumnos no tienen cargo).
        // Para DOCENTE/AUTORIDAD, parámetro 7 es su Facultad/Unidad y parámetro 8 es su Cargo oficial.
        String facultadOUnidad = ParamValidator.optionalString(params, 7, rol.equals("ALUMNO") ? "FICCT" : "UAGRM");
        String carreraOCargo = ParamValidator.optionalString(params, 8, rol.equals("ALUMNO") ? "Ingenieria Informatica" : rol);

        if (usuarioDAO.existsByCi(ci)) {
            throw new SQLException("Ya existe un usuario registrado con el CI: " + ci + ". El C.I. debe ser único en el sistema.");
        }

        if (usuarioDAO.existsByRegistro(registro)) {
            throw new SQLException("Ya existe un usuario registrado con el Registro / Código: " + registro + ". El código debe ser único.");
        }

        if (usuarioDAO.existsByEmail(email)) {
            throw new SQLException("Ya existe un usuario registrado con el correo: " + email + ".");
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
        usuario.setUnidadAcademica(facultadOUnidad);
        usuario.setCargo(carreraOCargo);
        usuario.setContrasena(ci);
        usuario.setEstado("ACTIVO");

        usuarioDAO.create(usuario);
    }

    public String[] obtenerUsuarioPorId(int id) throws SQLException {
        String[] data = usuarioDAO.readById(id);
        if (data == null) {
            throw new SQLException("No se encontró ningún usuario con el ID: " + id);
        }
        return data;
    }

    public List<String[]> listarUsuarios() throws SQLException {
        return usuarioDAO.listAll();
    }

    public void actualizarUsuario(List<String> params) throws SQLException {
        actualizarUsuario(params, null);
    }

    public void actualizarUsuario(List<String> params, String fotoPerfil) throws SQLException {
        String sintaxis = "usuario modificar [id;nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo;estado]";
        String ejemplo = "usuario modificar [4;Juan;Perez;4567890;219064748;juan@alu.uagrm.edu.bo;ALUMNO;71000004;FICCT;Ingenieria Informatica;ACTIVO]";

        int idUsuario = ParamValidator.requireInt(params, 0, "id_usuario", sintaxis, ejemplo);
        String nombre = ParamValidator.requireString(params, 1, "nombre", sintaxis, ejemplo);
        String apellidos = ParamValidator.requireString(params, 2, "apellidos", sintaxis, ejemplo);
        String ci = ParamValidator.requireString(params, 3, "ci", sintaxis, ejemplo).toUpperCase();
        String registro = ParamValidator.requireNumericCode(params, 4, "registro", sintaxis, ejemplo);
        String email = ParamValidator.requireString(params, 5, "email", sintaxis, ejemplo).toLowerCase();
        String rol = ParamValidator.optionalString(params, 6, "ALUMNO").toUpperCase();
        String telefono = ParamValidator.optionalString(params, 7, "");
        String facultadOUnidad = ParamValidator.optionalString(params, 8, "");
        String carreraOCargo = ParamValidator.optionalString(params, 9, "");
        String estado = ParamValidator.optionalString(params, 10, "ACTIVO").toUpperCase();

        if (!usuarioDAO.existsById(idUsuario)) {
            throw new SQLException("No existe ningún usuario registrado con el ID: " + idUsuario + ". Verifique los IDs con 'usuario listar'.");
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
        usuario.setUnidadAcademica(facultadOUnidad);
        usuario.setCargo(carreraOCargo);
        usuario.setEstado(estado);

        usuarioDAO.update(usuario);
    }

    public void eliminarUsuario(int id) throws SQLException {
        if (!usuarioDAO.existsById(id)) {
            throw new SQLException("No existe ningún usuario registrado con el ID: " + id + ". Verifique con 'usuario listar'.");
        }
        usuarioDAO.delete(id);
    }

    public String getComandosUsuario() {
        return "Comandos Gestion de Usuarios (CU1):\n" +
                "- usuario listar\n" +
                "- usuario agregar [nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo]\n" +
                "  • Alumnos: Los alumnos NO tienen cargo; se registra su facultad y la carrera que estudian.\n" +
                "    Ejemplo: usuario agregar [Juan;Perez;4567890;219064748;juan@alu.uagrm.edu.bo;ALUMNO;71000004;FICCT;Ingenieria Informatica]\n" +
                "  • Docentes: Se registra su facultad y su cargo docente.\n" +
                "    Ejemplo: usuario agregar [Alberto;Guzman;3456789;1045;aguzman@uagrm.edu.bo;DOCENTE;71000003;FICCT;Docente Titular]\n" +
                "  • Autoridades: Se registra su unidad y cargo oficial.\n" +
                "    Ejemplo: usuario agregar [Carlos;Mendoza;2345678;1002;cmendoza@uagrm.edu.bo;ENCARGADO;71000002;Despacho Vicerrectorado;Encargado de Firma]\n" +
                "- usuario modificar [id;nombre;apellidos;ci;registro;email;rol;telefono;facultad_o_unidad;carrera_o_cargo;estado]\n" +
                "- usuario eliminar [id]\n\n" +
                "Nota: Códigos puramente numéricos sin letras (ej. 219064748 para Alumno, 1045 para Docente de 4 dígitos, 1001 para Autoridad).\n" +
                "Su CI es su contraseña inicial.\n" +
                "Roles: VICERRECTOR, ENCARGADO, DOCENTE, ALUMNO, ADMINISTRATIVO.";
    }
}