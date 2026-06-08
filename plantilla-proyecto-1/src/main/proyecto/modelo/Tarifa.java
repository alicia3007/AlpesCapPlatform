package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "TARIFA")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTARIFA")
    private Long idTarifa;

    @Column(name = "TIPOSERVICIO", nullable = false)
    private String tipoServicio;

    @Column(name = "NIVEL", nullable = false)
    private String nivel;

    @Column(name = "VALORKM", nullable = false)
    private Double valorKm;

    public Tarifa() {}

    public Tarifa(String tipoServicio, String nivel, Double valorKm) {
        this.tipoServicio = tipoServicio;
        this.nivel = nivel;
        this.valorKm = valorKm;
    }

    public Long getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Long idTarifa) { this.idTarifa = idTarifa; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Double getValorKm() { return valorKm; }
    public void setValorKm(Double valorKm) { this.valorKm = valorKm; }
}



