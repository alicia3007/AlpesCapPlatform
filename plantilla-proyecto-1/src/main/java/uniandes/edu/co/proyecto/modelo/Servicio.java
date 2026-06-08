package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "SERVICIO")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDSERVICIO")
    private Long idServicio;

    @Column(name = "TIPO_SERVICIO", nullable = false)
    private String tipoServicio;

    @ManyToOne
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IDCONDUCTOR", nullable = false)
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "IDTARIFA", nullable = false)
    private Tarifa tarifa;

    @Column(name = "DURACION_MINUTOS", nullable = false)
    private Integer duracionMinutos;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "HORA_INICIO", nullable = false)
    private Date horaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "HORA_FIN", nullable = false)
    private Date horaFin;

    @Column(name = "NIVEL_APLICADO")
    private String nivelAplicado;

    @Column(name = "TARIFA_KM_APLICADA", nullable = false)
    private Double tarifaKmAplicada;

    @Column(name = "PORCENTAJE_COMISION", nullable = false)
    private Double porcentajeComision;

    // campos virtuales no se mapean (costo_total, comision_valor)

    public Servicio() {}

    public Servicio(String tipoServicio, Cliente cliente, Conductor conductor, Vehiculo vehiculo,
                    Tarifa tarifa, Integer duracionMinutos, Date horaInicio, Date horaFin,
                    String nivelAplicado, Double tarifaKmAplicada, Double porcentajeComision) {
        this.tipoServicio = tipoServicio;
        this.cliente = cliente;
        this.conductor = conductor;
        this.vehiculo = vehiculo;
        this.tarifa = tarifa;
        this.duracionMinutos = duracionMinutos;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.nivelAplicado = nivelAplicado;
        this.tarifaKmAplicada = tarifaKmAplicada;
        this.porcentajeComision = porcentajeComision;
    }

    // Getters y setters
    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Tarifa getTarifa() { return tarifa; }
    public void setTarifa(Tarifa tarifa) { this.tarifa = tarifa; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public Date getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Date horaInicio) { this.horaInicio = horaInicio; }

    public Date getHoraFin() { return horaFin; }
    public void setHoraFin(Date horaFin) { this.horaFin = horaFin; }

    public String getNivelAplicado() { return nivelAplicado; }
    public void setNivelAplicado(String nivelAplicado) { this.nivelAplicado = nivelAplicado; }

    public Double getTarifaKmAplicada() { return tarifaKmAplicada; }
    public void setTarifaKmAplicada(Double tarifaKmAplicada) { this.tarifaKmAplicada = tarifaKmAplicada; }

    public Double getPorcentajeComision() { return porcentajeComision; }
    public void setPorcentajeComision(Double porcentajeComision) { this.porcentajeComision = porcentajeComision; }
}
