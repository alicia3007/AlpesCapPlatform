package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "CLIENTE")
public class Cliente {

    @Id
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @OneToOne
    @MapsId
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    public Cliente() {}

    public Cliente(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

