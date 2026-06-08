-- ============================================
-- SCRIPT COMPLETO PARA POBLAR BASE DE DATOS
-- 100 Conductores, 200 Clientes, ~1000 Servicios
-- ============================================

-- PASO 1: LIMPIAR TODAS LAS TABLAS
DELETE FROM Pago;
DELETE FROM Servicio_Mercancias;
DELETE FROM Servicio_Domicilio;
DELETE FROM Servicio_Pasajeros;
DELETE FROM Servicio_Punto;
DELETE FROM Revision;
DELETE FROM Producto;
DELETE FROM Historial;
DELETE FROM Actividad;
DELETE FROM Mapa;
DELETE FROM Restaurante;
DELETE FROM Servicio;
DELETE FROM Tarjeta_credito;
DELETE FROM Punto;
DELETE FROM Tarifa;
DELETE FROM Vehiculo;
DELETE FROM Ciudad;
DELETE FROM Cliente;
DELETE FROM Conductor;
DELETE FROM Usuario;
COMMIT;

-- ============================================
-- PASO 2: CREAR 300 USUARIOS (100 conductores + 200 clientes)
-- ============================================
BEGIN
  FOR i IN 1..100 LOOP
    INSERT INTO Usuario (nombre, correo, celular, cedula) 
    VALUES (
      'Conductor' || i,
      'conductor' || i || '@email.com',
      3000000000 + i,
      10000000 + i
    );
  END LOOP;
  
  FOR i IN 1..200 LOOP
    INSERT INTO Usuario (nombre, correo, celular, cedula) 
    VALUES (
      'Cliente' || i,
      'cliente' || i || '@email.com',
      3100000000 + i,
      20000000 + i
    );
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('300 usuarios creados');
END;
/

-- ============================================
-- PASO 3: ASIGNAR ROLES
-- ============================================
BEGIN
  FOR usuario_rec IN (
    SELECT idUsuario 
    FROM Usuario 
    WHERE nombre LIKE 'Conductor%' 
    ORDER BY idUsuario
  ) LOOP
    INSERT INTO Conductor (idUsuario) VALUES (usuario_rec.idUsuario);
  END LOOP;
  
  FOR usuario_rec IN (
    SELECT idUsuario 
    FROM Usuario 
    WHERE nombre LIKE 'Cliente%' 
    ORDER BY idUsuario
  ) LOOP
    INSERT INTO Cliente (idUsuario) VALUES (usuario_rec.idUsuario);
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('100 conductores y 200 clientes asignados');
END;
/

-- ============================================
-- PASO 4: CIUDADES
-- ============================================
INSERT INTO Ciudad (nombre) VALUES ('Bogotá');
INSERT INTO Ciudad (nombre) VALUES ('Medellín');
INSERT INTO Ciudad (nombre) VALUES ('Cali');
INSERT INTO Ciudad (nombre) VALUES ('Barranquilla');
INSERT INTO Ciudad (nombre) VALUES ('Bucaramanga');
INSERT INTO Ciudad (nombre) VALUES ('Cartagena');
INSERT INTO Ciudad (nombre) VALUES ('Pereira');
INSERT INTO Ciudad (nombre) VALUES ('Santa Marta');
INSERT INTO Ciudad (nombre) VALUES ('Manizales');
INSERT INTO Ciudad (nombre) VALUES ('Villavicencio');
COMMIT;

-- ============================================
-- PASO 5: VEHÍCULOS (100 vehículos, 1 por conductor)
-- ============================================
DECLARE
  v_tipo VARCHAR2(20);
  v_nivel VARCHAR2(20);
  v_marca VARCHAR2(50);
  v_modelo VARCHAR2(50);
  v_color VARCHAR2(50);
  v_contador NUMBER := 0;
