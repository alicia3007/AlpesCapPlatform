package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.modelo.Conductor;
import uniandes.edu.co.proyecto.modelo.Servicio;
import uniandes.edu.co.proyecto.modelo.Tarifa;
import uniandes.edu.co.proyecto.modelo.Vehiculo;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    @Query(value = "SELECT * FROM SERVICIO", nativeQuery = true)
    Collection<Servicio> darServicios();

    @Query(value = "SELECT * FROM SERVICIO WHERE IDSERVICIO = :id", nativeQuery = true)
    Servicio darServicio(@Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM SERVICIO WHERE IDSERVICIO = :idServicio AND IDCLIENTE = :idCliente", nativeQuery = true)
    int verificarClienteParticipo(@Param("idServicio") Long idServicio, @Param("idCliente") Long idCliente);

    @Query(value = "SELECT COUNT(*) FROM SERVICIO WHERE IDSERVICIO = :idServicio AND IDCONDUCTOR = :idConductor", nativeQuery = true)
    int verificarConductorParticipo(@Param("idServicio") Long idServicio, @Param("idConductor") Long idConductor);

        @Query(value = "SELECT COUNT(*) > 0 FROM SERVICIO WHERE IDSERVICIO = :idServicio " +
            "AND IDCLIENTE = :idCliente", nativeQuery = true)
        boolean validarClienteParticipo(@Param("idServicio") Long idServicio,
                          @Param("idCliente") Long idCliente);

        @Query(value = "SELECT COUNT(*) > 0 FROM SERVICIO WHERE IDSERVICIO = :idServicio " +
            "AND IDCONDUCTOR = :idConductor", nativeQuery = true)
        boolean validarConductorParticipo(@Param("idServicio") Long idServicio,
                         @Param("idConductor") Long idConductor);

        @Query(value = "SELECT * FROM SERVICIO WHERE IDSERVICIO = " +
            "(SELECT MAX(IDSERVICIO) FROM SERVICIO)", nativeQuery = true)
        Servicio darUltimoServicioRegistrado();

        @Query(value = "SELECT TARIFA FROM TARIFA t " +
            "WHERE t.TIPOSERVICIO = :tipoServicio " +
            "AND t.DISTANCIA >= (SELECT DISTANCIA FROM PUNTO p1, PUNTO p2 " +
            "WHERE p1.IDPUNTO = :puntoOrigen AND p2.IDPUNTO = :puntoDestino) " +
            "ORDER BY t.DISTANCIA ASC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
        Double calcularTarifa(@Param("puntoOrigen") Long puntoOrigen,
                  @Param("puntoDestino") Long puntoDestino,
                  @Param("tipoServicio") String tipoServicio);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO SERVICIO (IDCLIENTE, IDCONDUCTOR, PUNTOORIGEN, PUNTODESTINO, " +
            "TIPOSERVICIO, TARIFA, HORAINICIO) VALUES " +
            "(:idCliente, :idConductor, :puntoOrigen, :puntoDestino, :tipoServicio, :tarifa, " +
            "CURRENT_TIMESTAMP)", nativeQuery = true)
        void iniciarServicio(@Param("idCliente") Long idCliente,
                   @Param("idConductor") Long idConductor,
                   @Param("puntoOrigen") Long puntoOrigen,
                   @Param("puntoDestino") Long puntoDestino,
                   @Param("tipoServicio") String tipoServicio,
                   @Param("tarifa") Double tarifa);

        @Modifying
        @Transactional
        @Query(value = "UPDATE SERVICIO SET HORAFIN = CURRENT_TIMESTAMP, " +
            "LONGITUD = :longitud WHERE IDSERVICIO = :id", nativeQuery = true)
        void finalizarServicio(@Param("id") Long id, @Param("longitud") Double longitud);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO (TIPO_SERVICIO, IDCLIENTE, IDCONDUCTOR, IDVEHICULO, IDTARIFA, DURACION_MINUTOS, " +
                "HORA_INICIO, HORA_FIN, NIVEL_APLICADO, TARIFA_KM_APLICADA, PORCENTAJE_COMISION) " +
                "VALUES (:tipoServicio, :idCliente, :idConductor, :idVehiculo, :idTarifa, :duracionMinutos, " +
                ":horaInicio, :horaFin, :nivelAplicado, :tarifaKmAplicada, :porcentajeComision)",
        nativeQuery = true)
    void insertarServicio(
        @Param("tipoServicio") String tipoServicio,
        @Param("idCliente") Long idCliente,
        @Param("idConductor") Long idConductor,
        @Param("idVehiculo") Long idVehiculo,
        @Param("idTarifa") Long idTarifa,
        @Param("duracionMinutos") Integer duracionMinutos,
        @Param("horaInicio") java.util.Date horaInicio,
        @Param("horaFin") java.util.Date horaFin,
        @Param("nivelAplicado") String nivelAplicado,
        @Param("tarifaKmAplicada") Double tarifaKmAplicada,
        @Param("porcentajeComision") Double porcentajeComision
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO SET TIPO_SERVICIO = :tipoServicio, IDCLIENTE = :idCliente, IDCONDUCTOR = :idConductor, " +
                   "IDVEHICULO = :idVehiculo, IDTARIFA = :idTarifa, DURACION_MINUTOS = :duracionMinutos, " +
                   "HORA_INICIO = :horaInicio, HORA_FIN = :horaFin, NIVEL_APLICADO = :nivelAplicado, " +
                   "TARIFA_KM_APLICADA = :tarifaKmAplicada, PORCENTAJE_COMISION = :porcentajeComision " +
                   "WHERE IDSERVICIO = :id", nativeQuery = true)
    void actualizarServicio(@Param("id") Long id,
                            @Param("tipoServicio") String tipoServicio,
                            @Param("idCliente") Cliente idCliente,
                            @Param("idConductor") Conductor idConductor,
                            @Param("idVehiculo") Vehiculo idVehiculo,
                            @Param("idTarifa") Tarifa idTarifa,
                            @Param("duracionMinutos") Integer duracionMinutos,
                            @Param("horaInicio") java.util.Date horaInicio,
                            @Param("horaFin") java.util.Date horaFin,
                            @Param("nivelAplicado") String nivelAplicado,
                            @Param("tarifaKmAplicada") Double tarifaKmAplicada,
                            @Param("porcentajeComision") Double porcentajeComision);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO WHERE IDSERVICIO = :id", nativeQuery = true)
    void eliminarServicio(@Param("id") Long id);

    @Query(value = """
    SELECT COUNT(*) 
    FROM SERVICIO 
    WHERE IDCONDUCTOR = :idConductor
      AND ((HORA_INICIO < :horaFin) AND (HORA_FIN > :horaInicio))
    """, nativeQuery = true)
    int verificarDisponibilidad(
    @Param("idConductor") Long idConductor,
    @Param("horaInicio") java.util.Date horaInicio,
    @Param("horaFin") java.util.Date horaFin
);

    @Query(value = """
        SELECT COUNT(*) 
        FROM SERVICIO 
        WHERE IDVEHICULO = :idVehiculo
        AND IDSERVICIO <> :idServicio
        AND HORA_INICIO < :horaFin
        AND HORA_FIN > :horaInicio
    """, nativeQuery = true)
    int verificarTraslapeDisponibilidad(
        @Param("idVehiculo") Long idVehiculo,
        @Param("idServicio") Long idServicio,
        @Param("horaInicio") java.util.Date horaInicio,
        @Param("horaFin") java.util.Date horaFin
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE SERVICIO 
        SET HORA_FIN = :horaFin,
            DURACION_MINUTOS = :duracionMinutos, 
            ESTADO = 'COMPLETADO'
        WHERE IDSERVICIO = :idServicio
    """, nativeQuery = true)
    void finalizarServicio(
        @Param("idServicio") Long idServicio,
        @Param("horaFin") java.util.Date horaFin,
        @Param("duracionMinutos") int duracionMinutos
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM SERVICIO
        WHERE IDCONDUCTOR = :idConductor
        AND IDSERVICIO <> :idServicio
        AND HORA_INICIO < :horaFin
        AND HORA_FIN > :horaInicio
    """, nativeQuery = true)
    int verificarTraslapeDisponibilidadConductor(
        @Param("idConductor") Long idConductor,
        @Param("idServicio") Long idServicio,
        @Param("horaInicio") java.util.Date horaInicio,
        @Param("horaFin") java.util.Date horaFin
    );

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE SERVICIO
        SET HORA_INICIO = :horaInicio, HORA_FIN = :horaFin
        WHERE IDSERVICIO = :idServicio
    """, nativeQuery = true)
    void actualizarDisponibilidadServicio(
        @Param("idServicio") Long idServicio,
        @Param("horaInicio") java.util.Date horaInicio,
        @Param("horaFin") java.util.Date horaFin
    );

    @Query(value = """
    SELECT v.IDVEHICULO 
    FROM VEHICULO v
    JOIN CONDUCTOR c ON v.IDCONDUCTOR = c.IDUSUARIO
    WHERE NOT EXISTS (
        SELECT 1 FROM SERVICIO s
        WHERE s.IDVEHICULO = v.IDVEHICULO
        AND s.HORA_FIN > CURRENT_TIMESTAMP
        AND s.HORA_INICIO < CURRENT_TIMESTAMP
    )
    ORDER BY RANDOM() 
    FETCH FIRST 1 ROWS ONLY
    """, nativeQuery = true)
    Long asignarVehiculoDisponible();

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO SET HORA_FIN = :horaFin WHERE IDSERVICIO = :id", nativeQuery = true)
    void actualizarHoraFin(@Param("id") Long id, @Param("horaFin") java.util.Date horaFin);


    @Query(value = """
    SELECT 
        s.idServicio,
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
    WHERE s.idCliente = :idUsuario
    ORDER BY s.hora_inicio DESC
    """, nativeQuery = true)
List<Object[]> consultarHistorialServicios(@Param("idUsuario") Long idUsuario);

    @Query(value = """
        SELECT 
            u.idUsuario,
            u.nombre,
            u.correo,
            COUNT(s.idServicio) AS total_servicios
        FROM Conductor c
        JOIN Usuario u ON c.idUsuario = u.idUsuario
        JOIN Servicio s ON c.idUsuario = s.idConductor
        GROUP BY u.idUsuario, u.nombre, u.correo
        ORDER BY total_servicios DESC
        FETCH FIRST 20 ROWS ONLY
        """, nativeQuery = true)
    List<Object[]> top20ConductoresMasServicios();

    @Query(value = """
    WITH ServiciosPorCiudad AS (
        SELECT s.idServicio,
               s.tipo_servicio,
               s.nivel_aplicado,
               c.nombre AS ciudad
        FROM Servicio s
        JOIN Servicio_Punto sp ON s.idServicio = sp.idServicio
        JOIN Punto p ON sp.idPunto = p.idPunto
        JOIN Ciudad c ON p.idCiudad = c.idCiudad
        WHERE s.hora_inicio BETWEEN :fechaInicio AND :fechaFin
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
           ROUND((COUNT(spc.idServicio) * 100.0 / ts.total_general), 2) AS porcentaje
    FROM ServiciosPorCiudad spc
    CROSS JOIN TotalServicios ts 
    WHERE spc.ciudad = :ciudad
    GROUP BY spc.ciudad, spc.tipo_servicio, spc.nivel_aplicado, ts.total_general
    ORDER BY total_servicios DESC
""", nativeQuery = true)
List<Object[]> utilizacionServiciosPorCiudad(
    @Param("ciudad") String ciudad,
    @Param("fechaInicio") java.util.Date fechaInicio,
    @Param("fechaFin") java.util.Date fechaFin
);

    @Query(value = "SELECT * FROM SERVICIO WHERE IDSERVICIO = (SELECT MAX(IDSERVICIO) FROM SERVICIO)", nativeQuery = true)
    Servicio findTopByOrderByIdServicioDesc();
    
    // Métodos para RF8
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO (TIPO_SERVICIO, IDCLIENTE, IDCONDUCTOR, IDVEHICULO, IDTARIFA, " +
                   "DURACION_MINUTOS, HORA_INICIO, HORA_FIN, NIVEL_APLICADO, TARIFA_KM_APLICADA, PORCENTAJE_COMISION) " +
                   "VALUES (:tipoServicio, :idCliente, :idConductor, :idVehiculo, :idTarifa, " +
                   ":duracionMinutos, CURRENT_TIMESTAMP, NULL, :nivelAplicado, :tarifaKmAplicada, 0.10)", 
           nativeQuery = true)
    void registrarInicioServicio(@Param("tipoServicio") String tipoServicio,
                                 @Param("idCliente") Long idCliente,
                                 @Param("idConductor") Long idConductor,
                                 @Param("idVehiculo") Long idVehiculo,
                                 @Param("idTarifa") Long idTarifa,
                                 @Param("duracionMinutos") Integer duracionMinutos,
                                 @Param("nivelAplicado") String nivelAplicado,
                                 @Param("tarifaKmAplicada") Double tarifaKmAplicada);
    
    @Query(value = "SELECT IDSERVICIO FROM SERVICIO WHERE IDSERVICIO = (SELECT MAX(IDSERVICIO) FROM SERVICIO)", 
           nativeQuery = true)
    Long obtenerUltimoIdServicio();
    
    // RFC3 - Dinero ganado por conductor por vehículo discriminado por tipo de servicio
    @Query(value = """
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
        WHERE u.idUsuario = :idConductor
        GROUP BY u.idUsuario, u.nombre, v.placa, v.tipo, s.tipo_servicio
        ORDER BY v.placa, s.tipo_servicio
        """, nativeQuery = true)
    List<Object[]> consultarDineroGanadoConductor(@Param("idConductor") Long idConductor);
}












