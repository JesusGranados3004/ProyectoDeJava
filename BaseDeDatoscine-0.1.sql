create database Cine;
use Cine;

CREATE TABLE Cliente (
  cliente_id int PRIMARY KEY auto_increment,
  nombre VARCHAR(150) NOT NULL,
  apellido VARCHAR(150) NOT NULL,
  email VARCHAR(150) UNIQUE,
  telefono VARCHAR(30),
  fecha_registro TIMESTAMP DEFAULT now()
);

CREATE TABLE Empleado (
  empleado_id int PRIMARY KEY auto_increment,
  nombre VARCHAR(150) NOT NULL,
  apellido VARCHAR(150) NOT NULL,
  rol VARCHAR(50),
  telefono VARCHAR(30),
  email VARCHAR(150),
  fecha_registro TIMESTAMP DEFAULT now()
);

CREATE TABLE Pelicula (
  pelicula_id int PRIMARY KEY auto_increment,
  titulo VARCHAR(255) NOT NULL,
  director varchar(50),
  sinopsis TEXT,
  duracion_minutes varchar(20),
  fecha_estreno varchar(25),
  idioma VARCHAR(50)
);

CREATE TABLE Genero (
  genero_id int primary key auto_increment,
  nombre_genero VARCHAR(80) NOT NULL
);

CREATE TABLE Pelicula_Genero (
  pelicula_id INT NOT NULL,
  genero_id INT NOT NULL,
  PRIMARY KEY (pelicula_id, genero_id),
  FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id),
  FOREIGN KEY (genero_id) REFERENCES Genero(genero_id)
);

CREATE TABLE Cuenta (
  cuenta_id INT PRIMARY KEY AUTO_INCREMENT,
  usuario VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol ENUM('cliente','empleado','admin') NOT NULL,
  cliente_id INT,
  empleado_id INT,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(cliente_id),
  FOREIGN KEY (empleado_id) REFERENCES Empleado(empleado_id)
);

CREATE TABLE Sala (
  sala_id int PRIMARY KEY auto_increment,
  nombre VARCHAR(100),
  capacidad INT
);

CREATE TABLE Funcion (
  funcion_id int PRIMARY KEY auto_increment,
  pelicula_id INT NOT NULL,
  sala_id INT NOT NULL,
  inicio TIMESTAMP NOT NULL,
  fin TIMESTAMP NOT NULL,
  precio_base double NOT NULL,
  FOREIGN KEY (pelicula_id) REFERENCES Pelicula(pelicula_id),
  FOREIGN KEY (sala_id) REFERENCES Sala(sala_id)
);

CREATE TABLE Asiento (
  asiento_id int PRIMARY KEY auto_increment,
  numero INT,
  sala_id INT NOT NULL,
  funcion_id INT NULL,
  FOREIGN KEY (funcion_id) REFERENCES Funcion(funcion_id),
  FOREIGN KEY (sala_id) REFERENCES Sala(sala_id)
);

CREATE TABLE Reserva (
  reserva_id int PRIMARY KEY auto_increment,
  cliente_id INT NULL,
  empleado_id INT NULL,
  fecha_reserva TIMESTAMP DEFAULT now(),
  total double,
  FOREIGN KEY (cliente_id) REFERENCES Cliente(cliente_id),
  FOREIGN KEY (empleado_id) REFERENCES Empleado(empleado_id)
);

CREATE TABLE Ticket (
  ticket_id int PRIMARY KEY auto_increment,
  reserva_id INT NOT NULL,
  funcion_id INT NOT NULL,
  asiento_id INT NOT NULL,
  fecha_emision TIMESTAMP DEFAULT now(),
  precio_final double,
  FOREIGN KEY (reserva_id) REFERENCES Reserva(reserva_id),
  FOREIGN KEY (funcion_id) REFERENCES Funcion(funcion_id),
  FOREIGN KEY (asiento_id) REFERENCES Asiento(asiento_id),
  UNIQUE (funcion_id, asiento_id)
);

CREATE TABLE Pago (
  pago_id INT PRIMARY KEY AUTO_INCREMENT,
  reserva_id INT NOT NULL,
  monto double NOT NULL,
  fecha_pago TIMESTAMP DEFAULT now(),
  metodo_pago ENUM('tarjeta', 'efectivo') NOT NULL,
  ultimos_4_digitos VARCHAR(4), 
  FOREIGN KEY (reserva_id) REFERENCES Reserva(reserva_id)
);

delimiter //

