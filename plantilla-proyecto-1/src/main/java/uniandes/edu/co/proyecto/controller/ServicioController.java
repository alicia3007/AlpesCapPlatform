package uniandes.edu.co.proyecto.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import uniandes.edu.co.proyecto.Repositories.ClienteRepository;
import uniandes.edu.co.proyecto.Repositories.HistorialRepository;
import uniandes.edu.co.proyecto.Repositories.PagoRepository;
import uniandes.edu.co.proyecto.Repositories.ServicioPuntoRepository;
import uniandes.edu.co.proyecto.Repositories.ServicioRepository;
import uniandes.edu.co.proyecto.Repositories.TarjetaCreditoRepository;
import uniandes.edu.co.proyecto.Repositories.VehiculoRepository;
import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.modelo.Servicio;
import uniandes.edu.co.proyecto.modelo.Vehiculo;
import uniandes.edu.co.proyecto.service.HistorialServiciosService;
import uniandes.edu.co.proyecto.service.HistorialServiciosService.ResultadoDobleConsulta;

@Controller
public class ServicioController {
    
    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private uniandes.edu.co.proyecto.Repositories.ConductorRepository conductorRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private uniandes.edu.co.proyecto.Repositories.RevisionRepository revisionRepository;

    @Autowired
    private HistorialServiciosService historialServiciosService;

    @PostMapping("/servicios/{id}/revision/cliente")
    @ResponseBody
    public ResponseEntity<?> crearRevisionCliente(
        @PathVariable("id") Long idServicio,
        @RequestParam("idCliente") Long idCliente,
        @RequestParam("calificacion") Integer calificacion,
        @RequestParam(value = "comentario", required = false) String comentario) {
        
        // Validar que la calificación esté entre 1 y 5
        if (calificacion < 1 || calificacion > 5) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La calificación debe estar entre 1 y 5"));
        }

