package uniandes.edu.co.proyecto.dto;

/**
 * DTO para solicitar un servicio por parte de un usuario (RF8)
 */
public class SolicitudServicioDTO {
    
    private Long idCliente;
    private Long idPuntoPartida;
    private Long idPuntoLlegada;
    private String tipoServicio; // PASAJEROS, DOMICILIO, MERCANCIAS
    private String nivelServicio; // ESTANDAR, CONFORT, LARGE (opcional, se usa para calcular tarifa)
    
    // Campos específicos para tipo de servicio
    private Integer cantidadPasajeros; // Para PASAJEROS
    private Long idRestaurante; // Para DOMICILIO
    private Double valorOrden; // Para DOMICILIO
    private Integer cantidadProductos; // Para DOMICILIO
    private Integer cantidadObjetos; // Para MERCANCIAS
    
    public SolicitudServicioDTO() {}
    
    public SolicitudServicioDTO(Long idCliente, Long idPuntoPartida, Long idPuntoLlegada, 
                                String tipoServicio, String nivelServicio) {
        this.idCliente = idCliente;
        this.idPuntoPartida = idPuntoPartida;
        this.idPuntoLlegada = idPuntoLlegada;
        this.tipoServicio = tipoServicio;
        this.nivelServicio = nivelServicio;
    }
    
    // Getters y Setters
    public Long getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    
    public Long getIdPuntoPartida() {
        return idPuntoPartida;
    }
    
    public void setIdPuntoPartida(Long idPuntoPartida) {
        this.idPuntoPartida = idPuntoPartida;
    }
    
    public Long getIdPuntoLlegada() {
        return idPuntoLlegada;
    }
    
    public void setIdPuntoLlegada(Long idPuntoLlegada) {
        this.idPuntoLlegada = idPuntoLlegada;
    }
    
    public String getTipoServicio() {
        return tipoServicio;
    }
    
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    public String getNivelServicio() {
        return nivelServicio;
    }
    
    public void setNivelServicio(String nivelServicio) {
        this.nivelServicio = nivelServicio;
    }
    
    public Integer getCantidadPasajeros() {
        return cantidadPasajeros;
    }
    
    public void setCantidadPasajeros(Integer cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
    }
    
    public Long getIdRestaurante() {
        return idRestaurante;
    }
    
    public void setIdRestaurante(Long idRestaurante) {
        this.idRestaurante = idRestaurante;
    }
    
    public Double getValorOrden() {
        return valorOrden;
    }
    
    public void setValorOrden(Double valorOrden) {
        this.valorOrden = valorOrden;
    }
    
    public Integer getCantidadProductos() {
        return cantidadProductos;
    }
    
    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
    
    public Integer getCantidadObjetos() {
        return cantidadObjetos;
    }
    
    public void setCantidadObjetos(Integer cantidadObjetos) {
        this.cantidadObjetos = cantidadObjetos;
    }
}
