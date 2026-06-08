package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Punto;

public interface PuntoRepository extends JpaRepository<Punto, Long> {

    @Query(value = "SELECT * FROM PUNTO", nativeQuery = true)
    Collection<Punto> darPuntos();

    @Query(value = "SELECT * FROM PUNTO WHERE IDPUNTO = :id", nativeQuery = true)
    Punto darPunto(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PUNTO (NOMBRE, DIRECCION, LATITUD, LONGITUD, IDCIUDAD) " +
                   "VALUES (:nombre, :direccion, :latitud, :longitud, :idCiudad)", nativeQuery = true)
    void insertarPunto(@Param("nombre") String nombre,
                       @Param("direccion") String direccion,
                       @Param("latitud") Double latitud,
                       @Param("longitud") Double longitud,
                       @Param("idCiudad") Long idCiudad);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PUNTO SET NOMBRE = :nombre, DIRECCION = :direccion, LATITUD = :latitud, " +
                   "LONGITUD = :longitud, IDCIUDAD = :idCiudad WHERE IDPUNTO = :id", nativeQuery = true)
    void actualizarPunto(@Param("id") Long id,
                         @Param("nombre") String nombre,
                         @Param("direccion") String direccion,
                         @Param("latitud") Double latitud,
                         @Param("longitud") Double longitud,
                         @Param("idCiudad") Long idCiudad);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PUNTO WHERE IDPUNTO = :id", nativeQuery = true)
    void eliminarPunto(@Param("id") Long id);
    
    @Query(value = "SELECT * FROM PUNTO WHERE IDPUNTO = (SELECT MAX(IDPUNTO) FROM PUNTO)", nativeQuery = true)
    Punto findTopByOrderByIdPuntoDesc();
    
    // Métodos para RF8
    @Query(value = "SELECT " +
                   "SQRT(POWER(p2.LATITUD - p1.LATITUD, 2) + POWER(p2.LONGITUD - p1.LONGITUD, 2)) * 111.32 AS distancia " +
                   "FROM PUNTO p1, PUNTO p2 " +
                   "WHERE p1.IDPUNTO = :idPuntoPartida AND p2.IDPUNTO = :idPuntoLlegada", 
           nativeQuery = true)
    Double calcularDistanciaEnKm(@Param("idPuntoPartida") Long idPuntoPartida, 
                                   @Param("idPuntoLlegada") Long idPuntoLlegada);
}


