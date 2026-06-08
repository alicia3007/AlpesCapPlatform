package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "servicios")
public class ServicioMongo {

    @Id
    private String id;

    private String clienteId;
    private String conductorId;

    private String origenAddress;
    private Double origenLat;
    private Double origenLng;

    private String destinoAddress;
    private Double destinoLat;
    private Double destinoLng;

    private Double tarifa;
    private String tipoServicio;
    private String vehiculoPlaca;
    private Integer cantidadPasajeros;
    private String estado; // SOLICITADO, ASIGNADO, EN_CURSO, COMPLETADO
    private Date fechaSolicitud;
    private Date fechaInicio;
    private Date fechaFin;

    private Boolean pagoRealizado;

    public ServicioMongo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getConductorId() { return conductorId; }
    public void setConductorId(String conductorId) { this.conductorId = conductorId; }

    public String getOrigenAddress() { return origenAddress; }
    public void setOrigenAddress(String origenAddress) { this.origenAddress = origenAddress; }

    public Double getOrigenLat() { return origenLat; }
    public void setOrigenLat(Double origenLat) { this.origenLat = origenLat; }

    public Double getOrigenLng() { return origenLng; }
    public void setOrigenLng(Double origenLng) { this.origenLng = origenLng; }

    public String getDestinoAddress() { return destinoAddress; }
    public void setDestinoAddress(String destinoAddress) { this.destinoAddress = destinoAddress; }

    public Double getDestinoLat() { return destinoLat; }
    public void setDestinoLat(Double destinoLat) { this.destinoLat = destinoLat; }

    public Double getDestinoLng() { return destinoLng; }
    public void setDestinoLng(Double destinoLng) { this.destinoLng = destinoLng; }

    public Double getTarifa() { return tarifa; }
    public void setTarifa(Double tarifa) { this.tarifa = tarifa; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }

    public Integer getCantidadPasajeros() { return cantidadPasajeros; }
    public void setCantidadPasajeros(Integer cantidadPasajeros) { this.cantidadPasajeros = cantidadPasajeros; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getPagoRealizado() { return pagoRealizado; }
    public void setPagoRealizado(Boolean pagoRealizado) { this.pagoRealizado = pagoRealizado; }
}
