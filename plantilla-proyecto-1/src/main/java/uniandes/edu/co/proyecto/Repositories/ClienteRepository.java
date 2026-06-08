package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT * FROM CLIENTE", nativeQuery = true)
    Collection<Cliente> darClientes();

    @Query(value = "SELECT * FROM CLIENTE WHERE IDUSUARIO = :id", nativeQuery = true)
    Cliente darCliente(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CLIENTE (IDUSUARIO) VALUES (:idUsuario)", nativeQuery = true)
    void insertarCliente(@Param("idUsuario") Long idUsuario);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CLIENTE SET IDUSUARIO = :idUsuario WHERE IDUSUARIO = :id", nativeQuery = true)
    void actualizarCliente(@Param("id") Long id, @Param("idUsuario") Long idUsuario);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CLIENTE WHERE IDUSUARIO = :id", nativeQuery = true)
    void eliminarCliente(@Param("id") Long id);
    
    // Métodos para el servicio transaccional
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CLIENTE c JOIN USUARIO u ON c.IDUSUARIO = u.IDUSUARIO WHERE u.CEDULA = :cedula", nativeQuery = true)
    int existeClientePorCedula(@Param("cedula") Long cedula);
    
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CLIENTE c JOIN USUARIO u ON c.IDUSUARIO = u.IDUSUARIO WHERE u.CORREO = :correo", nativeQuery = true)
    int existeClientePorCorreo(@Param("correo") String correo);
    
    @Query(value = "SELECT c.* FROM CLIENTE c JOIN USUARIO u ON c.IDUSUARIO = u.IDUSUARIO WHERE u.CEDULA = :cedula", nativeQuery = true)
    Cliente darClientePorCedula(@Param("cedula") Long cedula);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USUARIO (NOMBRE, CORREO, CELULAR, CEDULA) VALUES (:nombre, :correo, :celular, :cedula)", nativeQuery = true)
    void insertarUsuario(@Param("nombre") String nombre, @Param("correo") String correo, 
                        @Param("celular") Long celular, @Param("cedula") Long cedula);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CLIENTE (IDUSUARIO) SELECT IDUSUARIO FROM USUARIO WHERE CEDULA = :cedula", nativeQuery = true)
    void insertarClientePorCedula(@Param("cedula") Long cedula);
    
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM PAGO WHERE IDUSUARIO = :idCliente", nativeQuery = true)
    int tieneMedioPago(@Param("idCliente") Long idCliente);
    
    // Métodos para RF8
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM CLIENTE_TARJETA WHERE IDCLIENTE = :idCliente", nativeQuery = true)
    int verificarMedioPagoRegistrado(@Param("idCliente") Long idCliente);
    
    @Query(value = "SELECT IDTARJETA FROM CLIENTE_TARJETA WHERE IDCLIENTE = :idCliente AND PREDETERMINADA = 1 " +
                   "UNION ALL SELECT IDTARJETA FROM CLIENTE_TARJETA WHERE IDCLIENTE = :idCliente FETCH FIRST 1 ROW ONLY", 
                   nativeQuery = true)
    Long obtenerTarjetaCliente(@Param("idCliente") Long idCliente);
}
