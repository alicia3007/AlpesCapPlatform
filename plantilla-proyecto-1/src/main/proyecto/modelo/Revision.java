package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "REVISION")
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDREVISION")
    private Long idRevision;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "IDREVISORUSUARIO", nullable = false)
    private Usuario revisorUsuario;

    @ManyToOne
    @JoinColumn(name = "IDRECEPTORUSUARIO", nullable = false)
    private Usuario receptorUsuario;

    @Column(name = "CALIFICACION", nullable = false)
    private Integer calificacion;

    @Column(name = "COMENTARIO")
    private String comentario;

    // Constructors
    public Revision() {}

    public Revision(Servicio servicio, Usuario revisorUsuario, Usuario receptorUsuario, Integer calificacion, String comentario) {
        this.servicio = servicio;
        this.revisorUsuario = revisorUsuario;
        this.receptorUsuario = receptorUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    // Getters y setters
    public Long getIdRevision() { return idRevision; }
    public void setIdRevision(Long idRevision) { this.idRevision = idRevision; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Usuario getRevisorUsuario() { return revisorUsuario; }
    public void setRevisorUsuario(Usuario revisorUsuario) { this.revisorUsuario = revisorUsuario; }

    public Usuario getReceptorUsuario() { return receptorUsuario; }
    public void setReceptorUsuario(Usuario receptorUsuario) { this.receptorUsuario = receptorUsuario; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

}
