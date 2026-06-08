package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio_Mercancias;

public interface ServicioMercanciasRepository extends JpaRepository<Servicio_Mercancias, Long> {

    @Query(value = "SELECT * FROM SERVICIO_MERCANCIAS", nativeQuery = true)
    Collection<Servicio_Mercancias> darServiciosMercancias();

    @Query(value = "SELECT * FROM SERVICIO_MERCANCIAS WHERE IDSERVICIO = :id", nativeQuery = true)
    Servicio_Mercancias darServicioMercancias(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO_MERCANCIAS (IDSERVICIO, CANTIDAD_OBJETOS) VALUES (:idServicio, :cantidad)", nativeQuery = true)
    void insertarServicioMercancias(@Param("idServicio") Long idServicio,
                                    @Param("cantidad") Integer cantidad);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO_MERCANCIAS SET CANTIDAD_OBJETOS = :cantidad WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void actualizarServicioMercancias(@Param("idServicio") Long idServicio,
                                      @Param("cantidad") Integer cantidad);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO_MERCANCIAS WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void eliminarServicioMercancias(@Param("idServicio") Long idServicio);
}