BEGIN
  FOR conductor_rec IN (SELECT idUsuario FROM Conductor ORDER BY idUsuario) LOOP
    v_contador := v_contador + 1;
    
    CASE MOD(v_contador, 3)
      WHEN 0 THEN v_tipo := 'CARRO';
      WHEN 1 THEN v_tipo := 'CAMIONETA';
      WHEN 2 THEN v_tipo := 'MOTO';
    END CASE;
    
    CASE MOD(v_contador, 3)
      WHEN 0 THEN v_nivel := 'ESTANDAR';
      WHEN 1 THEN v_nivel := 'CONFORT';
      WHEN 2 THEN v_nivel := 'LARGE';
    END CASE;
    
    CASE MOD(v_contador, 5)
      WHEN 0 THEN v_marca := 'Toyota'; v_modelo := 'Corolla';
      WHEN 1 THEN v_marca := 'Chevrolet'; v_modelo := 'Spark';
      WHEN 2 THEN v_marca := 'Renault'; v_modelo := 'Logan';
      WHEN 3 THEN v_marca := 'Mazda'; v_modelo := 'CX-5';
      WHEN 4 THEN v_marca := 'Nissan'; v_modelo := 'Versa';
    END CASE;
    
    CASE MOD(v_contador, 4)
      WHEN 0 THEN v_color := 'Blanco';
      WHEN 1 THEN v_color := 'Negro';
      WHEN 2 THEN v_color := 'Gris';
      WHEN 3 THEN v_color := 'Rojo';
    END CASE;
    
    INSERT INTO Vehiculo (tipo, marca, modelo, color, placa, ciudadPlaca, capacidad, nivel, idConductor)
    VALUES (
      v_tipo, v_marca, v_modelo, v_color,
      'VEH' || LPAD(v_contador, 3, '0'),
      'Bogotá',
      CASE v_tipo WHEN 'MOTO' THEN 2 WHEN 'CARRO' THEN 4 WHEN 'CAMIONETA' THEN 7 END,
      v_nivel,
      conductor_rec.idUsuario
    );
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('100 vehículos creados');
END;
/

-- ============================================
-- PASO 6: TARIFAS
-- ============================================
INSERT INTO Tarifa (tipoServicio, nivel, valorKm) VALUES ('PASAJEROS', 'ESTANDAR', 2500);
INSERT INTO Tarifa (tipoServicio, nivel, valorKm) VALUES ('PASAJEROS', 'CONFORT', 3500);
INSERT INTO Tarifa (tipoServicio, nivel, valorKm) VALUES ('PASAJEROS', 'LARGE', 4500);
INSERT INTO Tarifa (tipoServicio, nivel, valorKm) VALUES ('DOMICILIO', 'NA', 3000);
INSERT INTO Tarifa (tipoServicio, nivel, valorKm) VALUES ('MERCANCIAS', 'NA', 2000);
COMMIT;

-- ============================================
-- PASO 7: PUNTOS (50 puntos)
-- ============================================
DECLARE
  v_id_ciudad NUMBER;
  v_ciudad_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_ciudad_count FROM Ciudad;
  
  FOR i IN 1..50 LOOP
    SELECT idCiudad INTO v_id_ciudad
    FROM (SELECT idCiudad, ROWNUM as rn FROM Ciudad)
    WHERE rn = MOD(i - 1, v_ciudad_count) + 1;
    
    INSERT INTO Punto (nombre, direccion, latitud, longitud, idCiudad)
    VALUES (
      'Punto' || i,
      'Dirección ' || i || ' Calle ' || i,
      4.0 + (i * 0.01),
      -74.0 - (i * 0.01),
      v_id_ciudad
    );
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('50 puntos creados');
END;
/

-- ============================================
-- PASO 8: RESTAURANTES (20 restaurantes)
-- ============================================
DECLARE
  v_id_punto NUMBER;
  v_punto_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_punto_count FROM Punto;
  
  FOR i IN 1..20 LOOP
    SELECT idPunto INTO v_id_punto
    FROM (SELECT idPunto, ROWNUM as rn FROM Punto)
    WHERE rn = MOD(i - 1, v_punto_count) + 1;
    
    INSERT INTO Restaurante (nombre, idPunto)
    VALUES ('Restaurante' || i, v_id_punto);
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('20 restaurantes creados');
END;
/

