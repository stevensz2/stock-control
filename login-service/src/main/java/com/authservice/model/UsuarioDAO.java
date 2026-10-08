package com.authservice.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Clase de acceso a datos (DAO) para el manejo de usuarios.
 * Para efectos de esta evidencia se usa un almacenamiento en memoria
 * (ConcurrentHashMap). En un ambiente productivo esta clase se debe
 * reemplazar por acceso a base de datos mediante JDBC.
 *
 * @author Steven Diaz Morales
 */
public class UsuarioDAO {

    // Mapa en memoria: llave = nombre de usuario, valor = objeto Usuario
    private static final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    // Se crea un usuario de prueba por defecto para poder probar el login
    static {
        usuarios.put("admin", new Usuario("admin", "admin123", "admin@stockcontrol.com"));
    }

    /**
     * Registra un nuevo usuario si el nombre de usuario no existe todavia.
     *
     * @param usuario objeto Usuario a registrar
     * @return true si el registro fue exitoso, false si el usuario ya existe
     */
    public boolean registrar(Usuario usuario) {
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            return false;
        }
        if (usuarios.containsKey(usuario.getUsuario())) {
            return false; // el usuario ya existe
        }
        usuarios.put(usuario.getUsuario(), usuario);
        return true;
    }

    /**
     * Valida las credenciales de un usuario contra los datos almacenados.
     *
     * @param usuario     nombre de usuario
     * @param contrasena  contrasena a validar
     * @return true si el usuario existe y la contrasena coincide
     */
    public boolean autenticar(String usuario, String contrasena) {
        Usuario u = usuarios.get(usuario);
        if (u == null) {
            return false;
        }
        return u.getContrasena().equals(contrasena);
    }
}
