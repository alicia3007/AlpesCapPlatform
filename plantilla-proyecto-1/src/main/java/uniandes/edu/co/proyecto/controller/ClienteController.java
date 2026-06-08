package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ClienteRepository;
import uniandes.edu.co.proyecto.Repositories.UsuarioRepository;
import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.modelo.Usuario;

@Controller
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/clientes")
    public String clientes(Model model){
        model.addAttribute("clientes", clienteRepository.darClientes());
        return "clientes";
    }

    @GetMapping("/clientes/new")
    public String clienteFrom(Model model){
        model.addAttribute("cliente", new Cliente());
        return "clienteNuevo";
    }

    @PostMapping("/clientes/new/save")
    public String clienteGuardar(@ModelAttribute Usuario usuario){
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuarioGuardado);

        clienteRepository.save(cliente);

        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{id}/edit")
    public String clienteEditarForm(@PathVariable("id") long id, Model model){
        Cliente cliente = clienteRepository.darCliente(id);
        if (cliente != null){
            model.addAttribute("cliente", cliente);
            return "clienteEditar";
        } else {
            return "redirect:/clientes";
        }
    }

    @PostMapping("/clientes/{id}/edit/save")
    public String clienteEditarGuardar(@PathVariable("id") long id, @ModelAttribute Cliente cliente){
        clienteRepository.actualizarCliente(id, cliente.getIdUsuario());
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{id}/delete")
    public String clienteEliminar(@PathVariable("id") long id){
        clienteRepository.eliminarCliente(id);
        return "redirect:/clientes";
    }
}
