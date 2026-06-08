package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_domicilio")
public class Servicio_Domicilio {

    @Id
    @Column(name = "idservicio")
    private Long idServicio;

    @OneToOne
    @MapsId   // indica que la PK es también la FK a Servicio
    @JoinColumn(name = "idservicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "idrestaurante", referencedColumnName = "idrestaurante")
    private Restaurante restaurante;

    @Column(name = "valor_orden")
    private Double valorOrden;

    @Column(name = "cantidad_productos")
    private Integer cantidadProductos;

    public Servicio_Domicilio() {}

    public Servicio_Domicilio(Long idServicio, Servicio servicio, Restaurante restaurante, Double valorOrden, Integer cantidadProductos) {
        this.idServicio = idServicio;
        this.servicio = servicio;
        this.restaurante = restaurante;
        this.valorOrden = valorOrden;
        this.cantidadProductos = cantidadProductos;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Double getValorOrden() {
        return valorOrden;
    }

    public void setValorOrden(Double valorOrden) {
        this.valorOrden = valorOrden;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio_Domicilio)) return false;
        Servicio_Domicilio that = (Servicio_Domicilio) o;
        return idServicio != null && idServicio.equals(that.idServicio);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
