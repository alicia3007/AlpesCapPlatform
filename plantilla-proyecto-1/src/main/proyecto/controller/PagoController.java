package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.PagoRepository;
import uniandes.edu.co.proyecto.modelo.Pago;

@Controller
public class PagoController {
    
    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping("/pagos")
    public String pagos(Model model){
        model.addAttribute("pagos", pagoRepository.darPagos());
        return "pagos";
    }

    @GetMapping("/pagos/new")
    public String pagoFrom(Model model){
        model.addAttribute("pago", new Pago());
        return "pagoNuevo";
    }

    @PostMapping("/pagos/new/save")
    public String pagoGuardar(@ModelAttribute Pago pago){
        Long idServicio = pago.getServicio() != null ? pago.getServicio().getIdServicio() : null;
        Long idTarjeta  = pago.getTarjeta()  != null ? pago.getTarjeta().getIdTarjeta()  : null;
        pagoRepository.insertarPago(
            idServicio,
            idTarjeta,
            pago.getMonto(),
            pago.getFechaPago()
        );
        return "redirect:/pagos";
    }

    @GetMapping("/pagos/{id}/edit")
    public String pagoEditarForm(@PathVariable("id") long id, Model model){
        Pago pago = pagoRepository.darPago(id);
        if (pago != null){
            model.addAttribute("pago", pago);
            return "pagoEditar";
        } else {
            return "redirect:/pagos";
        }
    }

    @PostMapping("/pagos/{id}/edit/save")
    public String pagoEditarGuardar(@PathVariable("id") long id, @ModelAttribute Pago pago){
        Long idServicio = pago.getServicio() != null ? pago.getServicio().getIdServicio() : null;
        Long idTarjeta  = pago.getTarjeta()  != null ? pago.getTarjeta().getIdTarjeta()  : null;
        pagoRepository.actualizarPago(
            id,
            idServicio,
            idTarjeta,
            pago.getMonto(),
            pago.getFechaPago()
        );
        return "redirect:/pagos";
    }

    @GetMapping("/pagos/{id}/delete")
    public String pagoEliminar(@PathVariable("id") long id){
        pagoRepository.eliminarPago(id);
        return "redirect:/pagos";
    }
}
