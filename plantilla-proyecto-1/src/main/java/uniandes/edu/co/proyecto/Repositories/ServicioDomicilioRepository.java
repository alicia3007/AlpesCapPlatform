package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio_Domicilio;

public interface ServicioDomicilioRepository extends JpaRepository<Servicio_Domicilio, Long> {

    @Query(value = "SELECT * FROM SERVICIO_DOMICILIO", nativeQuery = true)
    Collection<Servicio_Domicilio> darServiciosDomicilio();

    @Query(value = "SELECT * FROM SERVICIO_DOMICILIO WHERE IDSERVICIO = :id", nativeQuery = true)
    Servicio_Domicilio darServicioDomicilio(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO SERVICIO_DOMICILIO (IDSERVICIO, IDRESTAURANTE, VALOR_ORDEN, CANTIDAD_PRODUCTOS) " +
                   "VALUES (:idServicio, :idRestaurante, :valorOrden, :cantidadProductos)", nativeQuery = true)
    void insertarServicioDomicilio(@Param("idServicio") Long idServicio,
                                   @Param("idRestaurante") Long idRestaurante,
                                   @Param("valorOrden") Double valorOrden,
                                   @Param("cantidadProductos") Integer cantidadProductos);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SERVICIO_DOMICILIO SET IDRESTAURANTE = :idRestaurante, VALOR_ORDEN = :valorOrden, " +
                   "CANTIDAD_PRODUCTOS = :cantidadProductos WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void actualizarServicioDomicilio(@Param("idServicio") Long idServicio,
                                     @Param("idRestaurante") Long idRestaurante,
                                     @Param("valorOrden") Double valorOrden,
                                     @Param("cantidadProductos") Integer cantidadProductos);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SERVICIO_DOMICILIO WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void eliminarServicioDomicilio(@Param("idServicio") Long idServicio);
}

