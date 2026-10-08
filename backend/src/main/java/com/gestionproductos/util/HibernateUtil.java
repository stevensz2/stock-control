package com.gestionproductos.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase de utilidad que centraliza la creación del SessionFactory de
 * Hibernate a partir de hibernate.cfg.xml. Se construye una única vez
 * (patrón Singleton) y se reutiliza en toda la aplicación.
 */
public class HibernateUtil {

    // Se inicializa de forma perezosa al cargar la clase por primera vez
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Lee hibernate.cfg.xml y construye la fábrica de sesiones
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /** @return la fábrica de sesiones única de la aplicación */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}