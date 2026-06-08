package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "CIUDAD")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCIUDAD")
    private Long idCiudad;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    public Ciudad() {}

    public Ciudad(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdCiudad() { return idCiudad; }
    public void setIdCiudad(Long idCiudad) { this.idCiudad = idCiudad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