CREATE PROCEDURE AgregarPelicula(
  ptitulo VARCHAR(255),
  pdirector VARCHAR(50),
  psinopsis TEXT,
  pduracion_minutes VARCHAR(20),
  pfecha_estreno VARCHAR(25),
  pidioma VARCHAR(50),
  pnombre_genero VARCHAR(80)
)
BEGIN
  DECLARE v_pelicula_id INT;
  DECLARE v_genero_id INT;
  
  INSERT INTO Pelicula(titulo, director, sinopsis, duracion_minutes, fecha_estreno, idioma)
  VALUES (ptitulo, pdirector, psinopsis, pduracion_minutes, pfecha_estreno, pidioma);

  SET v_pelicula_id = LAST_INSERT_ID();
  SELECT genero_id INTO v_genero_id
  FROM Genero
  WHERE nombre_genero = pnombre_genero
  LIMIT 1;
  IF v_genero_id IS NULL THEN
    INSERT INTO Genero(nombre_genero) VALUES (pnombre_genero);
    SET v_genero_id = LAST_INSERT_ID();
  END IF;
  INSERT INTO Pelicula_Genero(pelicula_id, genero_id)
  VALUES (v_pelicula_id, v_genero_id);

END //

CREATE PROCEDURE GuardarFuncion(
     p_pelicula_id INT,
     p_sala_id INT,
     p_inicio DATETIME,
     p_precio_base DOUBLE,
    OUT p_funcion_id INT
)
BEGIN
    DECLARE v_duracion INT;
    DECLARE p_fin DATETIME;
    DECLARE existe INT;

    SELECT CAST(duracion_minutes AS UNSIGNED) INTO v_duracion
    FROM pelicula
    WHERE pelicula_id = p_pelicula_id;

    SET p_fin = DATE_ADD(p_inicio, INTERVAL v_duracion MINUTE);

    SELECT COUNT(*) INTO existe
    FROM funcion
    WHERE sala_id = p_sala_id
      AND (p_inicio < fin AND p_fin > inicio);

    IF existe = 0 THEN
        INSERT INTO funcion (pelicula_id, sala_id, inicio, fin, precio_base)
        VALUES (p_pelicula_id, p_sala_id, p_inicio, p_fin, p_precio_base);
        SET p_funcion_id = LAST_INSERT_ID();
    ELSE
        SET p_funcion_id = 0;
    END IF;
END //

CREATE PROCEDURE ObtenerFunciones(
p_pelicula_id INT
)
BEGIN
    SELECT 
		f.sala_id,
        f.funcion_id,
        p.titulo,
        s.nombre AS sala,
        f.inicio,
        f.fin,
        f.precio_base
    FROM funcion f
    JOIN pelicula p ON f.pelicula_id = p.pelicula_id
    JOIN sala s ON f.sala_id = s.sala_id
    WHERE (p_pelicula_id IS NULL OR f.pelicula_id = p_pelicula_id);
END;//

CREATE PROCEDURE Obtenersala(
p_sala varchar(100)
)
BEGIN
    SELECT 
       *
    FROM sala s
    WHERE (nombre = p_sala);
END;//

CREATE PROCEDURE InsertarAsiento(
      p_numero     INT,
      p_sala_id    INT,
      p_funcion_id INT,
    OUT p_asiento_id INT
)
BEGIN
    IF EXISTS (SELECT 1
               FROM Asiento
               WHERE numero     = p_numero
                 AND sala_id    = p_sala_id
                 AND funcion_id = p_funcion_id
               LIMIT 1) THEN
        SIGNAL SQLSTATE '45000';
    ELSE
        INSERT INTO Asiento (numero, sala_id, funcion_id)
        VALUES (p_numero, p_sala_id, p_funcion_id);
        SET p_asiento_id = LAST_INSERT_ID();
    END IF;
END//

CREATE PROCEDURE insertar_reserva(
     p_id_cliente   INT,
     p_id_empleado  INT, 
     p_total        DOUBLE,
    OUT p_reserva_id   INT
)
BEGIN
    INSERT INTO Reserva (cliente_id, empleado_id, total)
    VALUES (p_id_cliente, p_id_empleado, p_total);
    
    SET p_reserva_id = LAST_INSERT_ID();
END //

CREATE PROCEDURE InsertarTicket(
      p_reserva_id   INT,
      p_funcion_id   INT,
      p_asiento_id   INT,
      p_precio       DOUBLE
)
BEGIN
    INSERT INTO Ticket (reserva_id, funcion_id, asiento_id, precio_final)
    VALUES (p_reserva_id, p_funcion_id, p_asiento_id, p_precio);
END//

