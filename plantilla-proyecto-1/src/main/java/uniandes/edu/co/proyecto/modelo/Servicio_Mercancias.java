package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_mercancias")
public class Servicio_Mercancias {

    @Id
    @Column(name = "idservicio")
    private Long idServicio;

    @OneToOne
    @MapsId   // PK = FK hacia Servicio
    @JoinColumn(name = "idservicio")
    private Servicio servicio;

    @Column(name = "cantidad_objetos")
    private Integer cantidadObjetos;

    public Servicio_Mercancias() {}

    public Servicio_Mercancias(Long idServicio, Servicio servicio, Integer cantidadObjetos) {
        this.idServicio = idServicio;
        this.servicio = servicio;
        this.cantidadObjetos = cantidadObjetos;
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

    public Integer getCantidadObjetos() {
        return cantidadObjetos;
    }

    public void setCantidadObjetos(Integer cantidadObjetos) {
        this.cantidadObjetos = cantidadObjetos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio_Mercancias)) return false;
        Servicio_Mercancias that = (Servicio_Mercancias) o;
        return idServicio != null && idServicio.equals(that.idServicio);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
