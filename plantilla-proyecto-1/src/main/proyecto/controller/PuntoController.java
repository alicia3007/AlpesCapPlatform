package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uniandes.edu.co.proyecto.Repositories.CiudadRepository;
import uniandes.edu.co.proyecto.Repositories.PuntoRepository;
import uniandes.edu.co.proyecto.modelo.Punto;

@Controller
public class PuntoController {
    
    @Autowired
    private PuntoRepository puntoRepository;

    @Autowired
    private CiudadRepository ciudadRepository; 

    @GetMapping("/puntos")
    public String puntos(Model model){
        model.addAttribute("puntos", puntoRepository.darPuntos());
        return "puntos";
    }

    

    @GetMapping("/puntos/new")
    public String puntoForm(Model model) {
        model.addAttribute("punto", new Punto());
        model.addAttribute("ciudades", ciudadRepository.findAll()); 
        return "puntoNuevo";
    }

    @PostMapping("/puntos/new/save")
    public String puntoGuardar(
        @RequestParam("nombre") String nombre,
        @RequestParam("direccion") String direccion,
        @RequestParam("latitud") double latitud,
        @RequestParam("longitud") double longitud,
        @RequestParam("idCiudad") Long idCiudad) {

    puntoRepository.insertarPunto(nombre, direccion, latitud, longitud, idCiudad);
    return "redirect:/puntos";
}

    @GetMapping("/puntos/{id}/edit")
    public String puntoEditarForm(@PathVariable("id") long id, Model model){
        Punto punto = puntoRepository.darPunto(id);
        if (punto != null){
            model.addAttribute("punto", punto);
            return "puntoEditar";
        } else {
            return "redirect:/puntos";
        }
    }

    @PostMapping("/puntos/{id}/edit/save")
    public String puntoEditarGuardar(@PathVariable("id") long id, @ModelAttribute Punto punto){
        Long idCiudad = punto.getCiudad() != null ? punto.getCiudad().getIdCiudad() : null;
        puntoRepository.actualizarPunto(
            id,
            punto.getNombre(),
            punto.getDireccion(),
            punto.getLatitud(),
            punto.getLongitud(),
            idCiudad
        );
        return "redirect:/puntos";
    }

    @GetMapping("/puntos/{id}/delete")
    public String puntoEliminar(@PathVariable("id") long id){
        puntoRepository.eliminarPunto(id);
        return "redirect:/puntos";
    }
}
