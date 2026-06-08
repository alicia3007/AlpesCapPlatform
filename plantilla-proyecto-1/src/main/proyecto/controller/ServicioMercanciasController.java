package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ServicioMercanciasRepository;
import uniandes.edu.co.proyecto.modelo.Servicio_Mercancias;

@Controller
public class ServicioMercanciasController {
    
    @Autowired
    private ServicioMercanciasRepository servicioMercanciasRepository;

    @GetMapping("/servicios-mercancias")
    public String serviciosMercancias(Model model){
        model.addAttribute("serviciosMercancias", servicioMercanciasRepository.darServiciosMercancias());
        return "serviciosMercancias";
    }

    @GetMapping("/servicios-mercancias/new")
    public String servicioMercanciasFrom(Model model){
        model.addAttribute("servicioMercancias", new Servicio_Mercancias());
        return "servicioMercanciasNuevo";
    }

    @PostMapping("/servicios-mercancias/new/save")
    public String servicioMercanciasGuardar(@ModelAttribute Servicio_Mercancias sm){
        Long idServicio = sm.getServicio() != null ? sm.getServicio().getIdServicio() : null;
        servicioMercanciasRepository.insertarServicioMercancias(
            idServicio,
            sm.getCantidadObjetos()
        );
        return "redirect:/servicios-mercancias";
    }

    @GetMapping("/servicios-mercancias/{id}/edit")
    public String servicioMercanciasEditarForm(@PathVariable("id") long id, Model model){
        Servicio_Mercancias sm = servicioMercanciasRepository.darServicioMercancias(id);
        if (sm != null){
            model.addAttribute("servicioMercancias", sm);
            return "servicioMercanciasEditar";
        } else {
            return "redirect:/servicios-mercancias";
        }
    }

    @PostMapping("/servicios-mercancias/{id}/edit/save")
    public String servicioMercanciasEditarGuardar(@PathVariable("id") long id, @ModelAttribute Servicio_Mercancias sm){
        servicioMercanciasRepository.actualizarServicioMercancias(
            id,
            sm.getCantidadObjetos()
        );
        return "redirect:/servicios-mercancias";
    }

    @GetMapping("/servicios-mercancias/{id}/delete")
    public String servicioMercanciasEliminar(@PathVariable("id") long id){
        servicioMercanciasRepository.eliminarServicioMercancias(id);
        return "redirect:/servicios-mercancias";
    }
}
