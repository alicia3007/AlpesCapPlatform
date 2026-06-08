package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Actividad;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    @Query(value = "SELECT * FROM ACTIVIDAD", nativeQuery = true)
    Collection<Actividad> darActividades();

    @Query(value = "SELECT * FROM ACTIVIDAD WHERE IDACTIVIDAD = :id", nativeQuery = true)
    Actividad darActividad(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ACTIVIDAD (DIASEMANA, FRANJASHORARIAS, TIPOSERVICIO, HORAINICIO, HORAFIN, IDVEHICULO, IDCONDUCTOR) " +
                   "VALUES (:diaSemana, :franjasHorarias, :tipoServicio, :horaInicio, :horaFin, :idVehiculo, :idConductor)", nativeQuery = true)
    void insertarActividad(@Param("diaSemana") int diaSemana,
                           @Param("franjasHorarias") String franjasHorarias,
                           @Param("tipoServicio") String tipoServicio,
                           @Param("horaInicio") java.util.Date horaInicio,
                           @Param("horaFin") java.util.Date horaFin,
                           @Param("idVehiculo") Long idVehiculo,
                           @Param("idConductor") Long idConductor);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ACTIVIDAD SET DIASEMANA = :diaSemana, FRANJASHORARIAS = :franjasHorarias, TIPOSERVICIO = :tipoServicio, " +
                   "HORAINICIO = :horaInicio, HORAFIN = :horaFin, IDVEHICULO = :idVehiculo, IDCONDUCTOR = :idConductor WHERE IDACTIVIDAD = :id", nativeQuery = true)
    void actualizarActividad(@Param("id") Long id,
                             @Param("diaSemana") int diaSemana,
                             @Param("franjasHorarias") String franjasHorarias,
                             @Param("tipoServicio") String tipoServicio,
                             @Param("horaInicio") java.util.Date horaInicio,
                             @Param("horaFin") java.util.Date horaFin,
                             @Param("idVehiculo") Long idVehiculo,
                             @Param("idConductor") Long idConductor);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ACTIVIDAD WHERE IDACTIVIDAD = :id", nativeQuery = true)
    void eliminarActividad(@Param("id") Long id);
}

