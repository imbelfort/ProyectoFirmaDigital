package Models;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String registro;          // Registro universitario o código de funcionario
    private String ci;                // Cédula de Identidad (contraseña por defecto)
    private String contrasena;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String rol;               // VICERRECTOR, ENCARGADO, DOCENTE, ALUMNO, ADMINISTRATIVO
    private String unidadAcademica;   // Carrera o Facultad
    private String cargo;             // Cargo institucional
    private String estado;            // ACTIVO, INACTIVO
    private Timestamp fechaCreacion;
    private Timestamp fechaModificacion;

    public Usuario() {
        this.estado = "ACTIVO";
    }

    public Usuario(int idUsuario, String registro, String ci, String contrasena, String nombre,
                   String apellidos, String email, String telefono, String rol,
                   String unidadAcademica, String cargo, String estado) {
        this.idUsuario = idUsuario;
        this.registro = registro;
        this.ci = ci;
        this.contrasena = (contrasena != null && !contrasena.trim().isEmpty()) ? contrasena : ci;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.unidadAcademica = unidadAcademica;
        this.cargo = cargo;
        this.estado = (estado != null && !estado.trim().isEmpty()) ? estado : "ACTIVO";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
        if (this.contrasena == null || this.contrasena.trim().isEmpty()) {
            this.contrasena = ci;
        }
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