-- ============================================
-- PASO 9: SERVICIOS (1000 servicios = 5 por cliente)
-- ============================================
DECLARE
  v_tipo_servicio VARCHAR2(20);
  v_nivel VARCHAR2(20);
  v_duracion NUMBER;
  v_tarifa_km NUMBER;
  v_comision NUMBER;
  v_contador NUMBER := 0;
  v_id_conductor NUMBER;
  v_id_vehiculo NUMBER;
  v_id_tarifa NUMBER;
  v_fecha_base DATE := TO_DATE('2024-01-01', 'YYYY-MM-DD');
  v_total_conductores NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_total_conductores
  FROM Conductor c JOIN Vehiculo v ON c.idUsuario = v.idConductor;
  
  FOR cliente_rec IN (SELECT idUsuario FROM Cliente ORDER BY idUsuario) LOOP
    FOR servicio_num IN 1..5 LOOP
      v_contador := v_contador + 1;
      
      -- Tipo de servicio rotativo
      CASE MOD(v_contador, 3)
        WHEN 0 THEN 
          v_tipo_servicio := 'PASAJEROS';
          CASE MOD(v_contador, 3)
            WHEN 0 THEN v_nivel := 'ESTANDAR';
            WHEN 1 THEN v_nivel := 'CONFORT';
            ELSE v_nivel := 'LARGE';
          END CASE;
        WHEN 1 THEN v_tipo_servicio := 'DOMICILIO'; v_nivel := 'NA';
        WHEN 2 THEN v_tipo_servicio := 'MERCANCIAS'; v_nivel := 'NA';
      END CASE;
      
      v_duracion := 15 + MOD(v_contador * 7, 75);
      
      -- Asignar tarifa
      IF v_tipo_servicio = 'PASAJEROS' THEN
        CASE v_nivel
          WHEN 'ESTANDAR' THEN v_tarifa_km := 2500;
          WHEN 'CONFORT' THEN v_tarifa_km := 3500;
          WHEN 'LARGE' THEN v_tarifa_km := 4500;
        END CASE;
      ELSIF v_tipo_servicio = 'DOMICILIO' THEN
        v_tarifa_km := 3000;
      ELSE
        v_tarifa_km := 2000;
      END IF;
      
      v_comision := 0.10 + (MOD(v_contador, 11) * 0.01);
      
      -- Conductor cíclico
      SELECT idUsuario, idVehiculo INTO v_id_conductor, v_id_vehiculo
      FROM (
        SELECT c.idUsuario, v.idVehiculo, ROWNUM as rn
        FROM Conductor c JOIN Vehiculo v ON c.idUsuario = v.idConductor
      )
      WHERE rn = MOD(v_contador - 1, v_total_conductores) + 1;
      
      -- ID tarifa
      SELECT idTarifa INTO v_id_tarifa
      FROM Tarifa
      WHERE tipoServicio = v_tipo_servicio AND nivel = v_nivel;
      
      INSERT INTO Servicio (
        tipo_servicio, idCliente, idConductor, idVehiculo, idTarifa,
        duracion_minutos, hora_inicio, hora_fin, nivel_aplicado,
        tarifa_km_aplicada, porcentaje_comision
      ) VALUES (
        v_tipo_servicio, cliente_rec.idUsuario, v_id_conductor, v_id_vehiculo, v_id_tarifa,
        v_duracion,
        v_fecha_base + MOD(v_contador, 365) + (MOD(v_contador, 24) / 24),
        v_fecha_base + MOD(v_contador, 365) + ((MOD(v_contador, 24) + (v_duracion/60)) / 24),
        v_nivel, v_tarifa_km, v_comision
      );
      
      IF MOD(v_contador, 200) = 0 THEN
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Servicios: ' || v_contador);
      END IF;
    END LOOP;
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Total: ' || v_contador || ' servicios');
END;
/

-- ============================================
-- PASO 10: TARJETAS DE CRÉDITO (200)
-- ============================================
BEGIN
  FOR i IN 1..200 LOOP
    INSERT INTO Tarjeta_credito (numero, nombre_tarjeta, fecha_vencimiento, codigo)
    VALUES (
      1000000000000000 + i,
      'TARJETA' || i,
      ADD_MONTHS(SYSDATE, 24 + MOD(i, 36)),
      LPAD(MOD(i * 7, 10000), 4, '0')
    );
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('200 tarjetas creadas');
END;
/

