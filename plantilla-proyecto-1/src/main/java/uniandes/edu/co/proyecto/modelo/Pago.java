package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "PAGO")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPAGO")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", referencedColumnName = "IDSERVICIO", nullable = false)
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "IDTARJETA", referencedColumnName = "IDTARJETA", nullable = false)
    private TarjetaCredito tarjeta;

    @Column(name = "MONTO", nullable = false)
    private Double monto;

    @Column(name = "FECHA_PAGO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    public Pago() {}

    public Pago(Servicio servicio, TarjetaCredito tarjeta, Double monto, Date fechaPago) {
        this.servicio = servicio;
        this.tarjeta = tarjeta;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public TarjetaCredito getTarjeta() { return tarjeta; }
    public void setTarjeta(TarjetaCredito tarjeta) { this.tarjeta = tarjeta; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public Date getFechaPago() { return fechaPago; }
    public void setFechaPago(Date fechaPago) { this.fechaPago = fechaPago; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pago)) return false;
        Pago pago = (Pago) o;
        return Objects.equals(idPago, pago.idPago);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPago);
    }
}
