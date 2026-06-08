package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "PUNTO")
public class Punto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPUNTO")
    private Long idPunto;

    @Column(name = "NOMBRE", unique = true)
    private String nombre;

    @Column(name = "DIRECCION", nullable = false, unique = true)
    private String direccion;

    @Column(name = "LATITUD", nullable = false)
    private Double latitud;

    @Column(name = "LONGITUD", nullable = false)
    private Double longitud;

    @ManyToOne
    @JoinColumn(name = "IDCIUDAD", nullable = false)
    private Ciudad ciudad;

    public Punto() {}

    public Punto(String nombre, String direccion, Double latitud, Double longitud, Ciudad ciudad) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad = ciudad;
    }

    // Getters y setters
    public Long getIdPunto() { return idPunto; }
    public void setIdPunto(Long idPunto) { this.idPunto = idPunto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }
}
