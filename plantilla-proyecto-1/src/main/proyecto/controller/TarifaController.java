package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.TarifaRepository;
import uniandes.edu.co.proyecto.modelo.Tarifa;

@Controller
public class TarifaController {
    
    @Autowired
    private TarifaRepository tarifaRepository;

    @GetMapping("/tarifas")
    public String tarifas(Model model){
        model.addAttribute("tarifas", tarifaRepository.darTarifas());
        return "tarifas";
    }
    
    @GetMapping("/tarifas/new")
    public String tarifaFrom(Model model){
        model.addAttribute("tarifa", new Tarifa());
        return "tarifaNueva";
    }
    
    @PostMapping("/tarifas/new/save")
    public String tarifaGuardar(@ModelAttribute Tarifa tarifa){
        tarifaRepository.insertarTarifa(tarifa.getTipoServicio(), tarifa.getNivel(),tarifa.getValorKm());
        return "redirect:/tarifas";
    }

    @GetMapping("tarifas/{id}/edit")
    public String tarifaEditarForm(@PathVariable("id") long id, Model model){
        Tarifa tarifa = tarifaRepository.darTarifa(id);
        if (tarifa != null){
            model.addAttribute("tarifa", tarifa);
            return "tarifaEditar";
        } else {
            return "redirect:/tarifas";
        }
    }

    @PostMapping("/tarifas/{id}/edit/save")
    public String tarifaEditarGuardar(@PathVariable("id") long id, @ModelAttribute Tarifa tarifa){
        tarifaRepository.actualizarTarifa(id,tarifa.getTipoServicio(), tarifa.getNivel(),tarifa.getValorKm());
        return "redirect:/tarifas";
    }

    @GetMapping("/tarifas/{id}/delete")
    public String tarifaEliminar(@PathVariable("id") long id){
        tarifaRepository.eliminarTarifa(id);
        return "redirect:/tarifas";
    }
}
