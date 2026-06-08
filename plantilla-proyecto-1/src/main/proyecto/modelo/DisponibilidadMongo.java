package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "disponibilidades")
public class DisponibilidadMongo {

    @Id
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String conductorId;
    private String diaSemana; // e.g., Lunes
    private String horaInicio; // HH:mm
    private String horaFin; // HH:mm
    private String tipoServicio; // e.g., "Pasajeros - Estándar"
    private String vehiculoPlaca;
    private Vehiculo vehiculo;

    public DisponibilidadMongo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getConductorId() { return conductorId; }
    public void setConductorId(String conductorId) { this.conductorId = conductorId; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String vehiculoPlaca) { this.vehiculoPlaca = vehiculoPlaca; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public static class Vehiculo {
        private String placa;
        private String modelo; // display string e.g., "Chevrolet Spark 2020"
        private Integer capacidad;
        private String nivel; // Estándar/Confort/Large

        public Vehiculo() {}

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public Integer getCapacidad() { return capacidad; }
        public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

        public String getNivel() { return nivel; }
        public void setNivel(String nivel) { this.nivel = nivel; }
    }
}
