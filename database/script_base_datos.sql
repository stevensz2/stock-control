-- La tabla "producto" ya existe en la base de datos stock_control con esta estructura:
--
-- CREATE TABLE producto (
--     id_producto INT AUTO_INCREMENT PRIMARY KEY,
--     codigo VARCHAR(50) NOT NULL UNIQUE,
--     nombre VARCHAR(150) NOT NULL,
--     descripcion TEXT,
--     precio_compra DECIMAL(10,2) NOT NULL DEFAULT 0.00,
--     precio_venta DECIMAL(10,2) NOT NULL DEFAULT 0.00,
--     stock_actual INT NOT NULL DEFAULT 0,
--     stock_minimo INT NOT NULL DEFAULT 5,
--     id_categoria INT,
--     FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
-- );

-- No es necesario ejecutar nada, la tabla ya está creada.
-- Este script se deja solo como referencia de la estructura usada.

-- Datos de ejemplo (opcional, solo si quieres probar el CRUD)
INSERT INTO producto (codigo, nombre, descripcion, precio_compra, precio_venta, stock_actual, stock_minimo)
VALUES
('PROD-001', 'Teclado mecánico', 'Teclado mecánico retroiluminado', 80000.00, 120000.00, 15, 5),
('PROD-002', 'Mouse inalámbrico', 'Mouse óptico inalámbrico', 25000.00, 45000.00, 30, 10);
