package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "MAPA")
public class Mapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDMAPA")
    private Long idMapa;

    @ManyToOne
    @JoinColumn(name = "IDCIUDAD", nullable = false)
    private Ciudad ciudad;

    @ManyToOne
    @JoinColumn(name = "IDPUNTOPARTIDA", nullable = false)
    private Punto puntoPartida;

    @ManyToOne
    @JoinColumn(name = "IDPUNTOLLEGADA", nullable = false)
    private Punto puntoLlegada;

    @Column(name = "DIRECCIONAPROX", nullable = false)
    private String direccionAprox;

    public Mapa() {}

    public Mapa(Ciudad ciudad, Punto puntoPartida, Punto puntoLlegada, String direccionAprox) {
        this.ciudad = ciudad;
        this.puntoPartida = puntoPartida;
        this.puntoLlegada = puntoLlegada;
        this.direccionAprox = direccionAprox;
    }

    // Getters y setters
    public Long getIdMapa() { return idMapa; }
    public void setIdMapa(Long idMapa) { this.idMapa = idMapa; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    public Punto getPuntoPartida() { return puntoPartida; }
    public void setPuntoPartida(Punto puntoPartida) { this.puntoPartida = puntoPartida; }

    public Punto getPuntoLlegada() { return puntoLlegada; }
    public void setPuntoLlegada(Punto puntoLlegada) { this.puntoLlegada = puntoLlegada; }

    public String getDireccionAprox() { return direccionAprox; }
    public void setDireccionAprox(String direccionAprox) { this.direccionAprox = direccionAprox; }
}
