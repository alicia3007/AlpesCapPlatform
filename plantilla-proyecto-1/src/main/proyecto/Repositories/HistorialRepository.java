package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Historial;

public interface HistorialRepository extends JpaRepository<Historial, Long> {

    @Query(value = "SELECT * FROM HISTORIAL", nativeQuery = true)
    Collection<Historial> darHistoriales();

    @Query(value = "SELECT * FROM HISTORIAL WHERE IDHISTORIAL = :id", nativeQuery = true)
    Historial darHistorial(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO HISTORIAL (IDSERVICIO, IDCLIENTE, IDCONDUCTOR, IDPUNTOPARTIDA, IDPUNTOLLEGADA) " +
                   "VALUES (:idServicio, :idCliente, :idConductor, :idPuntoPartida, :idPuntoLlegada)", nativeQuery = true)
    void insertarHistorial(@Param("idServicio") Long idServicio,
                           @Param("idCliente") Long idCliente,
                           @Param("idConductor") Long idConductor,
                           @Param("idPuntoPartida") Long idPuntoPartida,
                           @Param("idPuntoLlegada") Long idPuntoLlegada);

    @Modifying
    @Transactional
    @Query(value = "UPDATE HISTORIAL SET IDSERVICIO = :idServicio, IDCLIENTE = :idCliente, IDCONDUCTOR = :idConductor, " +
                   "IDPUNTOPARTIDA = :idPuntoPartida, IDPUNTOLLEGADA = :idPuntoLlegada WHERE IDHISTORIAL = :id", nativeQuery = true)
    void actualizarHistorial(@Param("id") Long id,
                             @Param("idServicio") Long idServicio,
                             @Param("idCliente") Long idCliente,
                             @Param("idConductor") Long idConductor,
                             @Param("idPuntoPartida") Long idPuntoPartida,
                             @Param("idPuntoLlegada") Long idPuntoLlegada);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM HISTORIAL WHERE IDHISTORIAL = :id", nativeQuery = true)
    void eliminarHistorial(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE HISTORIAL SET HORA_FIN = :horaFin, DISTANCIA_KM = :distanciaKm WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    void actualizarHistorialFinPorServicio(@Param("idServicio") Long idServicio,
                                           @Param("horaFin") java.util.Date horaFin,
                                           @Param("distanciaKm") Double distanciaKm);
}

