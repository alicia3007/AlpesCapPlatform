package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.Repositories.*;
import uniandes.edu.co.proyecto.modelo.*;
import uniandes.edu.co.proyecto.dto.*;
import uniandes.edu.co.proyecto.exception.NegocioException;

@Service
public class ServiciosTransporteService {
    
    private final CiudadRepository ciudadRepository;
    private final ClienteRepository clienteRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final DisponibilidadRepository disponibilidadRepository;
    private final PuntoRepository puntoRepository;
    private final ServicioRepository servicioRepository;
    private final RevisionRepository revisionRepository;

    public ServiciosTransporteService(
            CiudadRepository ciudadRepository,
            ClienteRepository clienteRepository,
            ConductorRepository conductorRepository,
            VehiculoRepository vehiculoRepository,
            DisponibilidadRepository disponibilidadRepository,
            PuntoRepository puntoRepository,
            ServicioRepository servicioRepository,
            RevisionRepository revisionRepository) {
        this.ciudadRepository = ciudadRepository;
        this.clienteRepository = clienteRepository;
        this.conductorRepository = conductorRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.puntoRepository = puntoRepository;
        this.servicioRepository = servicioRepository;
        this.revisionRepository = revisionRepository;
    }

    // RF1: Registrar ciudad
    @Transactional
    public Ciudad registrarCiudad(String nombre) {
        if (ciudadRepository.existeCiudadPorNombre(nombre)) {
            throw new NegocioException("La ciudad ya existe");
        }
        ciudadRepository.insertarCiudad(nombre);
        Ciudad ciudad = ciudadRepository.darCiudadPorNombre(nombre);
        if (ciudad == null) {
            throw new NegocioException("Error al registrar la ciudad");
        }
        return ciudad;
    }

    // RF2: Registrar usuario de servicios
    @Transactional
    public Cliente registrarCliente(String nombre, Long cedula, String correo, Long celular) {
        if (clienteRepository.existeClientePorCedula(cedula) > 0) {
            throw new NegocioException("Ya existe un usuario con esta cédula");
        }
        
        if (clienteRepository.existeClientePorCorreo(correo) > 0) {
            throw new NegocioException("Ya existe un usuario con este correo");
        }
        
        clienteRepository.insertarUsuario(nombre, correo, celular, cedula);
        clienteRepository.insertarClientePorCedula(cedula);
        Cliente cliente = clienteRepository.darClientePorCedula(cedula);
        if (cliente == null) {
            throw new NegocioException("Error al registrar el cliente");
        }
        return cliente;
    }

    // RF3: Registrar conductor
    @Transactional
    public Conductor registrarConductor(String nombre, Long cedula, String correo, Long celular) {
        if (conductorRepository.existeConductorPorCedula(cedula) > 0) {
            throw new NegocioException("Ya existe un conductor con esta cédula");
        }
        conductorRepository.insertarUsuarioConductor(nombre, correo, celular, cedula);
        conductorRepository.insertarConductorPorCedula(cedula);
        Conductor conductor = conductorRepository.darConductorPorCedula(cedula);
        if (conductor == null) {
            throw new NegocioException("Error al registrar el conductor");
        }
        return conductor;
    }

