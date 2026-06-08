package uniandes.edu.co.proyecto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import uniandes.edu.co.proyecto.dto.SolicitudServicioDTO;
import uniandes.edu.co.proyecto.Repositories.*;

/**
 * Servicio para implementar RF8 - Solicitar un servicio por parte de un usuario
 * 
 * Este servicio implementa las siguientes operaciones dentro de una transacción:
 * 1. Verificar que el usuario tiene un medio de pago registrado
 * 2. Buscar un conductor disponible
 * 3. Actualizar el estado del conductor (marcar como no disponible)
 * 4. Registrar el inicio del viaje con todos los datos necesarios
 * 5. Registrar los puntos de partida y llegada
 * 6. Registrar detalles específicos del tipo de servicio
 */
@Service
public class SolicitudServicioService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private ConductorRepository conductorRepository;
    
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private TarifaRepository tarifaRepository;
    
    @Autowired
    private PuntoRepository puntoRepository;
    
    @Autowired
    private ServicioPuntoRepository servicioPuntoRepository;
    
    @Autowired
    private ServicioPasajerosRepository servicioPasajerosRepository;
    
    @Autowired
    private ServicioDomicilioRepository servicioDomicilioRepository;
    
    @Autowired
    private ServicioMercanciasRepository servicioMercanciasRepository;
    
    @Autowired
    private PagoRepository pagoRepository;
    
    /**
     * Solicita un servicio de transporte de forma transaccional
     * 
     * @param solicitud DTO con los datos de la solicitud del servicio
     * @return ID del servicio creado
     * @throws RuntimeException si alguna validación falla o no se puede completar la transacción
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Long solicitarServicio(SolicitudServicioDTO solicitud) {
        
        // PASO 1: Verificar que el cliente tiene un medio de pago registrado
        int tieneMedioPago = clienteRepository.verificarMedioPagoRegistrado(solicitud.getIdCliente());
        if (tieneMedioPago == 0) {
            throw new RuntimeException("El cliente no tiene un medio de pago registrado. " +
                                     "Por favor registre una tarjeta de crédito antes de solicitar un servicio.");
        }
        
        // Obtener la tarjeta del cliente para el pago
        Long idTarjeta = clienteRepository.obtenerTarjetaCliente(solicitud.getIdCliente());
        if (idTarjeta == null) {
            throw new RuntimeException("No se pudo obtener el medio de pago del cliente.");
        }
        
        // PASO 2: Buscar un conductor disponible
        String nivelServicio = solicitud.getNivelServicio();
        if (nivelServicio == null || nivelServicio.isEmpty()) {
            nivelServicio = "ESTANDAR"; // valor por defecto
        }
        
        // Intentar buscar conductor por nivel específico
        Long idConductor = conductorRepository.buscarConductorDisponiblePorNivel(nivelServicio);
        
        // Si no hay conductor disponible con ese nivel, buscar cualquier conductor disponible
        if (idConductor == null) {
            idConductor = conductorRepository.buscarConductorDisponibleGeneral();
        }
        
        if (idConductor == null) {
            throw new RuntimeException("No hay conductores disponibles en este momento. " +
                                     "Por favor intente más tarde.");
        }
        
        // Obtener el vehículo del conductor
        Long idVehiculo = conductorRepository.obtenerVehiculoConductor(idConductor);
        if (idVehiculo == null) {
            throw new RuntimeException("El conductor no tiene un vehículo asignado.");
        }
        
        // PASO 3: Actualizar el estado del conductor (marcar como no disponible)
        conductorRepository.marcarConductorNoDisponible(idConductor);
        
        // PASO 4: Calcular la tarifa
        // Para DOMICILIO y MERCANCIAS, el nivel de tarifa configurado en la BD es 'NA'
        String nivelParaTarifa = ("DOMICILIO".equalsIgnoreCase(solicitud.getTipoServicio())
                                  || "MERCANCIAS".equalsIgnoreCase(solicitud.getTipoServicio()))
                                  ? "NA"
                                  : nivelServicio;

        Long idTarifa = tarifaRepository.obtenerIdTarifaPorTipoYNivel(
            solicitud.getTipoServicio(), 
            nivelParaTarifa
        );
        
        if (idTarifa == null) {
            // Revertir el cambio del conductor
            conductorRepository.marcarConductorComoDisponible(idConductor);
            throw new RuntimeException("No se encontró una tarifa para el tipo de servicio y nivel solicitados.");
        }
        
        Double tarifaKm = tarifaRepository.obtenerValorKmPorTipoYNivel(
            solicitud.getTipoServicio(), 
            nivelParaTarifa
        );
        
        // Calcular distancia entre puntos
        Double distanciaKm = puntoRepository.calcularDistanciaEnKm(
            solicitud.getIdPuntoPartida(), 
            solicitud.getIdPuntoLlegada()
        );
        
        if (distanciaKm == null) {
            distanciaKm = 10.0; // valor por defecto si no se puede calcular
        }
        
        // Estimar duración en minutos (asumiendo 30 km/h promedio en ciudad)
        Integer duracionMinutos = (int) Math.ceil(distanciaKm / 30.0 * 60.0);
        if (duracionMinutos < 5) {
            duracionMinutos = 5; // mínimo 5 minutos
        }
        
        // PASO 5: Registrar el inicio del servicio
        try {
            servicioRepository.registrarInicioServicio(
                solicitud.getTipoServicio(),
                solicitud.getIdCliente(),
                idConductor,
                idVehiculo,
                idTarifa,
                duracionMinutos,
                nivelParaTarifa,
                tarifaKm
            );
            
            // Obtener el ID del servicio recién creado
            Long idServicio = servicioRepository.obtenerUltimoIdServicio();
            
            if (idServicio == null) {
                throw new RuntimeException("No se pudo obtener el ID del servicio creado.");
            }
            
            // PASO 6: Registrar los puntos de partida y llegada
            servicioPuntoRepository.insertarServicioPunto(
                idServicio, 
                "PARTIDA", 
                1, 
                solicitud.getIdPuntoPartida()
            );
            
            servicioPuntoRepository.insertarServicioPunto(
                idServicio, 
                "LLEGADA", 
                1, 
                solicitud.getIdPuntoLlegada()
            );
            
            // PASO 7: Registrar detalles específicos del tipo de servicio
            switch (solicitud.getTipoServicio()) {
                case "PASAJEROS":
                    Integer cantidadPasajeros = solicitud.getCantidadPasajeros();
                    if (cantidadPasajeros == null || cantidadPasajeros < 1) {
                        cantidadPasajeros = 1;
                    }
                    servicioPasajerosRepository.insertarServicioPasajeros(idServicio, cantidadPasajeros);
                    break;
                    
                case "DOMICILIO":
                    if (solicitud.getIdRestaurante() == null) {
                        throw new RuntimeException("Debe especificar un restaurante para servicio de domicilio.");
                    }
                    Double valorOrden = solicitud.getValorOrden();
                    if (valorOrden == null || valorOrden <= 0) {
                        valorOrden = 10000.0; // valor por defecto
                    }
                    Integer cantidadProductos = solicitud.getCantidadProductos();
                    if (cantidadProductos == null || cantidadProductos < 1) {
                        cantidadProductos = 1;
                    }
                    servicioDomicilioRepository.insertarServicioDomicilio(
                        idServicio, 
                        solicitud.getIdRestaurante(), 
                        valorOrden, 
                        cantidadProductos
                    );
                    // Registrar punto de restaurante
                    // Asumimos que el restaurante tiene un punto asociado
                    break;
                    
                case "MERCANCIAS":
                    Integer cantidadObjetos = solicitud.getCantidadObjetos();
                    if (cantidadObjetos == null || cantidadObjetos < 1) {
                        cantidadObjetos = 1;
                    }
                    servicioMercanciasRepository.insertarServicioMercancias(idServicio, cantidadObjetos);
                    break;
                    
                default:
                    throw new RuntimeException("Tipo de servicio no válido: " + solicitud.getTipoServicio());
            }
            
            // PASO 8: Registrar el pago
            Double costoTotal = duracionMinutos * tarifaKm;
            pagoRepository.insertarPago(
                idServicio, 
                idTarjeta, 
                costoTotal, 
                new java.util.Date()
            );
            
            return idServicio;
            
        } catch (Exception e) {
            // En caso de error, revertir el cambio del conductor
            conductorRepository.marcarConductorComoDisponible(idConductor);
            throw new RuntimeException("Error al registrar el servicio: " + e.getMessage(), e);
        }
    }
    
    /**
     * Finaliza un servicio marcando la hora de fin y actualizando el estado del conductor
     * 
     * @param idServicio ID del servicio a finalizar
     * @throws RuntimeException si el servicio no existe o ya fue finalizado
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void finalizarServicio(Long idServicio) {
        // Obtener el servicio
        var servicio = servicioRepository.darServicio(idServicio);
        if (servicio == null) {
            throw new RuntimeException("El servicio no existe.");
        }
        
        // Verificar que el servicio no haya sido finalizado
        if (servicio.getHoraFin() != null) {
            throw new RuntimeException("El servicio ya ha sido finalizado.");
        }
        
        // Actualizar hora de fin
        servicioRepository.actualizarHoraFin(idServicio, new java.util.Date());
        
        // Marcar conductor como disponible nuevamente
        conductorRepository.marcarConductorComoDisponible(servicio.getConductor().getIdUsuario());
    }
}
