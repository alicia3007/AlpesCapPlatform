package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "HISTORIAL")
public class Historial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDHISTORIAL")
    private Long idHistorial;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IDCONDUCTOR", nullable = false)
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "IDPUNTOPARTIDA", nullable = false)
    private Punto puntoPartida;

    @ManyToOne
    @JoinColumn(name = "IDPUNTOLLEGADA", nullable = false)
    private Punto puntoLlegada;

    @Column(name = "HORA_FIN")
    private java.util.Date horaFin;

    @Column(name = "DISTANCIA_KM")
    private Double distanciaKm;

    public Historial() {}

    public Historial(Servicio servicio, Cliente cliente, Conductor conductor, Punto puntoPartida, Punto puntoLlegada) {
        this.servicio = servicio;
        this.cliente = cliente;
        this.conductor = conductor;
        this.puntoPartida = puntoPartida;
        this.puntoLlegada = puntoLlegada;
    }

    // Getters y setters
    public Long getIdHistorial() { return idHistorial; }
    public void setIdHistorial(Long idHistorial) { this.idHistorial = idHistorial; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }

    public Punto getPuntoPartida() { return puntoPartida; }
    public void setPuntoPartida(Punto puntoPartida) { this.puntoPartida = puntoPartida; }

    public Punto getPuntoLlegada() { return puntoLlegada; }
    public void setPuntoLlegada(Punto puntoLlegada) { this.puntoLlegada = puntoLlegada; }

    public java.util.Date getHoraFin() { return horaFin; }
    public void setHoraFin(java.util.Date horaFin) { this.horaFin = horaFin; }

    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
}
