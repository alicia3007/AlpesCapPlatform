package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    @Query(value = "SELECT * FROM CIUDAD", nativeQuery = true)
    Collection<Ciudad> darCiudades();

    @Query(value = "SELECT * FROM CIUDAD WHERE IDCIUDAD = :id", nativeQuery = true)
    Ciudad darCiudad(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CIUDAD (NOMBRE) VALUES (:nombre)", nativeQuery = true)
    void insertarCiudad(@Param("nombre") String nombre);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CIUDAD SET NOMBRE = :nombre WHERE IDCIUDAD = :id", nativeQuery = true)
    void actualizarCiudad(@Param("id") Long id, @Param("nombre") String nombre);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CIUDAD WHERE IDCIUDAD = :id", nativeQuery = true)
    void eliminarCiudad(@Param("id") Long id);

    @Query(value = "SELECT * FROM CIUDAD WHERE NOMBRE = :nombre", nativeQuery = true)
    Ciudad darCiudadPorNombre(@Param("nombre") String nombre);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CIUDAD WHERE NOMBRE = :nombre", nativeQuery = true)
    int existeCiudadPorNombreInt(@Param("nombre") String nombre);
    
    default boolean existeCiudadPorNombre(String nombre) {
        return existeCiudadPorNombreInt(nombre) > 0;
    }
}

