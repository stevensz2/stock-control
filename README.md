# stock-control

Sistema de control de inventario: API REST con Servlets Java (Tomcat) y front-end en React.
SENA, Análisis y desarrollo de software, evidencia GA8-220501096-AA1-EV02 (Módulos integrados).

## Estructura
| Carpeta | Contenido |
|---|---|
| `login-service/` | Servicio de autenticación (Servlets, JSON/texto, datos en memoria) |
| `productos-api/` | API REST de productos (`/api/productos`, datos en memoria) |
| `backend/` | GestionProductos: Servlets, JSP, Hibernate 5.6 y MySQL |
| `frontend/` | Interfaz en React con Vite |
| `database/` | Script SQL de la base `stock_control` |

## Requisitos
- Java 8, Apache Tomcat 9 (usa `javax.servlet`; no Tomcat 10+)
- MySQL (para `backend/`)
- Node.js LTS (para `frontend/`)

## Cómo ejecutar
1. Importa `login-service/`, `productos-api/` y `backend/` en Eclipse y agrégalos a Tomcat 9 (puerto 8080).
2. Front-end:
```
   cd frontend
   npm install
   npm run dev
```
   Abre http://localhost:5173

## URLs locales
- Login: http://localhost:8080/AA5EV01LoginService/
- API: http://localhost:8080/StockControlAPI/api/productos
- GestionProductos: http://localhost:8080/GestionProductos/ProductoServlet
- Front-end: http://localhost:5173

## Usuario de prueba
`admin` / `admin123`

## Configuración de base de datos
En `backend/` el archivo `hibernate.cfg.xml` trae el marcador `connection.password`. Reemplázalo por la contraseña de tu MySQL local. No la subas al repositorio.
