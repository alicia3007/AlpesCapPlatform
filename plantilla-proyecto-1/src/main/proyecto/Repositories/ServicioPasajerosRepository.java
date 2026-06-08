package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio_Pasajeros;

public interface ServicioPasajerosRepository extends JpaRepository<Servicio_Pasajeros, Long> {

    @Query(value = "SELECT * FROM SERVICIO_PASAJEROS", nativeQuery = true)
    Collection<Servicio_Pasajeros> darServiciosPasajeros();

    @Query(value = "SELECT * FROM SERVICIO_PASAJEROS WHERE IDSERVICIO = :id", nativeQuery = true)
    Servicio_Pasajeros darServicioPasajeros(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO_PASAJEROS (IDSERVICIO, CANTIDAD_PASAJEROS) VALUES (:idServicio, :cantidad)", nativeQuery = true)
    void insertarServicioPasajeros(@Param("idServicio") Long idServicio,
                                   @Param("cantidad") Integer cantidad);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO_PASAJEROS SET CANTIDAD_PASAJEROS = :cantidad WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void actualizarServicioPasajeros(@Param("idServicio") Long idServicio,
                                     @Param("cantidad") Integer cantidad);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO_PASAJEROS WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void eliminarServicioPasajeros(@Param("idServicio") Long idServicio);
}

