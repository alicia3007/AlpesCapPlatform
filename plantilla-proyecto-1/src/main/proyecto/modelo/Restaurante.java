package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "RESTAURANTE")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRESTAURANTE")
    private Long idRestaurante;

    @Column(name = "NOMBRE", nullable = false, unique = true)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "IDPUNTO", nullable = false)
    private Punto punto;

    public Restaurante() {}

    public Restaurante(String nombre, Punto punto) {
        this.nombre = nombre;
        this.punto = punto;
    }

    // Getters y setters
    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Punto getPunto() { return punto; }
    public void setPunto(Punto punto) { this.punto = punto; }
}
