package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "CORREO", nullable = false, unique = true)
    private String correo;

    @Column(name = "CELULAR")
    private Long celular;

    @Column(name = "CEDULA", unique = true)
    private Long cedula;

    public Usuario() {}

    public Usuario(String nombre, String correo, Long celular, Long cedula) {
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.cedula = cedula;
    }

    // Getters y setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Long getCelular() { return celular; }
    public void setCelular(Long celular) { this.celular = celular; }

    public Long getCedula() { return cedula; }
    public void setCedula(Long cedula) { this.cedula = cedula; }
}
