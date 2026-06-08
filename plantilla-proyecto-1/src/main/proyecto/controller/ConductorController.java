package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ConductorRepository;
import uniandes.edu.co.proyecto.Repositories.UsuarioRepository;
import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.modelo.Conductor;
import uniandes.edu.co.proyecto.modelo.Usuario;

@Controller
public class ConductorController {
    
    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/conductores")
    public String conductores(Model model){
        model.addAttribute("conductores", conductorRepository.darConductores());
        return "conductores";
    }

    @GetMapping("/conductores/new")
    public String conductorFrom(Model model){
        model.addAttribute("conductor", new Conductor());
        return "conductorNuevo";
    }

    @PostMapping("/conductores/new/save")
    public String conductorGuardar(@ModelAttribute Usuario usuario){
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Conductor conductor = new Conductor();
        conductor.setUsuario(usuarioGuardado);

        conductorRepository.save(conductor);

        return "redirect:/conductores";
    }

    @GetMapping("/conductores/{id}/edit")
    public String conductorEditarForm(@PathVariable("id") long id, Model model){
        Conductor conductor = conductorRepository.darConductor(id);
        if (conductor != null){
            model.addAttribute("conductor", conductor);
            return "conductorEditar";
        } else {
            return "redirect:/conductores";
        }
    }

    @PostMapping("/conductores/{id}/edit/save")
    public String conductorEditarGuardar(@PathVariable("id") long id, @ModelAttribute Conductor conductor){
        conductorRepository.actualizarConductor(id, conductor.getIdUsuario());
        return "redirect:/conductores";
    }

    @GetMapping("/conductores/{id}/delete")
    public String conductorEliminar(@PathVariable("id") long id){
        conductorRepository.eliminarConductor(id);
        return "redirect:/conductores";
    }

    @PostMapping("/conductores/{id}/disponible")
    public String marcarDisponible(@PathVariable("id") long id, @org.springframework.web.bind.annotation.RequestParam("valor") Integer valor) {
        if (valor == null) {
            valor = 0;
        }
        conductorRepository.marcarConductorDisponible(id, valor);
        return "redirect:/conductores";
    }

}
