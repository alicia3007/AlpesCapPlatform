package uniandes.edu.co.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uniandes.edu.co.proyecto.service.ViajeMongoService;
import uniandes.edu.co.proyecto.Repositories.ViajeMongoRepository;
import uniandes.edu.co.proyecto.modelo.ViajeMongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Controller
@RequestMapping("/mongo/viajes")
public class ViajeMongoController {

    private final ViajeMongoService viajeService;
    private final ViajeMongoRepository viajeRepo;
    private final MongoTemplate mongoTemplate;

    public ViajeMongoController(ViajeMongoService viajeService, ViajeMongoRepository viajeRepo, MongoTemplate mongoTemplate) {
        this.viajeService = viajeService;
        this.viajeRepo = viajeRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("")
    public String listar(@RequestParam(required = false) String clienteId, Model model) {
        List<Map<String, Object>> viajesList;
        if (clienteId != null && !clienteId.isBlank()) {
            Query q = new Query();
            try {
                q.addCriteria(Criteria.where("pasajeroId").is(new ObjectId(clienteId)));
            } catch (Exception e) {
                q.addCriteria(Criteria.where("pasajeroId").is(clienteId));
            }
            List<Document> docs = mongoTemplate.find(q, Document.class, "viajes");
            viajesList = docs.stream().map(this::normalizeDocument).collect(Collectors.toList());
        } else {
            List<Document> docs = mongoTemplate.findAll(Document.class, "viajes");
            viajesList = docs.stream().map(this::normalizeDocument).collect(Collectors.toList());
        }

        // obtener lista de clientes para el filtro
        List<Document> clientesDocs = mongoTemplate.findAll(Document.class, "clientes");
        List<Map<String, String>> clients = clientesDocs.stream().map(d -> {
            Map<String, String> m = new HashMap<>();
            Object id = d.get("_id");
            m.put("id", id != null ? id.toString() : "");
            String nombre = null;
            Object usuario = d.get("usuario");
            if (usuario instanceof Document) nombre = ((Document) usuario).getString("nombre");
            if (nombre == null) nombre = d.getString("nombre");
            m.put("nombre", nombre != null ? nombre : m.get("id"));
            return m;
        }).collect(Collectors.toList());

        model.addAttribute("viajes", viajesList);
        model.addAttribute("clients", clients);
        model.addAttribute("selectedCliente", clienteId);
        return "viajesMongo";
    }

    private Map<String, Object> normalizeDocument(Document d) {
        Map<String, Object> out = new HashMap<>();
        for (Map.Entry<String, Object> e : d.entrySet()) {
            Object v = e.getValue();
            if (v == null) {
                out.put(e.getKey(), null);
            } else if (v instanceof ObjectId) {
                out.put(e.getKey(), v.toString());
            } else if (v instanceof Document) {
                out.put(e.getKey(), normalizeDocument((Document) v));
            } else if (v instanceof List) {
                List<?> lst = (List<?>) v;
                List<Object> mapped = lst.stream().map(item -> {
                    if (item instanceof Document) return normalizeDocument((Document) item);
                    else if (item instanceof ObjectId) return item.toString();
                    else return item;
                }).collect(Collectors.toList());
                out.put(e.getKey(), mapped);
            } else if (v instanceof org.bson.types.Decimal128) {
                out.put(e.getKey(), ((org.bson.types.Decimal128) v).bigDecimalValue().doubleValue());
            } else {
                out.put(e.getKey(), v);
            }
        }
        // convenience id property for templates
        if (out.containsKey("_id") && !out.containsKey("id")) {
            Object idv = out.get("_id");
            out.put("id", idv != null ? idv.toString() : null);
        }
        return out;
    }

    @GetMapping("/top-conductores")
    public String topConductores(Model model) {
        List<Map<String, Object>> results = new java.util.ArrayList<>();

        // pipeline: group by conductorId and count, exclude null, sort desc, limit 20
        List<org.bson.Document> pipeline = new java.util.ArrayList<>();
        pipeline.add(org.bson.Document.parse("{ \"$match\": { \"conductorId\": { \"$ne\": null } } }"));
        pipeline.add(org.bson.Document.parse("{ \"$group\": { \"_id\": \"$conductorId\", \"count\": { \"$sum\": 1 } } }"));
        pipeline.add(org.bson.Document.parse("{ \"$sort\": { \"count\": -1 } }"));
        pipeline.add(org.bson.Document.parse("{ \"$limit\": 20 }"));

        try {
            com.mongodb.client.MongoCollection<org.bson.Document> coll = mongoTemplate.getCollection("servicios");
            com.mongodb.client.AggregateIterable<org.bson.Document> it = coll.aggregate(pipeline);
            int rank = 1;
            for (org.bson.Document r : it) {
                Object conductorIdObj = r.get("_id");
                Object countObj = r.get("count");
                long count = 0L;
                if (countObj instanceof Number) count = ((Number) countObj).longValue();
                String conductorIdStr = conductorIdObj != null ? conductorIdObj.toString() : null;

                // buscar conductor
                org.bson.Document condDoc = null;
                try {
                    if (conductorIdObj instanceof org.bson.types.ObjectId) condDoc = mongoTemplate.findById(conductorIdObj, org.bson.Document.class, "conductores");
                    else condDoc = mongoTemplate.findById(new org.bson.types.ObjectId(conductorIdStr), org.bson.Document.class, "conductores");
                } catch (Exception e) {
                    // fallback: try by string id
                    try { condDoc = mongoTemplate.findById(conductorIdStr, org.bson.Document.class, "conductores"); } catch (Exception ignored) {}
                }

                String nombre = conductorIdStr;
                String telefono = "-";
                if (condDoc != null) {
                    Object usuario = condDoc.get("usuario");
                    if (usuario instanceof org.bson.Document) {
                        nombre = ((org.bson.Document) usuario).getString("nombre");
                    } else if (condDoc.getString("nombre") != null) {
                        nombre = condDoc.getString("nombre");
                    }
                    if (condDoc.getString("telefono") != null) telefono = condDoc.getString("telefono");
                }

                Map<String, Object> m = new HashMap<>();
                m.put("rank", rank);
                m.put("conductorId", conductorIdStr);
                m.put("nombre", nombre != null ? nombre : conductorIdStr);
                m.put("telefono", telefono);
                m.put("serviciosCount", count);
                results.add(m);
                rank++;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al calcular top conductores: " + e.getMessage());
        }

        model.addAttribute("topConductores", results);
        return "topConductores";
    }

    /**
     * Variante que lista los top 20 conductores incluyendo aquellos con 0 servicios.
     * Usa agregación con $lookup desde la colección `conductores` para traer los servicios
     * y calcular el tamaño del array.
     */
    @GetMapping("/top-conductores/incluir-ceros")
    public String topConductoresIncluirCeros(Model model) {
        List<Map<String, Object>> results = new java.util.ArrayList<>();
        List<org.bson.Document> pipeline = new java.util.ArrayList<>();

        // $lookup para unir servicios por conductorId
        pipeline.add(org.bson.Document.parse("{ \"$lookup\": { \"from\": \"servicios\", \"localField\": \"_id\", \"foreignField\": \"conductorId\", \"as\": \"servicios\" } }"));
        pipeline.add(org.bson.Document.parse("{ \"$addFields\": { \"serviciosCount\": { \"$size\": { \"$ifNull\": [\"$servicios\", []] } } } }"));
        pipeline.add(org.bson.Document.parse("{ \"$sort\": { \"serviciosCount\": -1 } }"));
        pipeline.add(org.bson.Document.parse("{ \"$limit\": 20 }"));
        pipeline.add(org.bson.Document.parse("{ \"$project\": { \"nombre\": \"$usuario.nombre\", \"telefono\": 1, \"serviciosCount\": 1 } }"));

        try {
            com.mongodb.client.MongoCollection<org.bson.Document> coll = mongoTemplate.getCollection("conductores");
            com.mongodb.client.AggregateIterable<org.bson.Document> it = coll.aggregate(pipeline);
            int rank = 1;
            for (org.bson.Document r : it) {
                Object idObj = r.get("_id");
                String idStr = idObj != null ? idObj.toString() : null;
                Object nombreObj = r.get("nombre");
                String nombre = nombreObj != null ? nombreObj.toString() : idStr;
                String telefono = r.getString("telefono") != null ? r.getString("telefono") : "-";
                Object sc = r.get("serviciosCount");
                long count = (sc instanceof Number) ? ((Number) sc).longValue() : 0L;

                Map<String, Object> m = new HashMap<>();
                m.put("rank", rank);
                m.put("conductorId", idStr);
                m.put("nombre", nombre);
                m.put("telefono", telefono);
                m.put("serviciosCount", count);
                results.add(m);
                rank++;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al calcular top conductores (incluyendo ceros): " + e.getMessage());
        }

        model.addAttribute("topConductores", results);
        model.addAttribute("includingZeros", true);
        return "topConductores";
    }

    @GetMapping("/nuevo")
    public String nuevoForm() {
        return "viajeMongoNuevo";
    }

    @PostMapping("/registrar")
    public String registrarDesdeForm(@RequestParam String servicioId, RedirectAttributes redirectAttrs) {
        try {
            String id = viajeService.registrarViajeDesdeServicio(servicioId);
            redirectAttrs.addFlashAttribute("msg", "Viaje registrado: " + id);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mongo/viajes";
    }

    @PostMapping("/registrar/{servicioId}")
    @ResponseBody
    public ResponseEntity<String> registrar(@PathVariable String servicioId) {
        try {
            String id = viajeService.registrarViajeDesdeServicio(servicioId);
            return ResponseEntity.ok(id != null ? id : "registrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