CREATE PROCEDURE insertar_pago(
      p_reserva_id        INT,
      p_monto             DOUBLE,
      p_metodo            ENUM('tarjeta','efectivo'),
      p_ult4              VARCHAR(4)
)
BEGIN
    INSERT INTO Pago(reserva_id, monto, metodo_pago, ultimos_4_digitos)
    VALUES (p_reserva_id, p_monto, p_metodo, p_ult4);
END//

CREATE PROCEDURE ReporteVentasPorPelicula()
BEGIN
    SELECT 
        p.titulo                                    AS Pelicula,
        COUNT(t.ticket_id)                         AS BoletosVendidos,
        SUM(t.precio_final)                        AS Ingresos
    FROM Pelicula        p
    JOIN Funcion         f ON f.pelicula_id = p.pelicula_id
    JOIN Ticket          t ON t.funcion_id  = f.funcion_id
    GROUP BY p.pelicula_id, p.titulo
    ORDER BY Ingresos DESC;
END //

CREATE PROCEDURE ReporteOcupacionPorFuncion()
BEGIN
    SELECT 
        f.funcion_id                                      AS FuncionID,
        p.titulo                                          AS Pelicula,
        s.nombre                                          AS Sala,
        s.capacidad                                       AS CapacidadTotal,
        COUNT(a.asiento_id)                               AS AsientosOcupados,
        ROUND((COUNT(a.asiento_id) / s.capacidad) * 100, 2) AS PorcentajeOcupacion
    FROM Funcion f
    JOIN Pelicula p ON p.pelicula_id = f.pelicula_id
    JOIN Sala s ON s.sala_id = f.sala_id
    LEFT JOIN Asiento a ON a.funcion_id = f.funcion_id
    GROUP BY f.funcion_id, p.titulo, s.nombre, s.capacidad
    ORDER BY PorcentajeOcupacion DESC;
END //

CREATE PROCEDURE ReporteVentasPorRangoFechasTabla(
    IN p_fechaInicio DATE,
    IN p_fechaFin    DATE
)
BEGIN
    SELECT 
        DATE(t.fecha_emision)   AS Fecha,
        COUNT(t.ticket_id)      AS Boletos,
        SUM(t.precio_final)     AS Ingresos
    FROM Ticket t
    WHERE DATE(t.fecha_emision) BETWEEN p_fechaInicio AND p_fechaFin
    GROUP BY DATE(t.fecha_emision)
    ORDER BY Fecha;
END //

CREATE PROCEDURE ReportePeliculasMasTaquilleras(IN p_top INT)
BEGIN
    SELECT 
        p.titulo                                    AS Pelicula,
        COUNT(t.ticket_id)                          AS BoletosVendidos,
        SUM(t.precio_final)                         AS IngresosTotales
    FROM Pelicula p
    JOIN Funcion  f ON f.pelicula_id = p.pelicula_id
    JOIN Ticket   t ON t.funcion_id  = f.funcion_id
    GROUP BY p.pelicula_id, p.titulo
    ORDER BY IngresosTotales DESC
    LIMIT p_top;
END //

CREATE PROCEDURE loginSimple(
    IN  p_usuario  VARCHAR(50),
    IN  p_password VARCHAR(255)
)
BEGIN
    SELECT *
    FROM   Cuenta
    WHERE  usuario = p_usuario
      AND  password = p_password;
END//

CREATE PROCEDURE listar_generos()
BEGIN
    SELECT nombre_genero
    FROM Genero
    ORDER BY nombre_genero;
END //

CREATE PROCEDURE peliculas_completo()
BEGIN
    SELECT 
        p.pelicula_id,
        p.titulo,
        g.nombre_genero,
        p.director,
        p.duracion_minutes,
        p.idioma,
        p.fecha_estreno,
        p.sinopsis
    FROM pelicula p
    JOIN pelicula_genero pg ON p.pelicula_id = pg.pelicula_id
    JOIN genero g ON g.genero_id = pg.genero_id
    ORDER BY p.titulo;
END //

CREATE PROCEDURE listar_peliculas_combo()
BEGIN
    SELECT 
        p.pelicula_id,
        p.titulo,
        g.nombre_genero,
        p.director,
        p.duracion_minutes,
        p.idioma,
        p.fecha_estreno,
        p.sinopsis
    FROM pelicula p
    JOIN pelicula_genero pg ON p.pelicula_id = pg.pelicula_id
    JOIN genero g ON g.genero_id = pg.genero_id
    ORDER BY p.titulo;
END //

CREATE PROCEDURE listar_salas()
BEGIN
    SELECT sala_id, nombre, capacidad
    FROM sala
    ORDER BY nombre;
