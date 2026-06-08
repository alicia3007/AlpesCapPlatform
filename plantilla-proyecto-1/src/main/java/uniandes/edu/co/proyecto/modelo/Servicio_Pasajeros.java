package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_pasajeros")
public class Servicio_Pasajeros {

    @Id
    @Column(name = "idservicio")
    private Long idServicio;

    @OneToOne
    @MapsId   // PK = FK hacia Servicio
    @JoinColumn(name = "idservicio")
    private Servicio servicio;

    @Column(name = "cantidad_pasajeros")
    private Integer cantidadPasajeros;

    public Servicio_Pasajeros() {}

    public Servicio_Pasajeros(Long idServicio, Servicio servicio, Integer cantidadPasajeros) {
        this.idServicio = idServicio;
        this.servicio = servicio;
        this.cantidadPasajeros = cantidadPasajeros;
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

    public Integer getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public void setCantidadPasajeros(Integer cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio_Pasajeros)) return false;
        Servicio_Pasajeros that = (Servicio_Pasajeros) o;
        return idServicio != null && idServicio.equals(that.idServicio);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
