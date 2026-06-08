package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Vehiculo;
import uniandes.edu.co.proyecto.dto.VehiculoDTO;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    @Query(value = "SELECT * FROM VEHICULO", nativeQuery = true)
    Collection<Vehiculo> darVehiculos();

    @Query(value = "SELECT * FROM VEHICULO WHERE IDVEHICULO = :id", nativeQuery = true)
    Vehiculo darVehiculo(@Param("id") Long id);

    @Query(value = "SELECT * FROM VEHICULO WHERE PLACA = :placa", nativeQuery = true)
    Vehiculo darVehiculoPorPlaca(@Param("placa") String placa);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM VEHICULO WHERE PLACA = :placa", nativeQuery = true)
    int existeVehiculoPorPlaca(@Param("placa") String placa);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO VEHICULO (TIPO, MARCA, MODELO, COLOR, PLACA, CIUDADPLACA, CAPACIDAD, NIVEL, IDCONDUCTOR) " +
           "VALUES (:#{#vehiculo.tipo}, :#{#vehiculo.marca}, :#{#vehiculo.modelo}, :#{#vehiculo.color}, " +
           ":#{#vehiculo.placa}, :#{#vehiculo.ciudadPlaca}, :#{#vehiculo.capacidad}, :#{#vehiculo.nivel}, " +
           ":#{#vehiculo.idConductor})", nativeQuery = true)
    void insertarVehiculo(@Param("vehiculo") VehiculoDTO vehiculo);

    @Modifying
    @Transactional
    @Query(value = "UPDATE VEHICULO SET TIPO = :#{#vehiculo.tipo}, MARCA = :#{#vehiculo.marca}, " +
           "MODELO = :#{#vehiculo.modelo}, COLOR = :#{#vehiculo.color}, PLACA = :#{#vehiculo.placa}, " +
           "CIUDADPLACA = :#{#vehiculo.ciudadPlaca}, CAPACIDAD = :#{#vehiculo.capacidad}, " +
           "NIVEL = :#{#vehiculo.nivel}, IDCONDUCTOR = :#{#vehiculo.idConductor} " +
           "WHERE IDVEHICULO = :id", nativeQuery = true)
    void actualizarVehiculo(@Param("id") Long id, @Param("vehiculo") VehiculoDTO vehiculo);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VEHICULO WHERE IDVEHICULO = :id", nativeQuery = true)
    void eliminarVehiculo(@Param("id") Long id);
}
