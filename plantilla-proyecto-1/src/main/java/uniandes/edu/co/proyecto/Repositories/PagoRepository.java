package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Query(value = "SELECT * FROM PAGO", nativeQuery = true)
    Collection<Pago> darPagos();

    @Query(value = "SELECT * FROM PAGO WHERE IDPAGO = :id", nativeQuery = true)
    Pago darPago(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PAGO (IDSERVICIO, IDTARJETA, MONTO, FECHA_PAGO) VALUES (:idServicio, :idTarjeta, :monto, :fechaPago)", nativeQuery = true)
    void insertarPago(@Param("idServicio") Long idServicio,
                      @Param("idTarjeta") Long idTarjeta,
                      @Param("monto") Double monto,
                      @Param("fechaPago") Date fechaPago);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PAGO SET IDSERVICIO = :idServicio, IDTARJETA = :idTarjeta, MONTO = :monto, FECHA_PAGO = :fechaPago WHERE IDPAGO = :id", nativeQuery = true)
    void actualizarPago(@Param("id") Long id,
                        @Param("idServicio") Long idServicio,
                        @Param("idTarjeta") Long idTarjeta,
                        @Param("monto") Double monto,
                        @Param("fechaPago") Date fechaPago);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PAGO WHERE IDPAGO = :id", nativeQuery = true)
    void eliminarPago(@Param("id") Long id);
}

