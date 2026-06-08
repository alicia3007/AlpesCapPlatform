package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "domicilios")
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer valor;
    private Integer cantidadProductos;

    public Domicilio() {;}

    public Domicilio(Integer valor, Integer cantidadProductos) {
        this.valor = valor;
        this.cantidadProductos = cantidadProductos;
    }

    public Long getId() { return id; }
    public Integer getValor() { return valor; }
    public Integer getCantidadProductos() { return cantidadProductos; }

    public void setId(Long id) { this.id = id; }
    public void setValor(Integer valor) { this.valor = valor; }
    public void setCantidadProductos(Integer cantidadProductos) { this.cantidadProductos = cantidadProductos; }
}

