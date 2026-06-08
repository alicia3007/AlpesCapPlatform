package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Tarifa;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    @Query(value = "SELECT * FROM TARIFA", nativeQuery = true)
    Collection<Tarifa> darTarifas();

    @Query(value = "SELECT * FROM TARIFA WHERE IDTARIFA = :id", nativeQuery = true)
    Tarifa darTarifa(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO TARIFA (TIPOSERVICIO, NIVEL, VALORKM) VALUES (:tipoServicio, :nivel, :valorKm)", nativeQuery = true)
    void insertarTarifa(@Param("tipoServicio") String tipoServicio,
                        @Param("nivel") String nivel,
                        @Param("valorKm") Double valorKm);

    @Modifying
    @Transactional
    @Query(value = "UPDATE TARIFA SET TIPOSERVICIO = :tipoServicio, NIVEL = :nivel, VALORKM = :valorKm WHERE IDTARIFA = :id", nativeQuery = true)
    void actualizarTarifa(@Param("id") Long id,
                          @Param("tipoServicio") String tipoServicio,
                          @Param("nivel") String nivel,
                          @Param("valorKm") Double valorKm);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM TARIFA WHERE IDTARIFA = :id", nativeQuery = true)
    void eliminarTarifa(@Param("id") Long id);
    
    // Métodos para RF8
    @Query(value = "SELECT * FROM TARIFA WHERE TIPOSERVICIO = :tipoServicio AND NIVEL = :nivel", nativeQuery = true)
    Tarifa buscarTarifaPorTipoYNivel(@Param("tipoServicio") String tipoServicio, @Param("nivel") String nivel);
    
    @Query(value = "SELECT VALORKM FROM TARIFA WHERE TIPOSERVICIO = :tipoServicio AND NIVEL = :nivel", nativeQuery = true)
    Double obtenerValorKmPorTipoYNivel(@Param("tipoServicio") String tipoServicio, @Param("nivel") String nivel);
    
    @Query(value = "SELECT IDTARIFA FROM TARIFA WHERE TIPOSERVICIO = :tipoServicio AND NIVEL = :nivel", nativeQuery = true)
    Long obtenerIdTarifaPorTipoYNivel(@Param("tipoServicio") String tipoServicio, @Param("nivel") String nivel);
}


