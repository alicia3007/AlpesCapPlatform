package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "CONDUCTOR")
public class Conductor {

    @Id
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @OneToOne
    @MapsId
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    @Column(name = "DISPONIBLE")
    private Integer disponible; // 1 = disponible, 0 = no disponible

    public Conductor() {}

    public Conductor(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getDisponible() { return disponible; }
    public void setDisponible(Integer disponible) { this.disponible = disponible; }
}

