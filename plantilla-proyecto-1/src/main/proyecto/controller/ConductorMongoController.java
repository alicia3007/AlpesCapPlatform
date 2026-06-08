package uniandes.edu.co.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uniandes.edu.co.proyecto.modelo.ConductorMongo;
import uniandes.edu.co.proyecto.service.ConductorMongoService;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;

@Controller
@RequestMapping("/mongo/conductores")
public class ConductorMongoController {

    private final ConductorMongoService conductorMongoService;
    private final ConductorMongoRepository conductorMongoRepository;
    private final MongoTemplate mongoTemplate;

    public ConductorMongoController(ConductorMongoService conductorMongoService, ConductorMongoRepository conductorMongoRepository, MongoTemplate mongoTemplate) {
        this.conductorMongoService = conductorMongoService;
        this.conductorMongoRepository = conductorMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String listar(Model model) {
        List<ConductorMongo> todos = new ArrayList<>();
        try { todos.addAll(conductorMongoRepository.findAll()); } catch (Exception ignored) {}

        try {
            Set<String> colecciones = mongoTemplate.getCollectionNames();
            model.addAttribute("dbName", mongoTemplate.getDb().getName());
            model.addAttribute("collections", colecciones);

            for (String coll : colecciones) {
                String lower = coll.toLowerCase();
                if ((lower.contains("conductor") || lower.contains("conductores")) && !coll.equals("conductores")) {
                    try {
                        List<ConductorMongo> encontrados = mongoTemplate.findAll(ConductorMongo.class, coll);
                        for (ConductorMongo c : encontrados) {
                            if (todos.stream().noneMatch(x -> x.getId() != null && x.getId().equals(c.getId()))) {
                                todos.add(c);
                            }
                        }
                    } catch (Exception e) {
                        // ignore mapping errors
                    }
                }
            }
        } catch (Exception e) {
            model.addAttribute("dbError", "No se pudo obtener metadatos de la base: " + e.getMessage());
        }

        model.addAttribute("conductores", todos);
        return "conductoresMongo";
    }

    @GetMapping("/new")
    public String nuevoForm(Model model) {
        ConductorMongo c = new ConductorMongo();
        c.setTipo("conductor");
        // Asegurar que la lista de vehículos tenga al menos un elemento para cumplir validación de esquema
        try {
            if (c.getVehiculos() == null || c.getVehiculos().isEmpty()) {
                c.getVehiculos().add(new ConductorMongo.Vehiculo());
            }
        } catch (Exception ignored) {
        }
        model.addAttribute("conductor", c);
        return "conductorMongoNuevo";
    }

    @PostMapping("/new/save")
    public String guardar(@ModelAttribute ConductorMongo conductor, Model model) {
        try {
            conductorMongoService.registrarConductor(conductor);
            return "redirect:/mongo/conductores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("conductor", conductor);
            return "conductorMongoNuevo";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el conductor: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            model.addAttribute("conductor", conductor);
            return "conductorMongoNuevo";
        }
    }
}
