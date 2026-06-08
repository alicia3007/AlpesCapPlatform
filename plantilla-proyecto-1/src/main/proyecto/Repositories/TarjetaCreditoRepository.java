package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.TarjetaCredito;

public interface TarjetaCreditoRepository extends JpaRepository<TarjetaCredito, Long> {

    @Query(value = "SELECT * FROM TARJETA_CREDITO", nativeQuery = true)
    Collection<TarjetaCredito> darTarjetasCredito();

    @Query(value = "SELECT * FROM TARJETA_CREDITO WHERE IDTARJETA = :id", nativeQuery = true)
    TarjetaCredito darTarjetaCredito(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO TARJETA_CREDITO (NUMERO, NOMBRE_TARJETA, FECHA_VENCIMIENTO, CODIGO) " +
                   "VALUES (:numero, :nombreTarjeta, :fechaVencimiento, :codigo)", nativeQuery = true)
    void insertarTarjetaCredito(@Param("numero") Long numero,
                         @Param("nombreTarjeta") String nombreTarjeta,
                         @Param("fechaVencimiento") java.util.Date fechaVencimiento,
                         @Param("codigo") String codigo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE TARJETA_CREDITO SET NUMERO = :numero, NOMBRE_TARJETA = :nombreTarjeta, " +
                   "FECHA_VENCIMIENTO = :fechaVencimiento, CODIGO = :codigo WHERE IDTARJETA = :id", nativeQuery = true)
    void actualizarTarjetaCredito(@Param("id") Long id,
                           @Param("numero") Long numero,
                           @Param("nombreTarjeta") String nombreTarjeta,
                           @Param("fechaVencimiento") java.util.Date fechaVencimiento,
                           @Param("codigo") String codigo);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TARJETA_CREDITO WHERE IDTARJETA = :id", nativeQuery = true)
    void eliminarTarjetaCredito(@Param("id") Long id);
}

