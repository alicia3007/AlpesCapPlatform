package uniandes.edu.co.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.mongodb.core.MongoTemplate;

import uniandes.edu.co.proyecto.modelo.DisponibilidadMongo;
import uniandes.edu.co.proyecto.modelo.ConductorMongo;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;
import uniandes.edu.co.proyecto.Repositories.DisponibilidadMongoRepository;
import uniandes.edu.co.proyecto.service.DisponibilidadMongoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/mongo/disponibilidades")
public class DisponibilidadMongoController {

    private final DisponibilidadMongoService disponibilidadService;
    private final DisponibilidadMongoRepository disponibilidadRepo;
    private final ConductorMongoRepository conductorRepo;
    private final MongoTemplate mongoTemplate;

    public DisponibilidadMongoController(DisponibilidadMongoService disponibilidadService, DisponibilidadMongoRepository disponibilidadRepo, ConductorMongoRepository conductorRepo, MongoTemplate mongoTemplate) {
        this.disponibilidadService = disponibilidadService;
        this.disponibilidadRepo = disponibilidadRepo;
        this.conductorRepo = conductorRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String listar(Model model) {
        List<DisponibilidadMongo> todos = new ArrayList<>();
        try { todos.addAll(disponibilidadRepo.findAll()); } catch (Exception ignored) {}

        try {
            Set<String> colecciones = mongoTemplate.getCollectionNames();
            model.addAttribute("dbName", mongoTemplate.getDb().getName());
            model.addAttribute("collections", colecciones);
        } catch (Exception e) {
            model.addAttribute("dbError", "No se pudo obtener metadatos de la base: " + e.getMessage());
        }

        model.addAttribute("disponibilidades", todos);
        return "disponibilidadesMongo";
    }

    @GetMapping("/new")
    public String nuevoForm(Model model) {
        DisponibilidadMongo d = new DisponibilidadMongo();
        // Prellenar conductores para el select
        List<ConductorMongo> conductores = conductorRepo.findAll();
        model.addAttribute("conductores", conductores);
        model.addAttribute("disponibilidad", d);
        return "disponibilidadMongoNuevo";
    }

    @PostMapping("/new/save")
    public String guardar(@ModelAttribute DisponibilidadMongo disponibilidad, Model model) {
        try {
            disponibilidadService.registrarDisponibilidad(disponibilidad);
            return "redirect:/mongo/disponibilidades";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("disponibilidad", disponibilidad);
            model.addAttribute("conductores", conductorRepo.findAll());
            return "disponibilidadMongoNuevo";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar la disponibilidad: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            model.addAttribute("disponibilidad", disponibilidad);
            model.addAttribute("conductores", conductorRepo.findAll());
            return "disponibilidadMongoNuevo";
        }
    }

    @GetMapping("/{id}/edit")
    public String editarForm(@PathVariable String id, Model model) {
        DisponibilidadMongo d = disponibilidadRepo.findById(id).orElse(new DisponibilidadMongo());
        List<ConductorMongo> conductores = conductorRepo.findAll();
        model.addAttribute("conductores", conductores);
        model.addAttribute("disponibilidad", d);
        return "disponibilidadMongoEditar";
    }

    @PostMapping("/{id}/edit/save")
    public String guardarEdicion(@PathVariable String id, @ModelAttribute DisponibilidadMongo disponibilidad, Model model) {
        try {
            disponibilidad.setId(id);
            disponibilidadService.actualizarDisponibilidad(disponibilidad);
            return "redirect:/mongo/disponibilidades";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("disponibilidad", disponibilidad);
            model.addAttribute("conductores", conductorRepo.findAll());
            return "disponibilidadMongoEditar";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la disponibilidad: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            model.addAttribute("disponibilidad", disponibilidad);
            model.addAttribute("conductores", conductorRepo.findAll());
            return "disponibilidadMongoEditar";
        }
    }

    @GetMapping("/vehiculos")
    public String listarPorVehiculo(Model model) {
        List<DisponibilidadMongo> todas = new ArrayList<>();
        try { todas.addAll(disponibilidadRepo.findAll()); } catch (Exception ignored) {}

        // agrupar por placa (usar vehiculo.placa si existe, si no usar vehiculoPlaca)
        java.util.Map<String, List<DisponibilidadMongo>> porVehiculo = new java.util.LinkedHashMap<>();
        for (DisponibilidadMongo d : todas) {
            String placa = null;
            if (d.getVehiculo() != null && d.getVehiculo().getPlaca() != null) placa = d.getVehiculo().getPlaca();
            if ((placa == null || placa.trim().isEmpty()) && d.getVehiculoPlaca() != null) placa = d.getVehiculoPlaca();
            if (placa == null) placa = "<sin placa>";
            porVehiculo.computeIfAbsent(placa, k -> new ArrayList<>()).add(d);
        }

        model.addAttribute("grupoPorVehiculo", porVehiculo);
        return "disponibilidadesPorVehiculo";
    }

    @GetMapping("/vehiculo/{placa}")
    public String listarPorUnaPlaca(@PathVariable String placa, Model model) {
        List<DisponibilidadMongo> todas = disponibilidadRepo.findAll();
        List<DisponibilidadMongo> lista = new ArrayList<>();
        for (DisponibilidadMongo d : todas) {
            String p = null;
            if (d.getVehiculo() != null) p = d.getVehiculo().getPlaca();
            if ((p == null || p.trim().isEmpty()) && d.getVehiculoPlaca() != null) p = d.getVehiculoPlaca();
            if (p != null && p.equals(placa)) lista.add(d);
        }
        model.addAttribute("placa", placa);
        model.addAttribute("disponibilidades", lista);
        return "disponibilidadesPorVehiculoDetalle";
    }
}