END //

CREATE PROCEDURE cliente_cuenta(
      p_nombre   VARCHAR(150),
      p_apellido VARCHAR(150),
      p_email    VARCHAR(150),
      p_telefono VARCHAR(30),
      p_usuario  VARCHAR(50),
      p_password VARCHAR(255)
)
BEGIN
    INSERT INTO Cliente (nombre, apellido, email, telefono)
    VALUES (p_nombre, p_apellido, p_email, p_telefono);

    INSERT INTO Cuenta (usuario, password, rol, cliente_id)
    VALUES (p_usuario, p_password, 'cliente', LAST_INSERT_ID());
END //

CREATE PROCEDURE empleado_cuenta(
     p_nombre   VARCHAR(150),
     p_apellido VARCHAR(150),
     p_rol      VARCHAR(50),
     p_email    VARCHAR(150),
     p_telefono VARCHAR(30),
     p_usuario  VARCHAR(50),
     p_password VARCHAR(255)
)
BEGIN

    INSERT INTO Empleado (nombre, apellido, rol, email, telefono)
    VALUES (p_nombre, p_apellido, p_rol, p_email, p_telefono);

   
    INSERT INTO Cuenta (usuario, password, rol, empleado_id)
    VALUES (p_usuario, p_password, p_rol, LAST_INSERT_ID());
END //

CREATE PROCEDURE perfil(
    IN p_tipo  ENUM('cliente','empleado','admin'),
    IN p_id    INT
)
BEGIN
    IF p_tipo = 'cliente' THEN
        SELECT 
            c.nombre, 
            c.apellido, 
            c.email, 
            c.telefono,
            cu.usuario,
            cu.rol
        FROM Cliente c
        JOIN Cuenta cu ON cu.cliente_id = c.cliente_id
        WHERE c.cliente_id = p_id;
    ELSE
        SELECT 
            e.nombre, 
            e.apellido, 
            e.rol AS cargo, 
            e.email, 
            e.telefono,
            cu.usuario,
            cu.rol
        FROM Empleado e
        JOIN Cuenta cu ON cu.empleado_id = e.empleado_id
        WHERE e.empleado_id = p_id;
    END IF;
END //

CREATE PROCEDURE eliminar_asiento( p_asiento_id INT)
BEGIN
    DELETE FROM asiento WHERE asiento_id = p_asiento_id;
END //

CREATE PROCEDURE eliminar_reserva( p_reserva_id INT)
BEGIN
    DELETE FROM reserva WHERE reserva_id = p_reserva_id;
END //

CREATE PROCEDURE mis_tickets( 
p_tipo ENUM('cliente','empleado','admin'),  
p_id INT)
BEGIN
    IF p_tipo = 'cliente' THEN
        SELECT t.ticket_id, a.numero, f.inicio, t.precio_final, p.titulo, s.nombre AS sala
        FROM ticket t
        JOIN asiento a ON a.asiento_id = t.asiento_id
        JOIN funcion f ON f.funcion_id = t.funcion_id
        JOIN pelicula p ON p.pelicula_id = f.pelicula_id
        JOIN sala s ON s.sala_id = f.sala_id
        JOIN reserva r ON r.reserva_id = t.reserva_id
        WHERE r.cliente_id = p_id
        ORDER BY f.inicio DESC;
    ELSE
        SELECT t.ticket_id, a.numero, f.inicio, t.precio_final, p.titulo, s.nombre AS sala
        FROM ticket t
        JOIN asiento a ON a.asiento_id = t.asiento_id
        JOIN funcion f ON f.funcion_id = t.funcion_id
        JOIN pelicula p ON p.pelicula_id = f.pelicula_id
        JOIN sala s ON s.sala_id = f.sala_id
        JOIN reserva r ON r.reserva_id = t.reserva_id
        WHERE r.empleado_id = p_id
        ORDER BY f.inicio DESC;
    END IF;
END //

CREATE FUNCTION precio_con_descuento(
    p_precio_base double,
    p_cuenta_id   INT
)
RETURNS double
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_descuento DECIMAL(5,2) DEFAULT 0.0;
    DECLARE v_total_tickets INT DEFAULT 0;

    SELECT COUNT(*) INTO v_total_tickets
    FROM ticket t
    JOIN reserva r ON r.reserva_id = t.reserva_id
    JOIN cuenta c ON (c.cliente_id = r.cliente_id OR c.empleado_id = r.empleado_id)
    WHERE c.cuenta_id = p_cuenta_id;

    IF v_total_tickets >= 20 THEN
        SET v_descuento = 15.0;   
    ELSEIF v_total_tickets >= 10 THEN
        SET v_descuento = 10.0;  
    ELSEIF v_total_tickets >= 5 THEN
        SET v_descuento = 5.0;    
    END IF;

    RETURN ROUND(p_precio_base * (1 - v_descuento / 100), 2);
