package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ActividadRepository;
import uniandes.edu.co.proyecto.modelo.Actividad;

@Controller
public class ActividadController {
    
    @Autowired
    private ActividadRepository actividadRepository;

    @GetMapping("/actividades")
    public String actividades(Model model){
        model.addAttribute("actividades", actividadRepository.darActividades());
        return "actividades";
    }

    @GetMapping("/actividades/new")
    public String actividadFrom(Model model){
        model.addAttribute("actividad", new Actividad());
        return "actividadNuevo";
    }

    @PostMapping("/actividades/new/save")
    public String actividadGuardar(@ModelAttribute Actividad actividad){
        actividadRepository.insertarActividad(
            actividad.getDiaSemana(),
            actividad.getFranjasHorarias(),
            actividad.getTipoServicio(),
            actividad.getHoraInicio(),
            actividad.getHoraFin(),
            actividad.getVehiculo() != null ? actividad.getVehiculo().getIdVehiculo() : null,
            actividad.getConductor() != null ? actividad.getConductor().getIdUsuario() : null
        );
        return "redirect:/actividades";
    }

    @GetMapping("actividades/{id}/edit")
    public String actividadEditarForm(@PathVariable("id") long id, Model model){
        Actividad actividad = actividadRepository.darActividad(id);
        if (actividad != null){
            model.addAttribute("actividad", actividad);
            return "actividadEditar";
        } else {
            return "redirect:/actividades";
        }
    }

    @PostMapping("/actividades/{id}/edit/save")
    public String actividadEditarGuardar(@PathVariable("id") long id, @ModelAttribute Actividad actividad){
        actividadRepository.actualizarActividad(
            id,
            actividad.getDiaSemana(),
            actividad.getFranjasHorarias(),
            actividad.getTipoServicio(),
            actividad.getHoraInicio(),
            actividad.getHoraFin(),
            actividad.getVehiculo() != null ? actividad.getVehiculo().getIdVehiculo() : null,
            actividad.getConductor() != null ? actividad.getConductor().getIdUsuario() : null
        );
        return "redirect:/actividades";
    }

    @GetMapping("/actividades/{id}/delete")
    public String actividadEliminar(@PathVariable("id") long id){
        actividadRepository.eliminarActividad(id);
        return "redirect:/actividades";
    }
}




