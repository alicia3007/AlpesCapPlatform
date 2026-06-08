package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ServicioPasajerosRepository;
import uniandes.edu.co.proyecto.modelo.Servicio_Pasajeros;

@Controller
public class ServicioPasajerosControllers {
    
    @Autowired
    private ServicioPasajerosRepository servicioPasajerosRepository;

    @GetMapping("/servicios-pasajeros")
    public String serviciosPasajeros(Model model){
        model.addAttribute("serviciosPasajeros", servicioPasajerosRepository.darServiciosPasajeros());
        return "serviciosPasajeros";
    }

    @GetMapping("/servicios-pasajeros/new")
    public String servicioPasajerosFrom(Model model){
        model.addAttribute("servicioPasajeros", new Servicio_Pasajeros());
        return "servicioPasajerosNuevo";
    }

    @PostMapping("/servicios-pasajeros/new/save")
    public String servicioPasajerosGuardar(@ModelAttribute Servicio_Pasajeros sp){
        Long idServicio = sp.getServicio() != null ? sp.getServicio().getIdServicio() : null;
        servicioPasajerosRepository.insertarServicioPasajeros(
            idServicio,
            sp.getCantidadPasajeros()
        );
        return "redirect:/servicios-pasajeros";
    }

    @GetMapping("/servicios-pasajeros/{id}/edit")
    public String servicioPasajerosEditarForm(@PathVariable("id") long id, Model model){
        Servicio_Pasajeros sp = servicioPasajerosRepository.darServicioPasajeros(id);
        if (sp != null){
            model.addAttribute("servicioPasajeros", sp);
            return "servicioPasajerosEditar";
        } else {
            return "redirect:/servicios-pasajeros";
        }
    }

    @PostMapping("/servicios-pasajeros/{id}/edit/save")
    public String servicioPasajerosEditarGuardar(@PathVariable("id") long id, @ModelAttribute Servicio_Pasajeros sp){
        servicioPasajerosRepository.actualizarServicioPasajeros(
            id,
            sp.getCantidadPasajeros()
        );
        return "redirect:/servicios-pasajeros";
    }

    @GetMapping("/servicios-pasajeros/{id}/delete")
    public String servicioPasajerosEliminar(@PathVariable("id") long id){
        servicioPasajerosRepository.eliminarServicioPasajeros(id);
        return "redirect:/servicios-pasajeros";
    }
}
