package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.HistorialRepository;
import uniandes.edu.co.proyecto.modelo.Historial;

@Controller
public class HistorialController {
    
    @Autowired
    private HistorialRepository historialRepository;

    @GetMapping("/historiales")
    public String historiales(Model model){
        model.addAttribute("historiales", historialRepository.darHistoriales());
        return "historiales";
    }

    @GetMapping("/historiales/new")
    public String historialFrom(Model model){
        model.addAttribute("historial", new Historial());
        return "historialNuevo";
    }

    @PostMapping("/historiales/new/save")
    public String historialGuardar(@ModelAttribute Historial historial){
        Long idServicio   = historial.getServicio()   != null ? historial.getServicio().getIdServicio()     : null;
        Long idCliente    = historial.getCliente()    != null ? historial.getCliente().getIdUsuario()        : null;
        Long idConductor  = historial.getConductor()  != null ? historial.getConductor().getIdUsuario()      : null;
        Long idPPartida   = historial.getPuntoPartida()!= null ? historial.getPuntoPartida().getIdPunto()    : null;
        Long idPLlegada   = historial.getPuntoLlegada()!= null ? historial.getPuntoLlegada().getIdPunto()    : null;

        historialRepository.insertarHistorial(
            idServicio, idCliente, idConductor, idPPartida, idPLlegada
        );
        return "redirect:/historiales";
    }

    @GetMapping("/historiales/{id}/edit")
    public String historialEditarForm(@PathVariable("id") long id, Model model){
        Historial historial = historialRepository.darHistorial(id);
        if (historial != null){
            model.addAttribute("historial", historial);
            return "historialEditar";
        } else {
            return "redirect:/historiales";
        }
    }

    @PostMapping("/historiales/{id}/edit/save")
    public String historialEditarGuardar(@PathVariable("id") long id, @ModelAttribute Historial historial){
        Long idServicio   = historial.getServicio()   != null ? historial.getServicio().getIdServicio()     : null;
        Long idCliente    = historial.getCliente()    != null ? historial.getCliente().getIdUsuario()        : null;
        Long idConductor  = historial.getConductor()  != null ? historial.getConductor().getIdUsuario()      : null;
        Long idPPartida   = historial.getPuntoPartida()!= null ? historial.getPuntoPartida().getIdPunto()    : null;
        Long idPLlegada   = historial.getPuntoLlegada()!= null ? historial.getPuntoLlegada().getIdPunto()    : null;

        historialRepository.actualizarHistorial(
            id, idServicio, idCliente, idConductor, idPPartida, idPLlegada
        );
        return "redirect:/historiales";
    }

    @GetMapping("/historiales/{id}/delete")
    public String historialEliminar(@PathVariable("id") long id){
        historialRepository.eliminarHistorial(id);
        return "redirect:/historiales";
    }
}
