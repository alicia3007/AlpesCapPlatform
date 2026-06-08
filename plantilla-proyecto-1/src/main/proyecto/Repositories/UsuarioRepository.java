package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM usuario", nativeQuery = true)
    Collection<Usuario> darUsuarios();

    @Query(value = "SELECT * FROM usuario WHERE id= :id", nativeQuery = true)
    Usuario darUsuario(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usuario (nombre, correo, celular, cedula) VALUES (:nombre, :correo, :celular, :cedula)", nativeQuery = true)
    void insertarUsuario(@Param("nombre") String nombre, @Param("correo") String correo, @Param("celular") Long celular, @Param("cedula") Long cedula);

    @Modifying
    @Transactional
    @Query(value = "UPDATE usuario SET nombre = :nombre, correo = :correo, celular = :celular, cedula = :cedula WHERE id = :id", nativeQuery = true)
    void actualizarUsuario(@Param("id") Long id, @Param("nombre") String nombre, @Param("correo") String correo, @Param("celular") Long celular, @Param("cedula") Long cedula);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usuario WHERE id = :id", nativeQuery = true)
    void eliminarUsuario(@Param("id") Long id);
}
