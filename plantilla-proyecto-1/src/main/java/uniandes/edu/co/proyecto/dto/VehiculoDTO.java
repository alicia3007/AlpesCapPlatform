package uniandes.edu.co.proyecto.dto;

public class VehiculoDTO {
    private String tipo;
    private String marca;
    private String modelo;
    private String color;
    private String placa;
    private String ciudadPlaca;
    private Integer capacidad;
    private String nivel;
    private Long idConductor;

    public VehiculoDTO() {
    }

    public VehiculoDTO(String tipo, String marca, String modelo, String color, 
                      String placa, String ciudadPlaca, Integer capacidad, 
                      String nivel, Long idConductor) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.ciudadPlaca = ciudadPlaca;
        this.capacidad = capacidad;
        this.nivel = nivel;
        this.idConductor = idConductor;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCiudadPlaca() {
        return ciudadPlaca;
    }

    public void setCiudadPlaca(String ciudadPlaca) {
        this.ciudadPlaca = ciudadPlaca;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Long getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Long idConductor) {
        this.idConductor = idConductor;
    }
}