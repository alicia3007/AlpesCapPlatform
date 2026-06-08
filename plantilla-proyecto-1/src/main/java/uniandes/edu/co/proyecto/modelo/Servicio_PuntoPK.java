package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Servicio_PuntoPK implements Serializable {

    @Column(name = "idservicio")
    private Long idServicio;

    @Column(name = "rol")
    private String rol;

    @Column(name = "orden")
    private Integer orden;

    public Servicio_PuntoPK() {}

    public Servicio_PuntoPK(Long idServicio, String rol, Integer orden) {
        this.idServicio = idServicio;
        this.rol = rol;
        this.orden = orden;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio_PuntoPK)) return false;
        Servicio_PuntoPK that = (Servicio_PuntoPK) o;
        return Objects.equals(idServicio, that.idServicio) &&
               Objects.equals(rol, that.rol) &&
               Objects.equals(orden, that.orden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServicio, rol, orden);
    }
}
