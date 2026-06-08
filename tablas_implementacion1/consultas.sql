SELECT s.idServicio,
       s.tipo_servicio,
       s.hora_inicio,
       s.hora_fin,
       s.costo_total,
       pp.direccion AS direccion_partida,
       pl.direccion AS direccion_llegada,
       u_conductor.nombre AS conductor,
       v.placa
FROM Servicio s
JOIN Usuario u_cliente ON s.idCliente = u_cliente.idUsuario
JOIN Usuario u_conductor ON s.idConductor = u_conductor.idUsuario
JOIN Vehiculo v ON s.idVehiculo = v.idVehiculo
LEFT JOIN Servicio_Punto sp_partida 
    ON s.idServicio = sp_partida.idServicio AND sp_partida.rol = 'PARTIDA'
LEFT JOIN Punto pp ON sp_partida.idPunto = pp.idPunto
LEFT JOIN Servicio_Punto sp_llegada 
    ON s.idServicio = sp_llegada.idServicio AND sp_llegada.rol = 'LLEGADA'
LEFT JOIN Punto pl ON sp_llegada.idPunto = pl.idPunto
WHERE s.idCliente = &idUsuario
ORDER BY s.hora_inicio DESC; 


SELECT u.idUsuario,
       u.nombre,
       u.correo,
       COUNT(s.idServicio) AS total_servicios
FROM Conductor c
JOIN Usuario u ON c.idUsuario = u.idUsuario
JOIN Servicio s ON c.idUsuario = s.idConductor
GROUP BY u.idUsuario, u.nombre, u.correo
ORDER BY total_servicios DESC
FETCH FIRST 20 ROWS ONLY;


SELECT u.idUsuario,
       u.nombre,
       v.placa,
       v.tipo AS tipo_vehiculo,
       s.tipo_servicio,
       COUNT(s.idServicio) AS cantidad_servicios,
       SUM(s.costo_total) AS total_bruto,
       SUM(s.comision_valor) AS total_comision,
       SUM(s.costo_total - s.comision_valor) AS total_neto_conductor
FROM Servicio s
JOIN Usuario u ON s.idConductor = u.idUsuario
JOIN Vehiculo v ON s.idVehiculo = v.idVehiculo
GROUP BY u.idUsuario, u.nombre, v.placa, v.tipo, s.tipo_servicio
ORDER BY u.nombre, v.placa, s.tipo_servicio;


WITH ServiciosPorCiudad AS (
    SELECT s.idServicio,
           s.tipo_servicio,
           s.nivel_aplicado,
           c.nombre AS ciudad
    FROM Servicio s
    JOIN Servicio_Punto sp ON s.idServicio = sp.idServicio
    JOIN Punto p ON sp.idPunto = p.idPunto
    JOIN Ciudad c ON p.idCiudad = c.idCiudad
    WHERE s.hora_inicio BETWEEN :fecha_inicio AND :fecha_fin
    GROUP BY s.idServicio, s.tipo_servicio, s.nivel_aplicado, c.nombre
),
TotalServicios AS (
    SELECT COUNT(*) AS total_general
    FROM ServiciosPorCiudad
    WHERE ciudad = :ciudad
)

SELECT spc.ciudad,
       spc.tipo_servicio,
       spc.nivel_aplicado,
       COUNT(spc.idServicio) AS total_servicios,
       ROUND(
           (COUNT(spc.idServicio) * 100.0 / ts.total_general), 2
       ) AS porcentaje
FROM ServiciosPorCiudad spc
CROSS JOIN TotalServicios ts 
WHERE spc.ciudad = :ciudad
GROUP BY spc.ciudad, spc.tipo_servicio, spc.nivel_aplicado, ts.total_general
ORDER BY total_servicios DESC;
