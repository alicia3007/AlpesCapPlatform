package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRODUCTO")
    private Long idProducto;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "COSTO", nullable = false)
    private Double costo;

    public Producto() {}

    public Producto(String nombre, Double costo) {
        this.nombre = nombre;
        this.costo = costo;
    }

    // Getters y setters
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }
}