END //

CREATE PROCEDURE insertar_sala(
     p_nombre    VARCHAR(100),
     p_capacidad INT
)
BEGIN
    INSERT INTO Sala (nombre, capacidad)
    VALUES (p_nombre, p_capacidad);
END //

CALL cliente_cuenta(
  'Jesus',
  'Granados',
  'jesusgranados@gmail.com',
  '3001234567',
  'jesus3',
  'jesus3003'
);//

CALL empleado_cuenta(
  'Carlos',
  'Gómez',
  'admin',
  '3109876543',
  'carlos.gomez@gmail.com',
  'jesus',
  'jesus3004'
);//

CALL empleado_cuenta(
  'brayan',
  'fontecha',
  'empleado',
  '3109876543',
  'brayan.fontecha@gmail.com',
  'brayan',
  '12345'
);//

CALL insertar_sala('VIP', 20);//
CALL insertar_sala('Normal', 40);//
CALL insertar_sala('3D', 40);//

CALL AgregarPelicula(
  'Inception',
  'Christopher Nolan',
  'Un ladrón que roba secretos corporativos mediante el uso de la tecnología de compartir sueños recibe la tarea inversa de plantar una idea en la mente de un CEO.',
  '148',
  '2010-07-16',
  'Inglés',
  'Ciencia Ficción'
);//

CALL AgregarPelicula(
  'Avatar: El Camino del Agua',
  'James Cameron',
  'Jake Sully y Neytiri deben proteger a su familia y su hogar cuando una antigua amenaza regresa a Pandora.',
  '192',
  '2022-12-16',
  'Inglés',
  'Aventura'
);//

CALL AgregarPelicula(
  'Spider-Man: Sin Camino a Casa',
  'Jon Watts',
  'Peter Parker busca la ayuda del Doctor Strange para restaurar su identidad secreta, pero algo sale mal y el multiverso se abre.',
  '148',
  '2021-12-17',
  'Inglés',
  'Acción'
);//

CALL AgregarPelicula(
  'Coco',
  'Lee Unkrich',
  'Un joven aspirante a músico viaja al Mundo de los Muertos para descubrir los secretos de su familia.',
  '105',
  '2017-10-27',
  'Español',
  'Animación'
);//

CALL AgregarPelicula(
  'El Conjuro',
  'James Wan',
  'Basada en hechos reales, sigue a los investigadores paranormales Ed y Lorraine Warren en uno de sus casos más aterradores.',
  '112',
  '2013-07-19',
  'Inglés',
  'Terror'
);//


CALL GuardarFuncion(1, 1, '2025-11-20 18:00:00', 15000, @f1);//
CALL GuardarFuncion(3, 2, '2025-11-20 20:00:00', 18000, @f2);//
CALL GuardarFuncion(4, 3, '2025-11-21 16:00:00', 20000, @f3);//

CALL InsertarAsiento(1, 1, 1, @a1);//
CALL InsertarAsiento(2, 1, 1, @a2);//
CALL InsertarAsiento(3, 1, 1, @a3);//

CALL InsertarAsiento(5, 2, 2, @a4);//
CALL InsertarAsiento(6, 2, 2, @a5);//

CALL InsertarAsiento(10, 3, 3, @a6);//
CALL InsertarAsiento(11, 3, 3, @a7);//
CALL InsertarAsiento(12, 3, 3, @a8);//

CALL insertar_reserva(1, NULL, 45000, @r1);//
CALL InsertarTicket(1, 1, 1, 15000);//
CALL InsertarTicket(1, 1, 2, 15000);//
CALL InsertarTicket(1, 1, 3, 15000);//
CALL insertar_pago(1, 45000, 'tarjeta', '1234');//

CALL insertar_reserva(1, NULL, 36000, @r2);//
CALL InsertarTicket(2, 2, 4, 18000);//
CALL InsertarTicket(2, 2, 5, 18000);//
CALL insertar_pago(2, 36000, 'efectivo', NULL);//

CALL insertar_reserva(1, NULL, 60000, @r3);//
CALL InsertarTicket(3, 3, 6, 20000);//
CALL InsertarTicket(3, 3, 7, 20000);//
CALL InsertarTicket(3, 3, 8, 20000);//
CALL insertar_pago(3, 60000, 'tarjeta', '5678');//

SELECT * FROM funcion;//