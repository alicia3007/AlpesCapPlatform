package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio_punto")
public class Servicio_Punto {

    @EmbeddedId
    private Servicio_PuntoPK id;

    @ManyToOne
    @MapsId("idServicio")
    @JoinColumn(name = "idservicio", referencedColumnName = "idservicio", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "idpunto", nullable = false)
    private Punto punto;

    public Servicio_Punto() {}

    public Servicio_Punto(Servicio servicio, String rol, Integer orden, Punto punto) {
        this.id = new Servicio_PuntoPK(servicio.getIdServicio(), rol, orden);
        this.servicio = servicio;
        this.punto = punto;
    }

    public Servicio_PuntoPK getId() {
        return id;
    }

    public void setId(Servicio_PuntoPK id) {
        this.id = id;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Punto getPunto() {
        return punto;
    }

    public void setPunto(Punto punto) {
        this.punto = punto;
    }
}


