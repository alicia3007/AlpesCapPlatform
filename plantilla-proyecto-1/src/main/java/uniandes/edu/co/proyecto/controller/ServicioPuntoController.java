package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ServicioPuntoRepository;
import uniandes.edu.co.proyecto.modelo.Servicio_Punto;

@Controller
public class ServicioPuntoController {
    
    @Autowired
    private ServicioPuntoRepository servicioPuntoRepository;

    @GetMapping("/servicio-puntos")
    public String servicioPuntos(Model model){
        model.addAttribute("servicioPuntos", servicioPuntoRepository.darServicioPuntos());
        return "servicioPuntos";
    }

    @GetMapping("/servicio-puntos/new")
    public String servicioPuntoFrom(Model model){
        model.addAttribute("servicioPunto", new Servicio_Punto());
        return "servicioPuntoNuevo";
    }

    @PostMapping("/servicio-puntos/new/save")
    public String servicioPuntoGuardar(@ModelAttribute Servicio_Punto sp){
        Long idServicio = sp.getServicio() != null ? sp.getServicio().getIdServicio() : null;
        Long idPunto = sp.getPunto() != null ? sp.getPunto().getIdPunto() : null;
        servicioPuntoRepository.insertarServicioPunto(
            idServicio,
            sp.getId().getRol(),
            sp.getId().getOrden(),
            idPunto
        );
        return "redirect:/servicio-puntos";
    }

    @GetMapping("/servicio-puntos/{idServicio}/{rol}/{orden}/edit")
    public String servicioPuntoEditarForm(@PathVariable("idServicio") long idServicio,
                                          @PathVariable("rol") String rol,
                                          @PathVariable("orden") int orden,
                                          Model model){
        Servicio_Punto sp = servicioPuntoRepository.darServicioPunto(idServicio, rol, orden);
        if (sp != null){
            model.addAttribute("servicioPunto", sp);
            return "servicioPuntoEditar";
        } else {
            return "redirect:/servicio-puntos";
        }
    }

    @PostMapping("/servicio-puntos/{idServicio}/{rol}/{orden}/edit/save")
    public String servicioPuntoEditarGuardar(@PathVariable("idServicio") long idServicio,
                                             @PathVariable("rol") String rol,
                                             @PathVariable("orden") int orden,
                                             @ModelAttribute Servicio_Punto sp){
        Long idPunto = sp.getPunto() != null ? sp.getPunto().getIdPunto() : null;
        servicioPuntoRepository.actualizarServicioPunto(
            idServicio,
            rol,
            orden,
            idPunto
        );
        return "redirect:/servicio-puntos";
    }

    @GetMapping("/servicio-puntos/{idServicio}/{rol}/{orden}/delete")
    public String servicioPuntoEliminar(@PathVariable("idServicio") long idServicio,
                                        @PathVariable("rol") String rol,
                                        @PathVariable("orden") int orden){
        servicioPuntoRepository.eliminarServicioPunto(idServicio, rol, orden);
        return "redirect:/servicio-puntos";
    }
}
