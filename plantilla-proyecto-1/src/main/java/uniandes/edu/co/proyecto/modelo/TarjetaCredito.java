package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "TARJETA_CREDITO")
public class TarjetaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDTARJETA")
    private Long idTarjeta;

    @Column(name = "NUMERO", nullable = false, unique = true)
    private Long numero;

    @Column(name = "NOMBRE_TARJETA")
    private String nombreTarjeta;

    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private java.sql.Date fechaVencimiento;

    @Column(name = "CODIGO", nullable = false)
    private String codigo;

    public TarjetaCredito() {}

    public TarjetaCredito(Long numero, String nombreTarjeta, java.sql.Date fechaVencimiento, String codigo) {
        this.numero = numero;
        this.nombreTarjeta = nombreTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.codigo = codigo;
    }

    // Getters y setters
    public Long getIdTarjeta() { return idTarjeta; }
    public void setIdTarjeta(Long idTarjeta) { this.idTarjeta = idTarjeta; }

    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }

    public String getNombreTarjeta() { return nombreTarjeta; }
    public void setNombreTarjeta(String nombreTarjeta) { this.nombreTarjeta = nombreTarjeta; }

    public java.sql.Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(java.sql.Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
}