-- ============================================
-- PASO 11: SERVICIO_PUNTO (asignar puntos a servicios)
-- ============================================
DECLARE
  v_id_punto_partida NUMBER;
  v_id_punto_llegada NUMBER;
  v_punto_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_punto_count FROM Punto;
  
  FOR servicio_rec IN (SELECT idServicio, tipo_servicio FROM Servicio ORDER BY idServicio) LOOP
    SELECT idPunto INTO v_id_punto_partida
    FROM (SELECT idPunto, ROWNUM as rn FROM Punto)
    WHERE rn = MOD(servicio_rec.idServicio - 1, v_punto_count) + 1;
    
    SELECT idPunto INTO v_id_punto_llegada
    FROM (SELECT idPunto, ROWNUM as rn FROM Punto)
    WHERE rn = MOD(servicio_rec.idServicio, v_punto_count) + 1;
    
    INSERT INTO Servicio_Punto (idServicio, rol, orden, idPunto)
    VALUES (servicio_rec.idServicio, 'PARTIDA', 1, v_id_punto_partida);
    
    INSERT INTO Servicio_Punto (idServicio, rol, orden, idPunto)
    VALUES (servicio_rec.idServicio, 'LLEGADA', 2, v_id_punto_llegada);
    
    IF MOD(servicio_rec.idServicio, 200) = 0 THEN
      COMMIT;
    END IF;
  END LOOP;
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Puntos asignados a servicios');
END;
/

-- ============================================
-- PASO 12: ESPECIALIZACIÓN DE SERVICIOS
-- ============================================

-- Servicio_Pasajeros
INSERT INTO Servicio_Pasajeros (idServicio, Cantidad_pasajeros)
SELECT idServicio, 1 + MOD(idServicio, 4)
FROM Servicio WHERE tipo_servicio = 'PASAJEROS';

-- Servicio_Domicilio
DECLARE
  v_id_restaurante NUMBER;
  v_rest_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_rest_count FROM Restaurante;
  
  FOR servicio_rec IN (SELECT idServicio FROM Servicio WHERE tipo_servicio = 'DOMICILIO') LOOP
    SELECT idRestaurante INTO v_id_restaurante
    FROM (SELECT idRestaurante, ROWNUM as rn FROM Restaurante)
    WHERE rn = MOD(servicio_rec.idServicio - 1, v_rest_count) + 1;
    
    INSERT INTO Servicio_Domicilio (idServicio, idRestaurante, Valor_Orden, Cantidad_Productos)
    VALUES (
      servicio_rec.idServicio,
      v_id_restaurante,
      15000 + (MOD(servicio_rec.idServicio, 50) * 1000),
      1 + MOD(servicio_rec.idServicio, 5)
    );
  END LOOP;
  COMMIT;
END;
/

-- Servicio_Mercancias
INSERT INTO Servicio_Mercancias (idServicio, Cantidad_Objetos)
SELECT idServicio, 1 + MOD(idServicio, 10)
FROM Servicio WHERE tipo_servicio = 'MERCANCIAS';

COMMIT;

-- ============================================
-- VERIFICACIÓN FINAL
-- ============================================
SELECT 'Usuarios: ' || COUNT(*) as estadistica FROM Usuario
UNION ALL
SELECT 'Conductores: ' || COUNT(*) FROM Conductor
UNION ALL
SELECT 'Clientes: ' || COUNT(*) FROM Cliente
UNION ALL
SELECT 'Vehículos: ' || COUNT(*) FROM Vehiculo
UNION ALL
SELECT 'Servicios: ' || COUNT(*) FROM Servicio
UNION ALL
SELECT 'Servicios PASAJEROS: ' || COUNT(*) FROM Servicio WHERE tipo_servicio = 'PASAJEROS'
UNION ALL
SELECT 'Servicios DOMICILIO: ' || COUNT(*) FROM Servicio WHERE tipo_servicio = 'DOMICILIO'
UNION ALL
SELECT 'Servicios MERCANCIAS: ' || COUNT(*) FROM Servicio WHERE tipo_servicio = 'MERCANCIAS'
UNION ALL
SELECT 'Promedio servicios/cliente: ' || ROUND(AVG(cnt), 2)
FROM (SELECT COUNT(*) as cnt FROM Servicio GROUP BY idCliente)
UNION ALL
SELECT 'Puntos: ' || COUNT(*) FROM Punto
UNION ALL
SELECT 'Restaurantes: ' || COUNT(*) FROM Restaurante
UNION ALL
SELECT 'Tarjetas: ' || COUNT(*) FROM Tarjeta_credito;
