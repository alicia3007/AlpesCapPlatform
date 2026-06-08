package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DISPONIBILIDAD")
public class Disponibilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IDCONDUCTOR")
    private Long idConductor;

    @Column(name = "IDVEHICULO")
    private Long idVehiculo;

    @Column(name = "TIPOSERVICIO")
    private String tipoServicio;

    @Column(name = "DIAINICIO")
    private String diaInicio;

    @Column(name = "DIAFIN")
    private String diaFin;

    @Column(name = "HORAINICIO")
    private String horaInicio;

    @Column(name = "HORAFIN")
    private String horaFin;

    public Disponibilidad() {
    }

    public Disponibilidad(Long idConductor, Long idVehiculo, String tipoServicio,
                         String diaInicio, String diaFin, String horaInicio, String horaFin) {
        this.idConductor = idConductor;
        this.idVehiculo = idVehiculo;
        this.tipoServicio = tipoServicio;
        this.diaInicio = diaInicio;
        this.diaFin = diaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Long idConductor) {
        this.idConductor = idConductor;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getDiaFin() {
        return diaFin;
    }

    public void setDiaFin(String diaFin) {
        this.diaFin = diaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}