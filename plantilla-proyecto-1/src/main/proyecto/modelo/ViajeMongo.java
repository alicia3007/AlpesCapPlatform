package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "viajes")
public class ViajeMongo {

    @Id
    private String id;

    private String pasajeroId;
    private String conductorId;
    private String servicioId;

    private String tipoServicio;
    private Vehiculo vehiculo;

    private Date horaInicio;
    private Date horaFin;

    private Double distanciaKm;
    private Double costoTotal;

    public ViajeMongo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPasajeroId() { return pasajeroId; }
    public void setPasajeroId(String pasajeroId) { this.pasajeroId = pasajeroId; }

    public String getConductorId() { return conductorId; }
    public void setConductorId(String conductorId) { this.conductorId = conductorId; }

    public String getServicioId() { return servicioId; }
    public void setServicioId(String servicioId) { this.servicioId = servicioId; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Date getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Date horaInicio) { this.horaInicio = horaInicio; }

    public Date getHoraFin() { return horaFin; }
    public void setHoraFin(Date horaFin) { this.horaFin = horaFin; }

    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }

    public Double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Double costoTotal) { this.costoTotal = costoTotal; }

    public static class Vehiculo {
        private String placa;
        private String modelo;
        private String nivel;

        public Vehiculo() {}

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }
    }
}
