package com.authservice.model;

/**
 * Clase modelo que representa un usuario del sistema de autenticacion.
 * Contiene la informacion basica necesaria para el registro y el login.
 *
 * @author Steven Diaz Morales
 */
public class Usuario {

    // Nombre de usuario unico, usado como llave para autenticacion
    private String usuario;

    // Contrasena del usuario (en un ambiente real se debe almacenar cifrada)
    private String contrasena;

    // Correo electronico, opcional para el registro
    private String correo;

    public Usuario() {
    }

    public Usuario(String usuario, String contrasena, String correo) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
