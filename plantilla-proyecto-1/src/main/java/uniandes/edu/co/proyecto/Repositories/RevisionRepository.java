package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Revision;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    @Query(value = "SELECT * FROM REVISION", nativeQuery = true)
    Collection<Revision> darRevisiones();

    @Query(value = "SELECT * FROM REVISION WHERE IDREVISION = :id", nativeQuery = true)
    Revision darRevision(@Param("id") Long id);

    @Query(value = "SELECT * FROM REVISION WHERE IDSERVICIO = :idServicio", nativeQuery = true)
    Collection<Revision> darRevisionesPorServicio(@Param("idServicio") Long idServicio);

    @Query(value = "SELECT * FROM REVISION WHERE IDRECEPTORUSUARIO = :idConductor", nativeQuery = true)
    Collection<Revision> darRevisionesPorConductor(@Param("idConductor") Long idConductor);

    @Query(value = "SELECT * FROM REVISION WHERE IDREVISORUSUARIO = :idCliente", nativeQuery = true)
    Collection<Revision> darRevisionesPorCliente(@Param("idCliente") Long idCliente);

    @Query(value = """
        SELECT COUNT(*) 
        FROM REVISION 
        WHERE IDSERVICIO = :idServicio AND IDREVISORUSUARIO = :idRevisor
        """, nativeQuery = true)
    int verificarRevisionExistente(
        @Param("idServicio") Long idServicio,
        @Param("idRevisor") Long idRevisor
    );

    @Query(value = """
        SELECT AVG(CALIFICACION) 
        FROM REVISION 
        WHERE IDRECEPTORUSUARIO = :idReceptor
        """, nativeQuery = true)
    Double calcularPromedioCalificacionConductor(@Param("idReceptor") Long idReceptor);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO REVISION (IDSERVICIO, IDREVISORUSUARIO, IDRECEPTORUSUARIO, CALIFICACION, COMENTARIO) " +
                   "VALUES (:idServicio, :idRevisor, :idReceptor, :calificacion, :comentario)", nativeQuery = true)
    void insertarRevision(@Param("idServicio") Long idServicio,
                          @Param("idRevisor") Long idRevisor,
                          @Param("idReceptor") Long idReceptor,
                          @Param("calificacion") Integer calificacion,
                          @Param("comentario") String comentario);

    @Modifying
    @Transactional
    @Query(value = "UPDATE REVISION SET IDSERVICIO = :idServicio, IDREVISORUSUARIO = :idRevisor, IDRECEPTORUSUARIO = :idReceptor, " +
                   "CALIFICACION = :calificacion, COMENTARIO = :comentario WHERE IDREVISION = :id", nativeQuery = true)
    void actualizarRevision(@Param("id") Long id,
                            @Param("idServicio") Long idServicio,
                            @Param("idRevisor") Long idRevisor,
                            @Param("idReceptor") Long idReceptor,
                            @Param("calificacion") Integer calificacion,
                            @Param("comentario") String comentario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM REVISION WHERE IDREVISION = :id", nativeQuery = true)
    void eliminarRevision(@Param("id") Long id);
    
    // Métodos para el servicio transaccional
    @Query(value = "SELECT COUNT(*) > 0 FROM REVISION WHERE IDSERVICIO = :idServicio AND IDREVISORUSUARIO = :idCliente", nativeQuery = true)
    boolean existeRevisionCliente(@Param("idServicio") Long idServicio, @Param("idCliente") Long idCliente);
    
    @Query(value = "SELECT COUNT(*) > 0 FROM REVISION WHERE IDSERVICIO = :idServicio AND IDREVISORUSUARIO = :idConductor", nativeQuery = true)
    boolean existeRevisionConductor(@Param("idServicio") Long idServicio, @Param("idConductor") Long idConductor);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO REVISION (IDSERVICIO, IDREVISORUSUARIO, CALIFICACION, COMENTARIO, IDRECEPTORUSUARIO) " +
                   "VALUES (:idServicio, :idCliente, :calificacion, :comentario, " +
                   "(SELECT IDCONDUCTOR FROM SERVICIO WHERE IDSERVICIO = :idServicio))", nativeQuery = true)
    void insertarRevisionCliente(@Param("idServicio") Long idServicio, 
                                 @Param("idCliente") Long idCliente,
                                 @Param("calificacion") Integer calificacion, 
                                 @Param("comentario") String comentario);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO REVISION (IDSERVICIO, IDREVISORUSUARIO, CALIFICACION, COMENTARIO, IDRECEPTORUSUARIO) " +
                   "VALUES (:idServicio, :idConductor, :calificacion, :comentario, " +
                   "(SELECT IDCLIENTE FROM SERVICIO WHERE IDSERVICIO = :idServicio))", nativeQuery = true)
    void insertarRevisionConductor(@Param("idServicio") Long idServicio, 
                                   @Param("idConductor") Long idConductor,
                                   @Param("calificacion") Integer calificacion, 
                                   @Param("comentario") String comentario);
}

