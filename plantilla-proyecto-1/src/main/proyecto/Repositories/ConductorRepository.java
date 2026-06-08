package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Conductor;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    @Query(value = "SELECT * FROM CONDUCTOR", nativeQuery = true)
    Collection<Conductor> darConductores();

    @Query(value = "SELECT * FROM CONDUCTOR WHERE IDUSUARIO = :id", nativeQuery = true)
    Conductor darConductor(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CONDUCTOR (IDUSUARIO) VALUES (:idUsuario)", nativeQuery = true)
    void insertarConductor(@Param("idUsuario") Long idUsuario);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CONDUCTOR SET IDUSUARIO = :idUsuario WHERE IDUSUARIO = :id", nativeQuery = true)
    void actualizarConductor(@Param("id") Long id, @Param("idUsuario") Long idUsuario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CONDUCTOR WHERE IDUSUARIO = :id", nativeQuery = true)
    void eliminarConductor(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CONDUCTOR SET DISPONIBLE = :disponible WHERE IDUSUARIO = :id", nativeQuery = true)
    void marcarConductorDisponible(@Param("id") Long id, @Param("disponible") Integer disponible);
    
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CONDUCTOR WHERE IDUSUARIO = :id", nativeQuery = true)
    int existeConductor(@Param("id") Long id);

    @Query(value = "SELECT * FROM CONDUCTOR WHERE TIPODOCUMENTO = :tipoDocumento AND NUMERODOCUMENTO = :numeroDocumento", nativeQuery = true)
    Conductor darConductorPorDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CONDUCTOR WHERE TIPODOCUMENTO = :tipoDocumento AND NUMERODOCUMENTO = :numeroDocumento", nativeQuery = true)
    int existeConductorPorDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO CONDUCTOR (NOMBRE, TIPODOCUMENTO, NUMERODOCUMENTO, EMAIL, TELEFONO) VALUES (:nombre, :tipoDocumento, :numeroDocumento, :email, :telefono)", nativeQuery = true)
        void insertarConductor(@Param("nombre") String nombre, @Param("tipoDocumento") String tipoDocumento,
                              @Param("numeroDocumento") String numeroDocumento, @Param("email") String email,
                              @Param("telefono") String telefono);

        @Modifying
        @Transactional
        @Query(value = "UPDATE CONDUCTOR SET DISPONIBLE = 0 WHERE IDCONDUCTOR = :id", nativeQuery = true)
        void marcarNoDisponible(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CONDUCTOR SET DISPONIBLE = 1 WHERE IDCONDUCTOR = :id", nativeQuery = true)
    void marcarDisponible(@Param("id") Long id);
    
    // Métodos para servicio transaccional con estructura real de BD
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CONDUCTOR c JOIN USUARIO u ON c.IDUSUARIO = u.IDUSUARIO WHERE u.CEDULA = :cedula", nativeQuery = true)
    int existeConductorPorCedula(@Param("cedula") Long cedula);
    
    @Query(value = "SELECT c.* FROM CONDUCTOR c JOIN USUARIO u ON c.IDUSUARIO = u.IDUSUARIO WHERE u.CEDULA = :cedula", nativeQuery = true)
    Conductor darConductorPorCedula(@Param("cedula") Long cedula);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USUARIO (NOMBRE, CORREO, CELULAR, CEDULA) VALUES (:nombre, :correo, :celular, :cedula)", nativeQuery = true)
    void insertarUsuarioConductor(@Param("nombre") String nombre, @Param("correo") String correo, 
                                  @Param("celular") Long celular, @Param("cedula") Long cedula);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CONDUCTOR (IDUSUARIO) SELECT IDUSUARIO FROM USUARIO WHERE CEDULA = :cedula", nativeQuery = true)
    void insertarConductorPorCedula(@Param("cedula") Long cedula);
    
    // Métodos para RF8
    @Query(value = "SELECT c.IDUSUARIO FROM CONDUCTOR c " +
                   "JOIN VEHICULO v ON c.IDUSUARIO = v.IDCONDUCTOR " +
                   "WHERE c.DISPONIBLE = 1 " +
                   "AND v.NIVEL = :nivel " +
                   "AND NOT EXISTS (SELECT 1 FROM SERVICIO s WHERE s.IDCONDUCTOR = c.IDUSUARIO AND s.HORA_FIN IS NULL) " +
                   "FETCH FIRST 1 ROW ONLY", 
           nativeQuery = true)
    Long buscarConductorDisponiblePorNivel(@Param("nivel") String nivel);
    
    @Query(value = "SELECT c.IDUSUARIO FROM CONDUCTOR c " +
                   "WHERE c.DISPONIBLE = 1 " +
                   "AND NOT EXISTS (SELECT 1 FROM SERVICIO s WHERE s.IDCONDUCTOR = c.IDUSUARIO AND s.HORA_FIN IS NULL) " +
                   "FETCH FIRST 1 ROW ONLY", 
           nativeQuery = true)
    Long buscarConductorDisponibleGeneral();
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE CONDUCTOR SET DISPONIBLE = 0 WHERE IDUSUARIO = :idConductor", nativeQuery = true)
    void marcarConductorNoDisponible(@Param("idConductor") Long idConductor);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE CONDUCTOR SET DISPONIBLE = 1 WHERE IDUSUARIO = :idConductor", nativeQuery = true)
    void marcarConductorComoDisponible(@Param("idConductor") Long idConductor);
    
    @Query(value = "SELECT v.IDVEHICULO FROM VEHICULO v WHERE v.IDCONDUCTOR = :idConductor FETCH FIRST 1 ROW ONLY", 
           nativeQuery = true)
    Long obtenerVehiculoConductor(@Param("idConductor") Long idConductor);
}


