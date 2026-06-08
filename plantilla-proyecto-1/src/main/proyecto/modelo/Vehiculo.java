package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "VEHICULO")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDVEHICULO")
    private Long idVehiculo;

    @Column(name = "TIPO", nullable = false)
    private String tipo;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "MODELO")
    private String modelo;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "PLACA", nullable = false, unique = true)
    private String placa;

    @Column(name = "CIUDADPLACA")
    private String ciudadPlaca;

    @Column(name = "CAPACIDAD")
    private Integer capacidad;

    @Column(name = "NIVEL")
    private String nivel;

    @ManyToOne
    @JoinColumn(name = "IDCONDUCTOR", nullable = false)
    private Conductor conductor;

    public Vehiculo() {}

    public Vehiculo(String tipo, String marca, String modelo, String color,
                    String placa, String ciudadPlaca, Integer capacidad, String nivel,
                    Conductor conductor) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.ciudadPlaca = ciudadPlaca;
        this.capacidad = capacidad;
        this.nivel = nivel;
        this.conductor = conductor;
    }

    // Getters y setters
    public Long getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(Long idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getCiudadPlaca() { return ciudadPlaca; }
    public void setCiudadPlaca(String ciudadPlaca) { this.ciudadPlaca = ciudadPlaca; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Conductor getConductor() { return conductor; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }
}
