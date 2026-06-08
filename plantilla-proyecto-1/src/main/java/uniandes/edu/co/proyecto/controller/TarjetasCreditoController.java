package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.TarjetaCreditoRepository;
import uniandes.edu.co.proyecto.modelo.TarjetaCredito;

@Controller
public class TarjetasCreditoController {

    @Autowired
    private TarjetaCreditoRepository tarjetacreditoRepository;

    @GetMapping("/tarjetascredito")
    public String tarjetascredito(Model model){
        model.addAttribute("tarjetascredito", tarjetacreditoRepository.darTarjetasCredito());
        return "tarjetascredito";
    }
    
    @GetMapping("/tarjetascredito/new")
    public String tarjetascreditoFrom(Model model){
        model.addAttribute("tarjetacredito", new TarjetaCredito());
        return "tarjetacreditoNueva";
    }
    
    @PostMapping("/tarjetascredito/new/save")
    public String tarjetacreditoGuardar(@ModelAttribute TarjetaCredito tarjetacredito){
        tarjetacreditoRepository.insertarTarjetaCredito(tarjetacredito.getNumero(),tarjetacredito.getNombreTarjeta(),tarjetacredito.getFechaVencimiento(), tarjetacredito.getCodigo());
        return "redirect:/tarjetascredito";
    }

    @GetMapping("tarjetascredito/{id}/edit")
    public String tarjetacreditoEditarForm(@PathVariable("id") long id, Model model){
        TarjetaCredito tarjetacredito = tarjetacreditoRepository.darTarjetaCredito(id);
        if (tarjetacredito != null){
            model.addAttribute("tarjetacredito", tarjetacredito);
            return "tarjetacreditoEditar";
        } else {
            return "redirect:/tarjetascredito";
        }
    }

    @PostMapping("/tarjetascredito/{id}/edit/save")
    public String tarjetacreditoEditarGuardar(@PathVariable("id") long id, @ModelAttribute TarjetaCredito tarjetacredito){
        tarjetacreditoRepository.actualizarTarjetaCredito(id,tarjetacredito.getNumero(),tarjetacredito.getNombreTarjeta(),tarjetacredito.getFechaVencimiento(), tarjetacredito.getCodigo());
        return "redirect:/tarjetascredito";
    }

    @GetMapping("/tarjetascredito/{id}/delete")
    public String tarjetacreditoEliminar(@PathVariable("id") long id){
        tarjetacreditoRepository.eliminarTarjetaCredito(id);
        return "redirect:/tarjetascredito";
    }
}