    // RF4: Registrar vehículo para conductor
    @Transactional
    public Vehiculo registrarVehiculo(Long idConductor, String placa, String marca, String modelo, 
                                    Integer capacidad, String tipo) {
        if (vehiculoRepository.existeVehiculoPorPlaca(placa) > 0) {
            throw new NegocioException("Ya existe un vehículo con la placa " + placa);
        }
        if (conductorRepository.existeConductor(idConductor) == 0) {
            throw new NegocioException("No existe conductor con ID " + idConductor);
        }
        VehiculoDTO vehiculoDTO = new VehiculoDTO(tipo, marca, modelo, "", placa, "", capacidad, "", idConductor);
        vehiculoRepository.insertarVehiculo(vehiculoDTO);
        Vehiculo vehiculo = vehiculoRepository.darVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            throw new NegocioException("Error al registrar el vehículo");
        }
        return vehiculo;
    }

    // RF5: Registrar disponibilidad
    @Transactional
    public void registrarDisponibilidad(Long idConductor, Long idVehiculo, String tipoServicio,
                                      String diaInicio, String diaFin, String horaInicio, String horaFin) {
        // Validar superposición de horarios
        if (disponibilidadRepository.existeSuperposicion(idConductor, diaInicio, diaFin, horaInicio, horaFin) > 0) {
            throw new NegocioException("Existe superposición con otra disponibilidad");
        }
        disponibilidadRepository.insertarDisponibilidad(idConductor, idVehiculo, tipoServicio,
                                                      diaInicio, diaFin, horaInicio, horaFin);
    }

    // RF6: Modificar disponibilidad
    @Transactional
    public void modificarDisponibilidad(Long idDisponibilidad, String diaInicio, String diaFin,
                                      String horaInicio, String horaFin) {
        if (disponibilidadRepository.existeDisponibilidad(idDisponibilidad) == 0) {
            throw new NegocioException("La disponibilidad no existe");
        }
        Long idConductor = disponibilidadRepository.darConductorDeDisponibilidad(idDisponibilidad);
        if (disponibilidadRepository.existeSuperposicionExcluyendo(idConductor, idDisponibilidad,
                diaInicio, diaFin, horaInicio, horaFin) > 0) {
            throw new NegocioException("Existe superposición con otra disponibilidad");
        }
        disponibilidadRepository.actualizarDisponibilidad(idDisponibilidad, diaInicio, diaFin, horaInicio, horaFin);
    }

    // RF7: Registrar punto geográfico
    @Transactional
    public Punto registrarPunto(String nombre, String direccion, Double latitud, Double longitud, Long idCiudad) {
        if (!ciudadRepository.existsById(idCiudad)) {
            throw new NegocioException("La ciudad no existe");
        }
        puntoRepository.insertarPunto(nombre, direccion, latitud, longitud, idCiudad);
        // Buscar el punto recién insertado
        return puntoRepository.findTopByOrderByIdPuntoDesc();
    }

    // RF8: Solicitar servicio
    @Transactional
    public Servicio solicitarServicio(Long idCliente, Long puntoOrigen, Long puntoDestino, String tipoServicio) {
        // Validar medio de pago
        if (clienteRepository.tieneMedioPago(idCliente) == 0) {
            throw new NegocioException("El cliente no tiene medio de pago registrado");
        }

        // Buscar conductor disponible
        Long idConductor = disponibilidadRepository.buscarConductorDisponible(tipoServicio);
        if (idConductor == null) {
            throw new NegocioException("No hay conductores disponibles");
        }

        // Calcular tarifa
        double tarifa = servicioRepository.calcularTarifa(puntoOrigen, puntoDestino, tipoServicio);

        // Registrar servicio
        servicioRepository.iniciarServicio(idCliente, idConductor, puntoOrigen, puntoDestino, tipoServicio, tarifa);
        
        // Marcar conductor como no disponible
        conductorRepository.marcarNoDisponible(idConductor);

        return servicioRepository.findTopByOrderByIdServicioDesc();
    }

    // RF9: Finalizar servicio
    @Transactional
    public void finalizarServicio(Long idServicio, Double longitud) {
        Servicio servicio = servicioRepository.darServicio(idServicio);
        if (servicio == null) {
            throw new NegocioException("El servicio no existe");
        }

        servicioRepository.finalizarServicio(idServicio, longitud);
        conductorRepository.marcarDisponible(servicio.getConductor().getIdUsuario());
    }

    // RF10: Dejar revisión por cliente
    @Transactional
    public void dejarRevisionCliente(Long idServicio, Long idCliente, Integer calificacion, String comentario) {
        if (!servicioRepository.validarClienteParticipo(idServicio, idCliente)) {
            throw new NegocioException("El cliente no participó en este servicio");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new NegocioException("La calificación debe estar entre 1 y 5");
        }
        if (revisionRepository.existeRevisionCliente(idServicio, idCliente)) {
            throw new NegocioException("Ya existe una revisión del cliente para este servicio");
        }
        revisionRepository.insertarRevisionCliente(idServicio, idCliente, calificacion, comentario);
    }

    // RF11: Dejar revisión por conductor (CON TRANSACCIONALIDAD)
    @Transactional
    public void dejarRevisionConductor(Long idServicio, Long idConductor, Integer calificacion, String comentario) {
        if (!servicioRepository.validarConductorParticipo(idServicio, idConductor)) {
            throw new NegocioException("El conductor no participó en este servicio");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new NegocioException("La calificación debe estar entre 1 y 5");
        }
        if (revisionRepository.existeRevisionConductor(idServicio, idConductor)) {
            throw new NegocioException("Ya existe una revisión del conductor para este servicio");
        }
        revisionRepository.insertarRevisionConductor(idServicio, idConductor, calificacion, comentario);
    }

    // RF10: Dejar revisión por cliente (SIN TRANSACCIONALIDAD)
    public void dejarRevisionClienteSinTransaccion(Long idServicio, Long idCliente, Integer calificacion, String comentario) {
        if (!servicioRepository.validarClienteParticipo(idServicio, idCliente)) {
            throw new NegocioException("El cliente no participó en este servicio");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new NegocioException("La calificación debe estar entre 1 y 5");
        }
        if (revisionRepository.existeRevisionCliente(idServicio, idCliente)) {
            throw new NegocioException("Ya existe una revisión del cliente para este servicio");
        }
        revisionRepository.insertarRevisionCliente(idServicio, idCliente, calificacion, comentario);
    }

    // RF11: Dejar revisión por conductor (SIN TRANSACCIONALIDAD)
    public void dejarRevisionConductorSinTransaccion(Long idServicio, Long idConductor, Integer calificacion, String comentario) {
        if (!servicioRepository.validarConductorParticipo(idServicio, idConductor)) {
            throw new NegocioException("El conductor no participó en este servicio");
        }
        if (calificacion < 1 || calificacion > 5) {
            throw new NegocioException("La calificación debe estar entre 1 y 5");
        }
        if (revisionRepository.existeRevisionConductor(idServicio, idConductor)) {
            throw new NegocioException("Ya existe una revisión del conductor para este servicio");
        }
        revisionRepository.insertarRevisionConductor(idServicio, idConductor, calificacion, comentario);
    }
}