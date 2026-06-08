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

import uniandes.edu.co.proyecto.modelo.ClienteMongo;
import uniandes.edu.co.proyecto.service.ClienteMongoService;
import uniandes.edu.co.proyecto.Repositories.ClienteMongoRepository;

@Controller
@RequestMapping("/mongo/clientes")
public class ClienteMongoController {

    private final ClienteMongoService clienteMongoService;
    private final ClienteMongoRepository clienteMongoRepository;
    private final MongoTemplate mongoTemplate;

    public ClienteMongoController(ClienteMongoService clienteMongoService, ClienteMongoRepository clienteMongoRepository, MongoTemplate mongoTemplate) {
        this.clienteMongoService = clienteMongoService;
        this.clienteMongoRepository = clienteMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String listar(Model model) {
        // Intentar obtener clientes desde la colección configurada
        List<ClienteMongo> todos = new ArrayList<>();
        try {
            // colección por defecto (repositorio)
            todos.addAll(clienteMongoRepository.findAll());
        } catch (Exception ignored) {
        }

        // Adicional: buscar en colecciones con nombres comunes para clientes
        try {
            Set<String> colecciones = mongoTemplate.getCollectionNames();
            model.addAttribute("dbName", mongoTemplate.getDb().getName());
            model.addAttribute("collections", colecciones);

            for (String coll : colecciones) {
                String lower = coll.toLowerCase();
                if ((lower.contains("cliente") || lower.contains("clientes")) && !coll.equals("clientes")) {
                    try {
                        List<ClienteMongo> encontrados = mongoTemplate.findAll(ClienteMongo.class, coll);
                        for (ClienteMongo c : encontrados) {
                            // evitar duplicados por id
                            if (todos.stream().noneMatch(x -> x.getId() != null && x.getId().equals(c.getId()))) {
                                todos.add(c);
                            }
                        }
                    } catch (Exception e) {
                        // no mapear a ClienteMongo si la estructura no encaja
                    }
                }
            }
        } catch (Exception e) {
            model.addAttribute("dbError", "No se pudo obtener metadatos de la base: " + e.getMessage());
        }

        model.addAttribute("clientes", todos);
        return "clientesMongo";
    }

    @GetMapping("/new")
    public String nuevoForm(Model model) {
        ClienteMongo c = new ClienteMongo();
        c.setTipo("cliente");
        model.addAttribute("cliente", c);
        return "clienteMongoNuevo";
    }

    @PostMapping("/new/save")
    public String guardar(@ModelAttribute ClienteMongo cliente, Model model) {
        try {
            clienteMongoService.registrarCliente(cliente);
            return "redirect:/mongo/clientes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", cliente);
            return "clienteMongoNuevo";
        } catch (Exception e) {
            // Capturar cualquier excepción inesperada y mostrar mensaje en la vista
            model.addAttribute("error", "Error al guardar el cliente: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            model.addAttribute("cliente", cliente);
            return "clienteMongoNuevo";
        }
    }
}
