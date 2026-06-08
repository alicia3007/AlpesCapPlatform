package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ACTIVIDAD")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDACTIVIDAD")
    private Long idActividad;

    @Column(name = "DIASEMANA", nullable = false)
    private Integer diaSemana;

    @Column(name = "FRANJASHORARIAS")
    private String franjasHorarias;

    @Column(name = "TIPOSERVICIO", nullable = false)
    private String tipoServicio;

    @Column(name = "HORAINICIO", nullable = false)
    private Timestamp horaInicio;

    @Column(name = "HORAFIN", nullable = false)
    private Timestamp horaFin;

    @ManyToOne
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "IDCONDUCTOR", nullable = false)
    private Conductor conductor;

    public Actividad() {}

    public Actividad(Integer diaSemana, String franjasHorarias, String tipoServicio,
                     Timestamp horaInicio, Timestamp horaFin,
                     Vehiculo vehiculo, Conductor conductor) {
        this.diaSemana = diaSemana;
        this.franjasHorarias = franjasHorarias;
        this.tipoServicio = tipoServicio;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.vehiculo = vehiculo;
        this.conductor = conductor;
    }

    // Getters y setters
    public Long getIdActividad() { return idActividad; }
    public void setIdActividad(Long idActividad) { this.idActividad = idActividad; }

    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

    public String getFranjasHorarias() { return franjasHorarias; }
    public void setFranjasHorarias(String franjasHorarias) { this.franjasHorarias = franjasHorarias; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public Timestamp getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Timestamp horaInicio) { this.horaInicio = horaInicio; }

    public Timestamp getHoraFin() { return horaFin; }
    public void setHoraFin(Timestamp horaFin) { this.horaFin = horaFin; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }
}
