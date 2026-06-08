package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio_Punto;
import uniandes.edu.co.proyecto.modelo.Servicio_PuntoPK;

public interface ServicioPuntoRepository extends JpaRepository<Servicio_Punto, Servicio_PuntoPK> {

    @Query(value = "SELECT * FROM SERVICIO_PUNTO", nativeQuery = true)
    Collection<Servicio_Punto> darServicioPuntos();

    @Query(value = "SELECT * FROM SERVICIO_PUNTO WHERE IDSERVICIO = :idServicio AND ROL = :rol AND ORDEN = :orden", nativeQuery = true)
    Servicio_Punto darServicioPunto(@Param("idServicio") Long idServicio,
                                    @Param("rol") String rol,
                                    @Param("orden") Integer orden);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO_PUNTO (IDSERVICIO, ROL, ORDEN, IDPUNTO) VALUES (:idServicio, :rol, :orden, :idPunto)", nativeQuery = true)
    void insertarServicioPunto(@Param("idServicio") Long idServicio,
                               @Param("rol") String rol,
                               @Param("orden") Integer orden,
                               @Param("idPunto") Long idPunto);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO_PUNTO SET IDPUNTO = :idPunto WHERE IDSERVICIO = :idServicio AND ROL = :rol AND ORDEN = :orden", nativeQuery = true)
    void actualizarServicioPunto(@Param("idServicio") Long idServicio,
                                 @Param("rol") String rol,
                                 @Param("orden") Integer orden,
                                 @Param("idPunto") Long idPunto);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO_PUNTO WHERE IDSERVICIO = :idServicio AND ROL = :rol AND ORDEN = :orden", nativeQuery = true)
    void eliminarServicioPunto(@Param("idServicio") Long idServicio,
                               @Param("rol") String rol,
                               @Param("orden") Integer orden);
}

