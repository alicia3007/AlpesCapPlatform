package uniandes.edu.co.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.ConductorRepository;
import uniandes.edu.co.proyecto.Repositories.VehiculoRepository;
import uniandes.edu.co.proyecto.modelo.Vehiculo;
import uniandes.edu.co.proyecto.dto.VehiculoDTO;

@Controller
public class VehiculosController {
    
    private static final String VEHICULO_ATTR = "vehiculo";
    private static final String REDIRECT_VEHICULOS = "redirect:/vehiculos";
    
    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;

    public VehiculosController(VehiculoRepository vehiculoRepository, ConductorRepository conductorRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
    }

    @GetMapping("/vehiculos")
    public String vehiculos(Model model){
        model.addAttribute("vehiculos", vehiculoRepository.darVehiculos());
        model.addAttribute(VEHICULO_ATTR, new Vehiculo());
        model.addAttribute("conductores", conductorRepository.darConductores());
        return "vehiculos"; 
    }
    
    @GetMapping("/vehiculos/new")
    public String vehiculoFrom(Model model){
        model.addAttribute(VEHICULO_ATTR, new Vehiculo());
        return "vehiculoNuevo";
    }
    
    @PostMapping("/vehiculos/new/save")
    public String vehiculoGuardar(
        @ModelAttribute Vehiculo vehiculo,
        @org.springframework.web.bind.annotation.RequestParam(name = "idConductor", required = false) Long idConductor) {

    VehiculoDTO vehiculoDTO = new VehiculoDTO(
        vehiculo.getTipo(),
        vehiculo.getMarca(),
        vehiculo.getModelo(),
        vehiculo.getColor(),
        vehiculo.getPlaca(),
        vehiculo.getCiudadPlaca(),
        vehiculo.getCapacidad(),
        vehiculo.getNivel(),
        idConductor
    );
    vehiculoRepository.insertarVehiculo(vehiculoDTO);

    return REDIRECT_VEHICULOS;
}


    @GetMapping("vehiculos/{id}/edit")
    public String vehiculoEditarForm(@PathVariable("id") long id, Model model){
        Vehiculo vehiculo = vehiculoRepository.darVehiculo(id);
        if (vehiculo != null){
            model.addAttribute(VEHICULO_ATTR, vehiculo);
            return "vehicuoEditar";
        } else {
            return REDIRECT_VEHICULOS;
        }
    }

    @PostMapping("/vehiculos/{id}/edit/save")
    public String vehiculoEditarGuardar(@PathVariable("id") long id, @ModelAttribute Vehiculo vehiculo) {
    VehiculoDTO vehiculoDTO = new VehiculoDTO(
        vehiculo.getTipo(),
        vehiculo.getMarca(),
        vehiculo.getModelo(),
        vehiculo.getColor(),
        vehiculo.getPlaca(),
        vehiculo.getCiudadPlaca(),
        vehiculo.getCapacidad(),
        vehiculo.getNivel(),
        vehiculo.getConductor().getIdUsuario()
    );
    vehiculoRepository.actualizarVehiculo(id, vehiculoDTO);
    return REDIRECT_VEHICULOS;
}

    @GetMapping("/vehiculos/{id}/delete")
    public String vehiculoEliminar(@PathVariable("id") long id){
        vehiculoRepository.eliminarVehiculo(id);
        return REDIRECT_VEHICULOS;
    }

}
