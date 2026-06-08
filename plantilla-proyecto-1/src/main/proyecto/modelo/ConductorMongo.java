package uniandes.edu.co.proyecto.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "conductores")
public class ConductorMongo {

    @Id
    private String id;

    private String tipo;
    private String nombre;
    private String correo;
    private String numero;
    private String cedula;

    private Long totalComisiones;
    private Double calificacionPromedio;
    private Integer totalRevisiones;
    private List<String> revisionesRecibidas = new ArrayList<>();
    private List<Vehiculo> vehiculos = new ArrayList<>();

    public ConductorMongo() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public Long getTotalComisiones() { return totalComisiones; }
    public void setTotalComisiones(Long totalComisiones) { this.totalComisiones = totalComisiones; }

    public Double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public Integer getTotalRevisiones() { return totalRevisiones; }
    public void setTotalRevisiones(Integer totalRevisiones) { this.totalRevisiones = totalRevisiones; }

    public List<String> getRevisionesRecibidas() { return revisionesRecibidas; }
    public void setRevisionesRecibidas(List<String> revisionesRecibidas) { this.revisionesRecibidas = revisionesRecibidas; }

    public List<Vehiculo> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<Vehiculo> vehiculos) { this.vehiculos = vehiculos; }

    public static class Vehiculo {
        private String placa;
        private String marca;
        private Integer modelo;
        private Integer capacidad;

        public Vehiculo() {}

        public String getPlaca() { return placa; }
        public void setPlaca(String placa) { this.placa = placa; }

        public String getMarca() { return marca; }
        public void setMarca(String marca) { this.marca = marca; }

        public Integer getModelo() { return modelo; }
        public void setModelo(Integer modelo) { this.modelo = modelo; }

        public Integer getCapacidad() { return capacidad; }
        public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    }
}
