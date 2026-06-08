package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Disponibilidad;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END 
        FROM DISPONIBILIDAD 
        WHERE IDCONDUCTOR = :idConductor 
        AND (
            -- Convertir días a números para comparación correcta
            -- LUNES=1, MARTES=2, MIERCOLES=3, JUEVES=4, VIERNES=5, SABADO=6, DOMINGO=7
            (CASE 
                WHEN DIAINICIO = 'LUNES' THEN 1
                WHEN DIAINICIO = 'MARTES' THEN 2
                WHEN DIAINICIO = 'MIERCOLES' THEN 3
                WHEN DIAINICIO = 'JUEVES' THEN 4
                WHEN DIAINICIO = 'VIERNES' THEN 5
                WHEN DIAINICIO = 'SABADO' THEN 6
                WHEN DIAINICIO = 'DOMINGO' THEN 7
                ELSE 0
            END) <= 
            (CASE 
                WHEN :diaFin = 'LUNES' THEN 1
                WHEN :diaFin = 'MARTES' THEN 2
                WHEN :diaFin = 'MIERCOLES' THEN 3
                WHEN :diaFin = 'JUEVES' THEN 4
                WHEN :diaFin = 'VIERNES' THEN 5
                WHEN :diaFin = 'SABADO' THEN 6
                WHEN :diaFin = 'DOMINGO' THEN 7
                ELSE 0
            END)
            AND
            (CASE 
                WHEN DIAFIN = 'LUNES' THEN 1
                WHEN DIAFIN = 'MARTES' THEN 2
                WHEN DIAFIN = 'MIERCOLES' THEN 3
                WHEN DIAFIN = 'JUEVES' THEN 4
                WHEN DIAFIN = 'VIERNES' THEN 5
                WHEN DIAFIN = 'SABADO' THEN 6
                WHEN DIAFIN = 'DOMINGO' THEN 7
                ELSE 0
            END) >= 
            (CASE 
                WHEN :diaInicio = 'LUNES' THEN 1
                WHEN :diaInicio = 'MARTES' THEN 2
                WHEN :diaInicio = 'MIERCOLES' THEN 3
                WHEN :diaInicio = 'JUEVES' THEN 4
                WHEN :diaInicio = 'VIERNES' THEN 5
                WHEN :diaInicio = 'SABADO' THEN 6
                WHEN :diaInicio = 'DOMINGO' THEN 7
                ELSE 0
            END)
        )
        AND (HORAINICIO <= :horaFin AND HORAFIN >= :horaInicio)
        """, nativeQuery = true)
    int existeSuperposicion(@Param("idConductor") Long idConductor,
                              @Param("diaInicio") String diaInicio,
                              @Param("diaFin") String diaFin,
                              @Param("horaInicio") String horaInicio,
                              @Param("horaFin") String horaFin);

       @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM DISPONIBILIDAD WHERE ID = :id", nativeQuery = true)
       int existeDisponibilidad(@Param("id") Long id);

    @Query(value = "SELECT IDCONDUCTOR FROM DISPONIBILIDAD WHERE ID = :id", nativeQuery = true)
    Long darConductorDeDisponibilidad(@Param("id") Long id);

    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END 
        FROM DISPONIBILIDAD 
        WHERE IDCONDUCTOR = :idConductor 
        AND ID != :idDisponibilidad 
        AND (
            -- Convertir días a números para comparación correcta
            (CASE 
                WHEN DIAINICIO = 'LUNES' THEN 1
                WHEN DIAINICIO = 'MARTES' THEN 2
                WHEN DIAINICIO = 'MIERCOLES' THEN 3
                WHEN DIAINICIO = 'JUEVES' THEN 4
                WHEN DIAINICIO = 'VIERNES' THEN 5
                WHEN DIAINICIO = 'SABADO' THEN 6
                WHEN DIAINICIO = 'DOMINGO' THEN 7
                ELSE 0
            END) <= 
            (CASE 
                WHEN :diaFin = 'LUNES' THEN 1
                WHEN :diaFin = 'MARTES' THEN 2
                WHEN :diaFin = 'MIERCOLES' THEN 3
                WHEN :diaFin = 'JUEVES' THEN 4
                WHEN :diaFin = 'VIERNES' THEN 5
                WHEN :diaFin = 'SABADO' THEN 6
                WHEN :diaFin = 'DOMINGO' THEN 7
                ELSE 0
            END)
            AND
            (CASE 
                WHEN DIAFIN = 'LUNES' THEN 1
                WHEN DIAFIN = 'MARTES' THEN 2
                WHEN DIAFIN = 'MIERCOLES' THEN 3
                WHEN DIAFIN = 'JUEVES' THEN 4
                WHEN DIAFIN = 'VIERNES' THEN 5
                WHEN DIAFIN = 'SABADO' THEN 6
                WHEN DIAFIN = 'DOMINGO' THEN 7
                ELSE 0
            END) >= 
            (CASE 
                WHEN :diaInicio = 'LUNES' THEN 1
                WHEN :diaInicio = 'MARTES' THEN 2
                WHEN :diaInicio = 'MIERCOLES' THEN 3
                WHEN :diaInicio = 'JUEVES' THEN 4
                WHEN :diaInicio = 'VIERNES' THEN 5
                WHEN :diaInicio = 'SABADO' THEN 6
                WHEN :diaInicio = 'DOMINGO' THEN 7
                ELSE 0
            END)
        )
        AND (HORAINICIO <= :horaFin AND HORAFIN >= :horaInicio)
        """, nativeQuery = true)
    int existeSuperposicionExcluyendo(@Param("idConductor") Long idConductor,
                                        @Param("idDisponibilidad") Long idDisponibilidad,
                                        @Param("diaInicio") String diaInicio,
                                        @Param("diaFin") String diaFin,
                                        @Param("horaInicio") String horaInicio,
                                        @Param("horaFin") String horaFin);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO DISPONIBILIDAD (IDCONDUCTOR, IDVEHICULO, TIPOSERVICIO, " +
           "DIAINICIO, DIAFIN, HORAINICIO, HORAFIN) VALUES " +
           "(:idConductor, :idVehiculo, :tipoServicio, :diaInicio, :diaFin, :horaInicio, :horaFin)", 
           nativeQuery = true)
    void insertarDisponibilidad(@Param("idConductor") Long idConductor,
                               @Param("idVehiculo") Long idVehiculo,
                               @Param("tipoServicio") String tipoServicio,
                               @Param("diaInicio") String diaInicio,
                               @Param("diaFin") String diaFin,
                               @Param("horaInicio") String horaInicio,
                               @Param("horaFin") String horaFin);

    @Modifying
    @Transactional
    @Query(value = "UPDATE DISPONIBILIDAD SET DIAINICIO = :diaInicio, DIAFIN = :diaFin, " +
           "HORAINICIO = :horaInicio, HORAFIN = :horaFin WHERE ID = :id", nativeQuery = true)
    void actualizarDisponibilidad(@Param("id") Long id,
                                 @Param("diaInicio") String diaInicio,
                                 @Param("diaFin") String diaFin,
                                 @Param("horaInicio") String horaInicio,
                                 @Param("horaFin") String horaFin);

    @Query(value = "SELECT IDCONDUCTOR FROM DISPONIBILIDAD d " +
           "WHERE d.TIPOSERVICIO = :tipoServicio " +
           "AND d.IDCONDUCTOR NOT IN (SELECT IDCONDUCTOR FROM SERVICIO WHERE HORAFIN IS NULL) " +
           "FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    Long buscarConductorDisponible(@Param("tipoServicio") String tipoServicio);

    // Consultar todas las disponibilidades
    @Query(value = "SELECT * FROM DISPONIBILIDAD ORDER BY ID", nativeQuery = true)
    Collection<Disponibilidad> darDisponibilidades();

    // Consultar disponibilidades por conductor
    @Query(value = "SELECT * FROM DISPONIBILIDAD WHERE IDCONDUCTOR = :idConductor ORDER BY ID", nativeQuery = true)
    Collection<Disponibilidad> darDisponibilidadesPorConductor(@Param("idConductor") Long idConductor);

    // Consultar una disponibilidad por ID
    @Query(value = "SELECT * FROM DISPONIBILIDAD WHERE ID = :id", nativeQuery = true)
    Disponibilidad darDisponibilidadPorId(@Param("id") Long id);
}