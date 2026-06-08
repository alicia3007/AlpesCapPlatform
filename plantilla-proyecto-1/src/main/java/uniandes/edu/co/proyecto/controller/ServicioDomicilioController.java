package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ServicioDomicilioRepository;
import uniandes.edu.co.proyecto.modelo.Servicio_Domicilio;

@Controller
public class ServicioDomicilioController {
    
     @Autowired
    private ServicioDomicilioRepository servicioDomicilioRepository;

    @GetMapping("/servicios-domicilio")
    public String serviciosDomicilio(Model model){
        model.addAttribute("serviciosDomicilio", servicioDomicilioRepository.darServiciosDomicilio());
        return "serviciosDomicilio";
    }

    @GetMapping("/servicios-domicilio/new")
    public String servicioDomicilioFrom(Model model){
        model.addAttribute("servicioDomicilio", new Servicio_Domicilio());
        return "servicioDomicilioNuevo";
    }

    @PostMapping("/servicios-domicilio/new/save")
    public String servicioDomicilioGuardar(@ModelAttribute Servicio_Domicilio sd){
        Long idServicio = sd.getServicio() != null ? sd.getServicio().getIdServicio() : null;
        Long idRestaurante = sd.getRestaurante() != null ? sd.getRestaurante().getIdRestaurante() : null;
        servicioDomicilioRepository.insertarServicioDomicilio(
            idServicio,
            idRestaurante,
            sd.getValorOrden(),
            sd.getCantidadProductos()
        );
        return "redirect:/servicios-domicilio";
    }

    @GetMapping("/servicios-domicilio/{id}/edit")
    public String servicioDomicilioEditarForm(@PathVariable("id") long id, Model model){
        Servicio_Domicilio sd = servicioDomicilioRepository.darServicioDomicilio(id);
        if (sd != null){
            model.addAttribute("servicioDomicilio", sd);
            return "servicioDomicilioEditar";
        } else {
            return "redirect:/servicios-domicilio";
        }
    }

    @PostMapping("/servicios-domicilio/{id}/edit/save")
    public String servicioDomicilioEditarGuardar(@PathVariable("id") long id, @ModelAttribute Servicio_Domicilio sd){
        Long idRestaurante = sd.getRestaurante() != null ? sd.getRestaurante().getIdRestaurante() : null;
        servicioDomicilioRepository.actualizarServicioDomicilio(
            id,
            idRestaurante,
            sd.getValorOrden(),
            sd.getCantidadProductos()
        );
        return "redirect:/servicios-domicilio";
    }

    @GetMapping("/servicios-domicilio/{id}/delete")
    public String servicioDomicilioEliminar(@PathVariable("id") long id){
        servicioDomicilioRepository.eliminarServicioDomicilio(id);
        return "redirect:/servicios-domicilio";
    }
}
