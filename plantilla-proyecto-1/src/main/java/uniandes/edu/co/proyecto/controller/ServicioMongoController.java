package uniandes.edu.co.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import uniandes.edu.co.proyecto.modelo.ServicioMongo;
import uniandes.edu.co.proyecto.Repositories.ClienteMongoRepository;
import uniandes.edu.co.proyecto.Repositories.ServicioMongoRepository;
import uniandes.edu.co.proyecto.service.ServicioMongoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;

import java.util.List;

@Controller
@RequestMapping("/mongo/servicios")
public class ServicioMongoController {

    private final ServicioMongoService servicioService;
    private final ServicioMongoRepository servicioRepo;
    private final ClienteMongoRepository clienteRepo;
    private final MongoTemplate mongoTemplate;

    public ServicioMongoController(ServicioMongoService servicioService, ServicioMongoRepository servicioRepo, ClienteMongoRepository clienteRepo, MongoTemplate mongoTemplate) {
        this.servicioService = servicioService;
        this.servicioRepo = servicioRepo;
        this.clienteRepo = clienteRepo;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String listar(Model model) {
        List<Document> docs = mongoTemplate.findAll(Document.class, "servicios");
        List<java.util.Map<String,Object>> view = new java.util.ArrayList<>();
        for (Document d : docs) {
            java.util.Map<String,Object> m = new java.util.HashMap<>();
            Object id = d.get("_id");
            m.put("id", id != null ? id.toString() : null);

            // clienteId and conductorId may be nested { $oid: '...' }
            Object clienteId = d.get("clienteId");
            m.put("clienteId", extractOid(clienteId));
            Object conductorId = d.get("conductorId");
            m.put("conductorId", extractOid(conductorId));

            Document partida = d.get("puntoPartida", Document.class);
            if (partida != null) {
                m.put("puntoPartidaDireccion", partida.getString("direccion"));
                Document coords = partida.get("coordenadas", Document.class);
                if (coords != null) {
                    Object lat = nestedNumber(coords.get("latitud"));
                    Object lng = nestedNumber(coords.get("longitud"));
                    m.put("puntoPartidaLat", lat);
                    m.put("puntoPartidaLng", lng);
                }
            }

            java.util.List<Document> llegadas = d.get("puntosLlegada", java.util.List.class);
            if (llegadas != null && !llegadas.isEmpty()) {
                Document dest = llegadas.get(0);
                m.put("destinoDireccion", dest.getString("direccion"));
                Document coords2 = dest.get("coordenadas", Document.class);
                if (coords2 != null) {
                    Object lat2 = nestedNumber(coords2.get("latitud"));
                    Object lng2 = nestedNumber(coords2.get("longitud"));
                    m.put("destinoLat", lat2);
                    m.put("destinoLng", lng2);
                }
            }

            Object costoTotal = nestedNumber(d.get("costoTotal"));
            Object tarifa = nestedNumber(d.get("tarifa"));
            m.put("costoTotal", costoTotal != null ? costoTotal : tarifa);

            m.put("estado", d.getString("estado"));
            Object horaInicio = d.get("horaInicio");
            m.put("horaInicio", horaInicio != null ? horaInicio.toString() : null);

            view.add(m);
        }
        model.addAttribute("servicios", view);
        return "serviciosMongo";
    }

    @GetMapping("/new")
    public String nuevoForm(Model model) {
        ServicioMongo s = new ServicioMongo();
        model.addAttribute("servicio", s);
        model.addAttribute("clientes", clienteRepo.findAll());
        return "servicioMongoNuevo";
    }

    @PostMapping("/new/save")
    public String guardar(@ModelAttribute ServicioMongo servicio, Model model) {
        try {
            // Asumir 1 pasajero si no se especifica
            ServicioMongo creado = servicioService.solicitarServicio(servicio, 1);
            return "redirect:/mongo/servicios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("servicio", servicio);
            model.addAttribute("clientes", clienteRepo.findAll());
            return "servicioMongoNuevo";
        } catch (Exception e) {
            model.addAttribute("error", "Error al solicitar servicio: " + e.getMessage());
            model.addAttribute("servicio", servicio);
            model.addAttribute("clientes", clienteRepo.findAll());
            return "servicioMongoNuevo";
        }
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable String id, Model model) {
        Document d = mongoTemplate.findById(id, Document.class, "servicios");
        java.util.Map<String,Object> m = new java.util.HashMap<>();
        if (d != null) {
            m.put("id", d.get("_id").toString());
            m.put("clienteId", extractOid(d.get("clienteId")));
            m.put("conductorId", extractOid(d.get("conductorId")));
            Document partida = d.get("puntoPartida", Document.class);
            if (partida != null) {
                m.put("puntoPartidaDireccion", partida.getString("direccion"));
                Document coords = partida.get("coordenadas", Document.class);
                if (coords != null) {
                    m.put("puntoPartidaLat", nestedNumber(coords.get("latitud")));
                    m.put("puntoPartidaLng", nestedNumber(coords.get("longitud")));
                }
            }
            java.util.List<Document> lleg = d.get("puntosLlegada", java.util.List.class);
            if (lleg != null && !lleg.isEmpty()) {
                Document dest = lleg.get(0);
                m.put("destinoDireccion", dest.getString("direccion"));
                Document coords2 = dest.get("coordenadas", Document.class);
                if (coords2 != null) {
                    m.put("destinoLat", nestedNumber(coords2.get("latitud")));
                    m.put("destinoLng", nestedNumber(coords2.get("longitud")));
                }
            }
            m.put("costoTotal", nestedNumber(d.get("costoTotal")));
            m.put("tarifaId", extractOid(d.get("tarifaId")));
            m.put("estado", d.getString("estado"));
            m.put("horaInicio", d.get("horaInicio") != null ? d.get("horaInicio").toString() : null);
            m.put("cantidadPasajeros", d.getInteger("cantidadPasajeros"));
        }
        model.addAttribute("servicio", m);
        return "servicioMongoDetalle";
    }

    // Helper: extract $oid if nested or return string
    private String extractOid(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Document) {
            Object oid = ((Document)obj).get("$oid");
            return oid != null ? oid.toString() : obj.toString();
        }
        return obj.toString();
    }

    // Helper: numeric extraction (Decimal128 wrappers etc.)
    private Object nestedNumber(Object o) {
        if (o == null) return null;
        try {
            if (o instanceof Document) {
                Document d = (Document)o;
                Object n = d.get("$numberDecimal");
                if (n != null) return Double.parseDouble(n.toString());
                n = d.get("$numberInt");
                if (n != null) return Integer.parseInt(n.toString());
                n = d.get("$numberLong");
                if (n != null) return Long.parseLong(n.toString());
            }
            if (o instanceof Number) return o;
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            return o.toString();
        }
    }
}
