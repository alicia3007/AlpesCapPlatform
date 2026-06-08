package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniandes.edu.co.proyecto.dto.SolicitudServicioDTO;
import uniandes.edu.co.proyecto.service.SolicitudServicioService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para RF8 - Solicitar un servicio por parte de un usuario
 * 
 * Endpoints:
 * - POST /api/rf8/solicitar : Solicita un nuevo servicio
 * - PUT /api/rf8/{id}/finalizar : Finaliza un servicio existente
 */
@RestController
@RequestMapping("/api/rf8")
@CrossOrigin(origins = "*")
public class SolicitudServicioController {
    
    @Autowired
    private SolicitudServicioService solicitudServicioService;
    
    /**
     * Solicita un nuevo servicio de transporte
     * 
     * @param solicitud Datos de la solicitud del servicio
     * @return ResponseEntity con el ID del servicio creado o mensaje de error
     */
    @PostMapping("/solicitar")
    public ResponseEntity<Map<String, Object>> solicitarServicio(@RequestBody SolicitudServicioDTO solicitud) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validaciones básicas
            if (solicitud.getIdCliente() == null) {
                response.put("error", "El ID del cliente es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (solicitud.getIdPuntoPartida() == null) {
                response.put("error", "El punto de partida es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (solicitud.getIdPuntoLlegada() == null) {
                response.put("error", "El punto de llegada es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (solicitud.getTipoServicio() == null || solicitud.getTipoServicio().isEmpty()) {
                response.put("error", "El tipo de servicio es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar tipo de servicio
            if (!solicitud.getTipoServicio().matches("PASAJEROS|DOMICILIO|MERCANCIAS")) {
                response.put("error", "Tipo de servicio no válido. Debe ser: PASAJEROS, DOMICILIO o MERCANCIAS");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar nivel de servicio si se especifica
            if (solicitud.getNivelServicio() != null && !solicitud.getNivelServicio().isEmpty()) {
                if (!solicitud.getNivelServicio().matches("ESTANDAR|CONFORT|LARGE|NA")) {
                    response.put("error", "Nivel de servicio no válido. Debe ser: ESTANDAR, CONFORT, LARGE o NA");
                    return ResponseEntity.badRequest().body(response);
                }
            }
            
            // Validaciones específicas por tipo de servicio
            if ("DOMICILIO".equals(solicitud.getTipoServicio()) && solicitud.getIdRestaurante() == null) {
                response.put("error", "Para servicio de DOMICILIO debe especificar un restaurante");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Solicitar el servicio
            Long idServicio = solicitudServicioService.solicitarServicio(solicitud);
            
            response.put("success", true);
            response.put("message", "Servicio solicitado exitosamente");
            response.put("idServicio", idServicio);
            response.put("detalles", Map.of(
                "idCliente", solicitud.getIdCliente(),
                "tipoServicio", solicitud.getTipoServicio(),
                "nivelServicio", solicitud.getNivelServicio() != null ? solicitud.getNivelServicio() : "ESTANDAR"
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            
            // Determinar el código de estado HTTP apropiado
            if (e.getMessage().contains("medio de pago")) {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
            } else if (e.getMessage().contains("conductores disponibles")) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Finaliza un servicio existente
     * 
     * @param id ID del servicio a finalizar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Map<String, Object>> finalizarServicio(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            solicitudServicioService.finalizarServicio(id);
            
            response.put("success", true);
            response.put("message", "Servicio finalizado exitosamente");
            response.put("idServicio", id);
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            
            if (e.getMessage().contains("no existe")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (e.getMessage().contains("ya ha sido finalizado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint de prueba para verificar que el controlador está activo
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "RF8 - SolicitudServicioController");
        response.put("endpoint", "/api/rf8");
        return ResponseEntity.ok(response);
    }
}
