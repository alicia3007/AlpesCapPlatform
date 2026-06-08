package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Mapa;

public interface MapaRepository extends JpaRepository<Mapa, Long> {

    @Query(value = "SELECT * FROM MAPA", nativeQuery = true)
    Collection<Mapa> darMapas();

    @Query(value = "SELECT * FROM MAPA WHERE IDMAPA = :id", nativeQuery = true)
    Mapa darMapa(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO MAPA (IDCIUDAD, IDPUNTOPARTIDA, IDPUNTOLLEGADA, DIRECCIONAPROX) " +
                   "VALUES (:idCiudad, :idPuntoPartida, :idPuntoLlegada, :direccionAprox)", nativeQuery = true)
    void insertarMapa(@Param("idCiudad") Long idCiudad,
                      @Param("idPuntoPartida") Long idPuntoPartida,
                      @Param("idPuntoLlegada") Long idPuntoLlegada,
                      @Param("direccionAprox") String direccionAprox);

    @Modifying
    @Transactional
    @Query(value = "UPDATE MAPA SET IDCIUDAD = :idCiudad, IDPUNTOPARTIDA = :idPuntoPartida, " +
                   "IDPUNTOLLEGADA = :idPuntoLlegada, DIRECCIONAPROX = :direccionAprox WHERE IDMAPA = :id", nativeQuery = true)
    void actualizarMapa(@Param("id") Long id,
                        @Param("idCiudad") Long idCiudad,
                        @Param("idPuntoPartida") Long idPuntoPartida,
                        @Param("idPuntoLlegada") Long idPuntoLlegada,
                        @Param("direccionAprox") String direccionAprox);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM MAPA WHERE IDMAPA = :id", nativeQuery = true)
    void eliminarMapa(@Param("id") Long id);
}

