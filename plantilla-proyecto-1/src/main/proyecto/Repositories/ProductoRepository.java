package uniandes.edu.co.proyecto.Repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniandes.edu.co.proyecto.modelo.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "SELECT * FROM PRODUCTO", nativeQuery = true)
    Collection<Producto> darProductos();

    @Query(value = "SELECT * FROM PRODUCTO WHERE IDPRODUCTO = :id", nativeQuery = true)
    Producto darProducto(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PRODUCTO (IDRESTAURANTE, NOMBRE, PRECIO) VALUES (:idRestaurante, :nombre, :precio)", nativeQuery = true)
    void insertarProducto(@Param("idRestaurante") Long idRestaurante,
                          @Param("nombre") String nombre,
                          @Param("precio") Double precio);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PRODUCTO SET IDRESTAURANTE = :idRestaurante, NOMBRE = :nombre, PRECIO = :precio WHERE IDPRODUCTO = :id", nativeQuery = true)
    void actualizarProducto(@Param("id") Long id,
                            @Param("idRestaurante") Long idRestaurante,
                            @Param("nombre") String nombre,
                            @Param("precio") Double precio);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PRODUCTO WHERE IDPRODUCTO = :id", nativeQuery = true)
    void eliminarProducto(@Param("id") Long id);
}

