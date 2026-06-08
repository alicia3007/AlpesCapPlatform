package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.MapaRepository;
import uniandes.edu.co.proyecto.modelo.Mapa;

@Controller
public class MapaController {
    
    @Autowired
    private MapaRepository mapaRepository;

    @GetMapping("/mapas")
    public String mapas(Model model){
        model.addAttribute("mapas", mapaRepository.darMapas());
        return "mapas";
    }

    @GetMapping("/mapas/new")
    public String mapaFrom(Model model){
        model.addAttribute("mapa", new Mapa());
        return "mapaNuevo";
    }

    @PostMapping("/mapas/new/save")
    public String mapaGuardar(@ModelAttribute Mapa mapa){
        Long idCiudad = mapa.getCiudad() != null ? mapa.getCiudad().getIdCiudad() : null;
        Long idPPartida = mapa.getPuntoPartida() != null ? mapa.getPuntoPartida().getIdPunto() : null;
        Long idPLlegada = mapa.getPuntoLlegada() != null ? mapa.getPuntoLlegada().getIdPunto() : null;
        mapaRepository.insertarMapa(
            idCiudad,
            idPPartida,
            idPLlegada,
            mapa.getDireccionAprox()
        );
        return "redirect:/mapas";
    }

    @GetMapping("/mapas/{id}/edit")
    public String mapaEditarForm(@PathVariable("id") long id, Model model){
        Mapa mapa = mapaRepository.darMapa(id);
        if (mapa != null){
            model.addAttribute("mapa", mapa);
            return "mapaEditar";
        } else {
            return "redirect:/mapas";
        }
    }

    @PostMapping("/mapas/{id}/edit/save")
    public String mapaEditarGuardar(@PathVariable("id") long id, @ModelAttribute Mapa mapa){
        Long idCiudad = mapa.getCiudad() != null ? mapa.getCiudad().getIdCiudad() : null;
        Long idPPartida = mapa.getPuntoPartida() != null ? mapa.getPuntoPartida().getIdPunto() : null;
        Long idPLlegada = mapa.getPuntoLlegada() != null ? mapa.getPuntoLlegada().getIdPunto() : null;
        mapaRepository.actualizarMapa(
            id,
            idCiudad,
            idPPartida,
            idPLlegada,
            mapa.getDireccionAprox()
        );
        return "redirect:/mapas";
    }

    @GetMapping("/mapas/{id}/delete")
    public String mapaEliminar(@PathVariable("id") long id){
        mapaRepository.eliminarMapa(id);
        return "redirect:/mapas";
    }
}
