package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    @Query(value = "SELECT * FROM RESTAURANTE", nativeQuery = true)
    Collection<Restaurante> darRestaurantes();

    @Query(value = "SELECT * FROM RESTAURANTE WHERE IDRESTAURANTE = :id", nativeQuery = true)
    Restaurante darRestaurante(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO RESTAURANTE (NOMBRE, IDPUNTO) VALUES (:nombre, :idPunto)", nativeQuery = true)
    void insertarRestaurante(@Param("nombre") String nombre,
                             @Param("idPunto") Long idPunto);

    @Modifying
    @Transactional
    @Query(value = "UPDATE RESTAURANTE SET NOMBRE = :nombre, IDPUNTO = :idPunto WHERE IDRESTAURANTE = :id", nativeQuery = true)
    void actualizarRestaurante(@Param("id") Long id,
                               @Param("nombre") String nombre,
                               @Param("idPunto") Long idPunto);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM RESTAURANTE WHERE IDRESTAURANTE = :id", nativeQuery = true)
    void eliminarRestaurante(@Param("id") Long id);
}