        // Verificar que el cliente participó en el servicio
        if (servicioRepository.verificarClienteParticipo(idServicio, idCliente) == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "El cliente no participó en este servicio"));
        }

        // Verificar que el cliente no haya dejado ya una revisión
        if (revisionRepository.verificarRevisionExistente(idServicio, idCliente) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El cliente ya dejó una revisión para este servicio"));
        }

        // Obtener el servicio para la revisión
        Servicio servicio = servicioRepository.darServicio(idServicio);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Insertar la revisión del cliente al conductor
            revisionRepository.insertarRevision(
                idServicio,
                idCliente,
                servicio.getConductor().getIdUsuario(),
                calificacion,
                comentario
            );
            return ResponseEntity.ok()
                .body(Map.of("mensaje", "Revisión del cliente creada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear la revisión: " + e.getMessage()));
        }
    }

    @PostMapping("/servicios/{id}/revision/conductor")
    @ResponseBody
    public ResponseEntity<?> crearRevisionConductor(
        @PathVariable("id") Long idServicio,
        @RequestParam("idConductor") Long idConductor,
        @RequestParam("calificacion") Integer calificacion,
        @RequestParam(value = "comentario", required = false) String comentario) {
        
        // Validar que la calificación esté entre 1 y 5
        if (calificacion < 1 || calificacion > 5) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "La calificación debe estar entre 1 y 5"));
        }

        // Verificar que el conductor participó en el servicio
        if (servicioRepository.verificarConductorParticipo(idServicio, idConductor) == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "El conductor no participó en este servicio"));
        }

        // Verificar que el conductor no haya dejado ya una revisión
        if (revisionRepository.verificarRevisionExistente(idServicio, idConductor) > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El conductor ya dejó una revisión para este servicio"));
        }

        // Obtener el servicio para la revisión
        Servicio servicio = servicioRepository.darServicio(idServicio);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Insertar la revisión del conductor al cliente
            revisionRepository.insertarRevision(
                idServicio,
                idConductor,
                servicio.getCliente().getIdUsuario(),
                calificacion,
                comentario
            );
            return ResponseEntity.ok()
                .body(Map.of("mensaje", "Revisión del conductor creada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear la revisión: " + e.getMessage()));
        }
    }
    
    @Autowired
    private TarjetaCreditoRepository tarjetacreditoRepository;
    
    @Autowired
    private ServicioPuntoRepository servicioPuntoRepository;
    

    @GetMapping("/servicios")
    public String servicios(Model model){
        model.addAttribute("servicios", servicioRepository.darServicios());
        return "servicios";
    }
    
    @GetMapping("/servicios/new")
    public String servicioFrom(Model model){
        model.addAttribute("servicio", new Servicio());
        return "servicioNuevo";
    }
    
    @PostMapping("/servicios/new/save")
    public String servicioGuardar(
    @RequestParam("tipoServicio") String tipoServicio,
    @RequestParam("idCliente") Long idCliente,
    @RequestParam("idConductor") Long idConductor,
    @RequestParam("idVehiculo") Long idVehiculo,
    @RequestParam("idTarifa") Long idTarifa,
    @RequestParam("duracionMinutos") int duracionMinutos,
    @RequestParam("horaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date horaInicio,
    @RequestParam("horaFin") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date horaFin,
    @RequestParam(value = "nivelAplicado", required = false) String nivelAplicado,
    @RequestParam("tarifaKmAplicada") double tarifaKmAplicada,
    @RequestParam("porcentajeComision") double porcentajeComision,
    Model model
) {
    int count = servicioRepository.verificarDisponibilidad(idConductor, horaInicio, horaFin);
    if (count > 0) {
        model.addAttribute("error", "El conductor ya tiene un servicio o disponibilidad en ese horario.");
        model.addAttribute("servicio", new Servicio());
        return "servicioNuevo"; 
    }

    if (tipoServicio.equalsIgnoreCase("PASAJEROS") && (nivelAplicado == null || nivelAplicado.isEmpty())) {
        if (idVehiculo % 3 == 0) {
            nivelAplicado = "Large";
        } else if (idVehiculo % 2 == 0) {
            nivelAplicado = "Confort";
        } else {
            nivelAplicado = "Estándar";
        }
    }

    servicioRepository.insertarServicio(
        tipoServicio, idCliente, idConductor, idVehiculo, idTarifa,
        duracionMinutos, horaInicio, horaFin,
        nivelAplicado, tarifaKmAplicada, porcentajeComision
    );

    return "redirect:/servicios";
}




    @GetMapping("servicios/{id}/edit")
    public String servicioEditarForm(@PathVariable("id") long id, Model model){
        Servicio servicio = servicioRepository.darServicio(id);
        if (servicio != null){
            model.addAttribute("servicio", servicio);
            return "servicioEditar";
        } else {
            return "redirect:/servicios";
        }
    }

    @PostMapping("/servicios/{id}/edit/save")
    public String servicioEditarGuardar(@PathVariable("id") long id, @ModelAttribute Servicio servicio){
        servicioRepository.actualizarServicio(id,servicio.getTipoServicio(),servicio.getCliente(),servicio.getConductor(),servicio.getVehiculo(),servicio.getTarifa(),servicio.getDuracionMinutos(),servicio.getHoraInicio(),
        servicio.getHoraFin(),servicio.getNivelAplicado(),servicio.getTarifaKmAplicada(),servicio.getPorcentajeComision());
        return "redirect:/servicios";
    }

    @GetMapping("/servicios/{id}/delete")
    public String servicioEliminar(@PathVariable("id") long id){
        servicioRepository.eliminarServicio(id);
        return "redirect:/servicios";
    }

    @GetMapping("/servicios/{id}/disponibilidad")
    public String mostrarDisponibilidad(@PathVariable("id") Long id, Model model) {
        Servicio servicio = servicioRepository.findById(id).orElse(null);
        if (servicio == null) {
            return "redirect:/servicios";
        }
        model.addAttribute("servicio", servicio);
        return "servicioDisponibilidad";
    }

    @PostMapping("/servicios/{id}/disponibilidad/save")
    public String guardarDisponibilidad(
    @PathVariable("id") Long id,
    @org.springframework.web.bind.annotation.RequestParam("horaInicio") 
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horaInicio,
    @org.springframework.web.bind.annotation.RequestParam("horaFin") 
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date horaFin,
    Model model
) {
    Servicio servicio = servicioRepository.findById(id).orElse(null);

        if (horaInicio == null || horaFin == null || !horaInicio.before(horaFin)) {
        model.addAttribute("servicio", servicio);
        model.addAttribute("error", "Rango de tiempo inválido.");
        return "servicioDisponibilidad";
    }

    if (servicio == null) {
        return "redirect:/servicios";
    }

    Long idVehiculo = servicio.getVehiculo().getIdVehiculo();
    int conflictos = servicioRepository.verificarTraslapeDisponibilidad(idVehiculo, id, horaInicio, horaFin);
    if (conflictos > 0) {
        model.addAttribute("servicio", servicio);
        model.addAttribute("error", "El vehículo ya tiene un servicio en ese rango de tiempo.");
        return "servicioDisponibilidad";
    }

    Long idConductor = servicio.getConductor() != null ? servicio.getConductor().getIdUsuario() : null;
    if (idConductor != null) {
        int conflictosConductor = servicioRepository.verificarTraslapeDisponibilidadConductor(idConductor, id, horaInicio, horaFin);
        if (conflictosConductor > 0) {
            model.addAttribute("servicio", servicio);
            model.addAttribute("error", "El conductor ya tiene un servicio o disponibilidad en ese rango de tiempo.");
            return "servicioDisponibilidad";
        }
    }

    servicioRepository.actualizarDisponibilidadServicio(id, horaInicio, horaFin);

    return "redirect:/servicios";
    }

    @GetMapping("/servicios/solicitar")
    public String solicitarServicioForm(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("clientes", clienteRepository.darClientes());
        return "servicioSolicitar";
    }

    @PostMapping("/servicios/solicitar/save")
    public String solicitarServicioGuardar(
            @RequestParam("idCliente") Long idCliente,
            @RequestParam("idPuntoPartida") Long idPuntoPartida,    // ahora esperamos ids de Punto
            @RequestParam("idPuntoLlegada") Long idPuntoLlegada,
            @RequestParam("nivel") String nivel,
            @RequestParam("idTarjeta") Long idTarjeta,              // tarjeta para el cobro
            Model model) {
    
        // 1) Validar tarjeta del usuario (simple: que exista)
        if (idTarjeta == null || tarjetacreditoRepository.darTarjetaCredito(idTarjeta) == null) {
            model.addAttribute("error", "No se encontró medio de pago. Registre una tarjeta antes de solicitar.");
            model.addAttribute("servicio", new Servicio());
            return "servicioSolicitar";
        }
    
        // 2) Calcular tarifa (Ejemplo: usar la heurística existente)
        double distanciaKm = 10.0; // placeholder. Preferible calcular real con puntos
        double tarifaBase = switch (nivel.toLowerCase()) {
            case "confort" -> 3500;
            case "large" -> 5000;
            default -> 2000;
        };
        double tarifaKmAplicada = tarifaBase * distanciaKm;
        double porcentajeComision = 0.1;
        double tarifaFinal = tarifaKmAplicada * (1 + porcentajeComision);
    
        // 3) Asignar vehículo disponible (usa método existente)
        Long idVehiculo = servicioRepository.asignarVehiculoDisponible();
        if (idVehiculo == null) {
            model.addAttribute("error", "No hay conductores disponibles en este momento.");
            model.addAttribute("servicio", new Servicio());
            return "servicioSolicitar";
        }
    
        // 4) Obtener vehículo y conductor
        Vehiculo vehiculo = vehiculoRepository.darVehiculo(idVehiculo);
        if (vehiculo == null || vehiculo.getConductor() == null) {
            model.addAttribute("error", "Vehículo asignado no tiene conductor válido.");
            model.addAttribute("servicio", new Servicio());
            return "servicioSolicitar";
        }
        Long idConductor = vehiculo.getConductor().getIdUsuario();
    
        // 5) Preparar horaInicio/horaFin (aquí marcamos inicio en ahora y estimamos finish)
        Date horaInicio = new Date();
        int duracionMinutos = 30;
        Date horaFin = new Date(horaInicio.getTime() + duracionMinutos * 60000L);
    
        // 6) Verificar que el conductor no tenga conflicto
        int conflictos = servicioRepository.verificarDisponibilidad(idConductor, horaInicio, horaFin);
        if (conflictos > 0) {
            model.addAttribute("error", "El conductor asignado tiene conflicto de horarios.");
            model.addAttribute("servicio", new Servicio());
            return "servicioSolicitar";
        }
    
        // 7) Construir entidad Servicio y persistir con JPA (para obtener id)
        Cliente cliente = clienteRepository.darCliente(idCliente); // asume existe
        Servicio servicio = new Servicio();
        servicio.setTipoServicio("PASAJEROS");
        servicio.setCliente(cliente);
        servicio.setConductor(vehiculo.getConductor());
        servicio.setVehiculo(vehiculo);
        servicio.setDuracionMinutos(duracionMinutos);
        servicio.setHoraInicio(horaInicio);
        servicio.setHoraFin(horaFin);
        servicio.setNivelAplicado(nivel);
        servicio.setTarifaKmAplicada(tarifaFinal);
        servicio.setPorcentajeComision(porcentajeComision);
        // si tienes objeto Tarifa, puedes setearlo también; aquí lo dejamos null.
    
        Servicio servicioPersistido = servicioRepository.save(servicio); // devuelve id generado
    
        // 8) Registrar puntos de partida / llegada (usa ServicioPuntoRepository)
        Long idServicio = servicioPersistido.getIdServicio();
        servicioPuntoRepository.insertarServicioPunto(idServicio, "PARTIDA", 1, idPuntoPartida);
        servicioPuntoRepository.insertarServicioPunto(idServicio, "LLEGADA", 1, idPuntoLlegada);
    
        // 9) Registrar pago (simulado) usando PagoRepository
        pagoRepository.insertarPago(idServicio, idTarjeta, tarifaFinal, new java.util.Date());
    
        // 10) Responder (vista de confirmación)
        model.addAttribute("mensaje", "Servicio solicitado y cobrado exitosamente. Tarifa: $" + tarifaFinal);
        return "servicioConfirmado";
    }

    @GetMapping("/usuarios/{id}/historial-servicios")
    public String consultarHistorialServicios(@PathVariable("id") Long id, Model model) {
        List<Object[]> historial = servicioRepository.consultarHistorialServicios(id);
        model.addAttribute("historial", historial);
        model.addAttribute("idUsuario", id);
        return "historialServiciosUsuario";
    }

    /**
     * RFC1 - Endpoint REST para consultar historial de servicios (sin transaccionalidad especial)
     * Retorna JSON para pruebas en Postman
     */
    @GetMapping("/api/rfc1/usuarios/{id}/historial")
    public ResponseEntity<Map<String, Object>> consultarHistorialServiciosREST(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> historial = servicioRepository.consultarHistorialServicios(id);
            
            // Transformar Object[] a una estructura más legible
            List<Map<String, Object>> servicios = new ArrayList<>();
            for (Object[] fila : historial) {
                Map<String, Object> servicio = new HashMap<>();
                servicio.put("idServicio", fila[0]);
                servicio.put("tipoServicio", fila[1]);
                servicio.put("horaInicio", fila[2]);
                servicio.put("horaFin", fila[3]);
                servicio.put("costoTotal", fila[4]);
                servicio.put("direccionPartida", fila[5]);
                servicio.put("direccionLlegada", fila[6]);
                servicio.put("conductor", fila[7]);
                servicio.put("placa", fila[8]);
                servicios.add(servicio);
            }
            
            response.put("success", true);
            response.put("idUsuario", id);
            response.put("totalServicios", servicios.size());
            response.put("servicios", servicios);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al consultar historial: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuarios/{id}/historial-servicios/rc")
    public String consultarHistorialServiciosReadCommitted(@PathVariable("id") Long id, Model model) {
        ResultadoDobleConsulta res = historialServiciosService.consultarHistorialReadCommitted(id);
        model.addAttribute("historialAntes", res.getAntes());
        model.addAttribute("historialDespues", res.getDespues());
        model.addAttribute("aislamiento", "READ_COMMITTED");
        model.addAttribute("idUsuario", id);
        return "historialServiciosUsuarioRFC1";
    }

    @GetMapping("/usuarios/{id}/historial-servicios/serializable")
    public String consultarHistorialServiciosSerializable(@PathVariable("id") Long id, Model model) {
        ResultadoDobleConsulta res = historialServiciosService.consultarHistorialSerializable(id);
        model.addAttribute("historialAntes", res.getAntes());
        model.addAttribute("historialDespues", res.getDespues());
        model.addAttribute("aislamiento", "SERIALIZABLE");
        model.addAttribute("idUsuario", id);
        return "historialServiciosUsuarioRFC1";
    }

    @GetMapping("/conductores/top20")
    public String mostrarTop20Conductores(Model model) {
    List<Object[]> topConductores = servicioRepository.top20ConductoresMasServicios();
    model.addAttribute("topConductores", topConductores);
    return "topConductores";
}

    /**
     * RFC2 - Endpoint REST para consultar top 20 conductores (sin transaccionalidad especial)
     * Retorna JSON para pruebas en Postman
     */
    @GetMapping("/api/rfc2/conductores/top20")
    public ResponseEntity<Map<String, Object>> top20ConductoresREST() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> topConductores = servicioRepository.top20ConductoresMasServicios();
            
            // Transformar Object[] a una estructura más legible
            List<Map<String, Object>> conductores = new ArrayList<>();
            for (Object[] fila : topConductores) {
                Map<String, Object> conductor = new HashMap<>();
                conductor.put("idUsuario", fila[0]);
                conductor.put("nombre", fila[1]);
                conductor.put("correo", fila[2]);
                conductor.put("totalServicios", fila[3]);
                conductores.add(conductor);
            }
            
            response.put("success", true);
            response.put("totalConductores", conductores.size());
            response.put("conductores", conductores);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al consultar top 20 conductores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * RFC3 - Endpoint REST para consultar dinero ganado por conductor (sin transaccionalidad especial)
     * Retorna JSON para pruebas en Postman
     */
    @GetMapping("/api/rfc3/conductores/{id}/dinero-ganado")
    public ResponseEntity<Map<String, Object>> dineroGanadoConductorREST(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> dineroGanado = servicioRepository.consultarDineroGanadoConductor(id);
            
            // Transformar Object[] a una estructura más legible
            List<Map<String, Object>> vehiculosServicios = new ArrayList<>();
            double totalGeneral = 0.0;
            
            for (Object[] fila : dineroGanado) {
                Map<String, Object> detalle = new HashMap<>();
                detalle.put("idConductor", fila[0]);
                detalle.put("nombreConductor", fila[1]);
                detalle.put("placa", fila[2]);
                detalle.put("tipoVehiculo", fila[3]);
                detalle.put("tipoServicio", fila[4]);
                detalle.put("cantidadServicios", fila[5]);
                detalle.put("totalBruto", fila[6]);
                detalle.put("totalComision", fila[7]);
                detalle.put("totalNetoConductor", fila[8]);
                
                // Acumular total neto
                if (fila[8] != null) {
                    totalGeneral += ((Number) fila[8]).doubleValue();
                }
                
                vehiculosServicios.add(detalle);
            }
            
            response.put("success", true);
            response.put("idConductor", id);
            response.put("totalRegistros", vehiculosServicios.size());
            response.put("totalNetoGeneral", totalGeneral);
            response.put("detalle", vehiculosServicios);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al consultar dinero ganado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


@GetMapping("/servicios/utilizacion")
public String mostrarUtilizacionServicios(
        @RequestParam(value = "ciudad", required = false) String ciudad,
        @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
        @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
        Model model) {

    // Si faltan parámetros, renderizamos el formulario vacío en lugar de devolver 400
    if (ciudad == null || fechaInicio == null || fechaFin == null) {
        model.addAttribute("resultados", null);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "utilizacionServicios";
    }

    List<Object[]> resultados = servicioRepository.utilizacionServiciosPorCiudad(ciudad, fechaInicio, fechaFin);

    model.addAttribute("resultados", resultados);
    model.addAttribute("ciudad", ciudad);
    model.addAttribute("fechaInicio", fechaInicio);
    model.addAttribute("fechaFin", fechaFin);

    return "utilizacionServicios";
}

    /**
     * RFC4 - Endpoint REST para consultar utilización de servicios por ciudad (sin transaccionalidad especial)
     * Retorna JSON para pruebas en Postman
     */
    @GetMapping("/api/rfc4/servicios/utilizacion")
    public ResponseEntity<Map<String, Object>> utilizacionServiciosREST(
            @RequestParam("ciudad") String ciudad,
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Object[]> resultados = servicioRepository.utilizacionServiciosPorCiudad(ciudad, fechaInicio, fechaFin);
            
            // Transformar Object[] a una estructura más legible
            List<Map<String, Object>> estadisticas = new ArrayList<>();
            int totalServiciosGeneral = 0;
            
            for (Object[] fila : resultados) {
                Map<String, Object> estadistica = new HashMap<>();
                estadistica.put("ciudad", fila[0]);
                estadistica.put("tipoServicio", fila[1]);
                estadistica.put("nivelAplicado", fila[2]);
                estadistica.put("totalServicios", fila[3]);
                estadistica.put("porcentaje", fila[4]);
                
                // Acumular total
                if (fila[3] != null) {
                    totalServiciosGeneral += ((Number) fila[3]).intValue();
                }
                
                estadisticas.add(estadistica);
            }
            
            response.put("success", true);
            response.put("ciudad", ciudad);
            response.put("fechaInicio", fechaInicio);
            response.put("fechaFin", fechaFin);
            response.put("totalRegistros", estadisticas.size());
            response.put("totalServiciosGeneral", totalServiciosGeneral);
            response.put("estadisticas", estadisticas);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al consultar utilización de servicios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/servicios/{id}/finalizar")
    public ResponseEntity<?> finalizarServicio(
            @PathVariable("id") Long id,
            @org.springframework.web.bind.annotation.RequestParam("horaFin") @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") java.util.Date horaFin,
            @org.springframework.web.bind.annotation.RequestParam("duracionMinutos") int duracionMinutos,
            @org.springframework.web.bind.annotation.RequestParam("distanciaKm") double distanciaKm,
            @org.springframework.web.bind.annotation.RequestParam(value = "idConductor", required = false) Long idConductor,
            Model model) {

        Servicio servicio = servicioRepository.darServicio(id);
        if (servicio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Servicio no encontrado."));
        }

        if (servicio.getHoraInicio() != null && horaFin.before(servicio.getHoraInicio())) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "horaFin debe ser posterior a horaInicio."));
        }

        if (distanciaKm < 0 || duracionMinutos < 0) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "distancia y duracion deben ser valores no negativos."));
        }

        try {
            // 1) Finalizar servicio (actualiza HORA_FIN, DURACION_MINUTOS, ESTADO)
            servicioRepository.finalizarServicio(id, horaFin, duracionMinutos);

            // 2) Actualizar historial asociado(es) para cliente y conductor (se hace por IDSERVICIO)
            historialRepository.actualizarHistorialFinPorServicio(id, horaFin, distanciaKm);

            // 3) Marcar conductor disponible de ser provisto
            if (idConductor != null) {
                conductorRepository.marcarConductorDisponible(idConductor, 1);
            } else if (servicio.getConductor() != null) {
                Long idCond = servicio.getConductor().getIdUsuario();
                conductorRepository.marcarConductorDisponible(idCond, 1);
            }

            Servicio servicioActualizado = servicioRepository.darServicio(id);
            return ResponseEntity.ok(servicioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al finalizar el servicio: " + e.getMessage()));
        }
    }

}

