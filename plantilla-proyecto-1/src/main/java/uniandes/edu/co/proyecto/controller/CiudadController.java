package uniandes.edu.co.proyecto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.CiudadRepository;
import uniandes.edu.co.proyecto.modelo.Ciudad;

@Controller
public class CiudadController {
    @Autowired
    private CiudadRepository ciudadRepository;

    @GetMapping("/ciudades")
    public String ciudades(Model model){
        model.addAttribute("ciudades", ciudadRepository.darCiudades());
        return "ciudades";
    }
    
    @GetMapping("/ciudades/new")
    public String ciudadFrom(Model model){
        model.addAttribute("ciudad", new Ciudad());
        return "ciudadNuevo";
    }

    @PostMapping("/ciudades/new/save")
    public String ciudadGuardar(@ModelAttribute Ciudad ciudad){
        ciudadRepository.insertarCiudad(ciudad.getNombre());
        return "redirect:/ciudades";
    }

    @GetMapping("/ciudades/{id}/edit")
    public String ciudadEditarForm(@PathVariable("id") long id, Model model){
        Ciudad ciudad = ciudadRepository.darCiudad(id);
        if (ciudad != null){
            model.addAttribute("ciudad", ciudad);
            return "ciudadEditar";
        } else {
            return "redirect:/ciudades";
        }
    }

    @PostMapping("/ciudades/{id}/edit/save")
    public String ciudadEditarGuardar(@PathVariable("id") long id, @ModelAttribute Ciudad ciudad){
        ciudadRepository.actualizarCiudad(id, ciudad.getNombre());
        return "redirect:/ciudades";
    }

    @GetMapping("/ciudades/{id}/delete")
    public String ciudadEliminar(@PathVariable("id") long id){
        ciudadRepository.eliminarCiudad(id);
        return "redirect:/ciudades";
    }

}
