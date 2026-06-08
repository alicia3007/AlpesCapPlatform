package uniandes.edu.co.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniandes.edu.co.proyecto.modelo.*;
import uniandes.edu.co.proyecto.Repositories.DisponibilidadRepository;
import uniandes.edu.co.proyecto.service.ServiciosTransporteService;

@RestController
@RequestMapping("/api")
public class ServiciosTransporteController {

    private final ServiciosTransporteService serviciosTransporteService;
    private final DisponibilidadRepository disponibilidadRepository;

    public ServiciosTransporteController(ServiciosTransporteService serviciosTransporteService,
                                        DisponibilidadRepository disponibilidadRepository) {
        this.serviciosTransporteService = serviciosTransporteService;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    // RF1: Registrar ciudad
    @PostMapping("/ciudades")
    public ResponseEntity<?> registrarCiudad(@RequestParam String nombre) {
        try {
            Ciudad ciudad = serviciosTransporteService.registrarCiudad(nombre);
            return ResponseEntity.ok(ciudad);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF2: Registrar usuario de servicios
    @PostMapping("/clientes")
    public ResponseEntity<?> registrarCliente(@RequestParam String nombre,
                                              @RequestParam Long cedula,
                                              @RequestParam String correo,
                                              @RequestParam Long celular) {
        try {
            Cliente cliente = serviciosTransporteService.registrarCliente(nombre, cedula, correo, celular);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF3: Registrar conductor
    @PostMapping("/conductores")
    public ResponseEntity<?> registrarConductor(@RequestParam String nombre,
                                                @RequestParam Long cedula,
                                                @RequestParam String correo,
                                                @RequestParam Long celular) {
        try {
            Conductor conductor = serviciosTransporteService.registrarConductor(nombre, cedula, correo, celular);
            return ResponseEntity.ok(conductor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF4: Registrar vehículo
    @PostMapping("/vehiculos")
    public ResponseEntity<?> registrarVehiculo(@RequestParam Long idConductor,
                                               @RequestParam String placa,
                                               @RequestParam String marca,
                                               @RequestParam String modelo,
                                               @RequestParam Integer capacidad,
                                               @RequestParam String tipo) {
        try {
            Vehiculo vehiculo = serviciosTransporteService.registrarVehiculo(idConductor, placa, 
                                                                            marca, modelo, capacidad, tipo);
            return ResponseEntity.ok(vehiculo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF5: Registrar disponibilidad
    @PostMapping("/disponibilidades")
    public ResponseEntity<?> registrarDisponibilidad(@RequestParam Long idConductor,
                                                       @RequestParam Long idVehiculo,
                                                       @RequestParam String tipoServicio,
                                                       @RequestParam String diaInicio,
                                                       @RequestParam String diaFin,
                                                       @RequestParam String horaInicio,
                                                       @RequestParam String horaFin) {
        try {
            serviciosTransporteService.registrarDisponibilidad(idConductor, idVehiculo, tipoServicio,
                                                             diaInicio, diaFin, horaInicio, horaFin);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar todas las disponibilidades
    @GetMapping("/disponibilidades")
    public ResponseEntity<?> consultarDisponibilidades() {
        try {
            return ResponseEntity.ok(disponibilidadRepository.darDisponibilidades());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar disponibilidades por conductor
    @GetMapping("/disponibilidades/conductor/{idConductor}")
    public ResponseEntity<?> consultarDisponibilidadesPorConductor(@PathVariable Long idConductor) {
        try {
            return ResponseEntity.ok(disponibilidadRepository.darDisponibilidadesPorConductor(idConductor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar disponibilidad por ID
    @GetMapping("/disponibilidades/{id}")
    public ResponseEntity<?> consultarDisponibilidadPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(disponibilidadRepository.darDisponibilidadPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF6: Modificar disponibilidad
    @PutMapping("/disponibilidades/{id}")
    public ResponseEntity<?> modificarDisponibilidad(@PathVariable("id") Long idDisponibilidad,
                                                      @RequestParam String diaInicio,
                                                      @RequestParam String diaFin,
                                                      @RequestParam String horaInicio,
                                                      @RequestParam String horaFin) {
        try {
            serviciosTransporteService.modificarDisponibilidad(idDisponibilidad, diaInicio, 
                                                             diaFin, horaInicio, horaFin);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF7: Registrar punto geográfico
    @PostMapping("/puntos")
    public ResponseEntity<Punto> registrarPunto(@RequestParam String nombre,
                                              @RequestParam String direccion,
                                              @RequestParam Double latitud,
                                              @RequestParam Double longitud,
                                              @RequestParam Long idCiudad) {
        try {
            Punto punto = serviciosTransporteService.registrarPunto(nombre, direccion, latitud, 
                                                                   longitud, idCiudad);
            return ResponseEntity.ok(punto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // RF8: Solicitar servicio
    @PostMapping("/servicios")
    public ResponseEntity<Servicio> solicitarServicio(@RequestParam Long idCliente,
                                                     @RequestParam Long puntoOrigen,
                                                     @RequestParam Long puntoDestino,
                                                     @RequestParam String tipoServicio) {
        try {
            Servicio servicio = serviciosTransporteService.solicitarServicio(idCliente, puntoOrigen, 
                                                                            puntoDestino, tipoServicio);
            return ResponseEntity.ok(servicio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // RF9: Finalizar servicio
    @PutMapping("/servicios/{id}/finalizar")
    public ResponseEntity<Void> finalizarServicio(@PathVariable("id") Long idServicio,
                                                @RequestParam Double longitud) {
        try {
            serviciosTransporteService.finalizarServicio(idServicio, longitud);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // RF10: Dejar revisión por cliente
    @PostMapping("/servicios/{id}/revisiones/cliente")
    public ResponseEntity<Void> dejarRevisionCliente(@PathVariable("id") Long idServicio,
                                                   @RequestParam Long idCliente,
                                                   @RequestParam Integer calificacion,
                                                   @RequestParam String comentario) {
        try {
            serviciosTransporteService.dejarRevisionCliente(idServicio, idCliente, calificacion, comentario);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // RF11: Dejar revisión por conductor
    @PostMapping("/servicios/{id}/revisiones/conductor")
    public ResponseEntity<Void> dejarRevisionConductor(@PathVariable("id") Long idServicio,
                                                     @RequestParam Long idConductor,
                                                     @RequestParam Integer calificacion,
                                                     @RequestParam String comentario) {
        try {
            serviciosTransporteService.dejarRevisionConductor(idServicio, idConductor, calificacion, comentario);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // RF10: Dejar revisión por cliente (SIN TRANSACCIONALIDAD)
    @PostMapping("/servicios/{id}/revisiones/cliente/sin-transaccion")
    public ResponseEntity<?> dejarRevisionClienteSinTransaccion(@PathVariable("id") Long idServicio,
                                                                  @RequestParam Long idCliente,
                                                                  @RequestParam Integer calificacion,
                                                                  @RequestParam String comentario) {
        try {
            serviciosTransporteService.dejarRevisionClienteSinTransaccion(idServicio, idCliente, calificacion, comentario);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF11: Dejar revisión por conductor (SIN TRANSACCIONALIDAD)
    @PostMapping("/servicios/{id}/revisiones/conductor/sin-transaccion")
    public ResponseEntity<?> dejarRevisionConductorSinTransaccion(@PathVariable("id") Long idServicio,
                                                                    @RequestParam Long idConductor,
                                                                    @RequestParam Integer calificacion,
                                                                    @RequestParam String comentario) {
        try {
            serviciosTransporteService.dejarRevisionConductorSinTransaccion(idServicio, idConductor, calificacion, comentario);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

