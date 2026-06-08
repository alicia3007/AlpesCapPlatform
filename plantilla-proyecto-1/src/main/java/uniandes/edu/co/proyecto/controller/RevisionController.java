package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.Repositories.RevisionRepository;
import uniandes.edu.co.proyecto.modelo.Revision;

@Controller
public class RevisionController {
    
    private static final String REVISIONES_VIEW = "revisiones";
    private static final String REVISIONES_ATTR = "revisiones";
    private static final String REDIRECT_REVISIONES = "redirect:/revisiones";
    private static final String TIPO_BUSQUEDA_ATTR = "tipoBusqueda";
    private static final String ID_BUSQUEDA_ATTR = "idBusqueda";

    private final RevisionRepository revisionRepository;

    public RevisionController(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    @GetMapping("/revisiones")
    public String revisiones(Model model){
        model.addAttribute(REVISIONES_ATTR, revisionRepository.darRevisiones());
        return REVISIONES_VIEW;
    }

    @GetMapping("/revisiones/conductor/{id}")
    public String revisionesPorConductor(@PathVariable("id") Long id, Model model) {
        model.addAttribute(REVISIONES_ATTR, revisionRepository.darRevisionesPorConductor(id));
        model.addAttribute(TIPO_BUSQUEDA_ATTR, "conductor");
        model.addAttribute(ID_BUSQUEDA_ATTR, id);
        return REVISIONES_VIEW;
    }

    @GetMapping("/revisiones/servicio/{id}")
    public String revisionesPorServicio(@PathVariable("id") Long id, Model model) {
        model.addAttribute(REVISIONES_ATTR, revisionRepository.darRevisionesPorServicio(id));
        model.addAttribute(TIPO_BUSQUEDA_ATTR, "servicio");
        model.addAttribute(ID_BUSQUEDA_ATTR, id);
        return REVISIONES_VIEW;
    }

    @GetMapping("/revisiones/cliente/{id}")
    public String revisionesPorCliente(@PathVariable("id") Long id, Model model) {
        model.addAttribute(REVISIONES_ATTR, revisionRepository.darRevisionesPorCliente(id));
        model.addAttribute(TIPO_BUSQUEDA_ATTR, "cliente");
        model.addAttribute(ID_BUSQUEDA_ATTR, id);
        return REVISIONES_VIEW;
    }

    @GetMapping("/revisiones/new")
    public String revisionFrom(Model model){
        model.addAttribute("revision", new Revision());
        return "revisionNuevo";
    }

    @PostMapping("/revisiones/new/save")
    public String revisionGuardar(@ModelAttribute Revision revision){
        Long idServicio = revision.getServicio() != null ? revision.getServicio().getIdServicio() : null;
        Long idRevisor  = revision.getRevisorUsuario() != null ? revision.getRevisorUsuario().getIdUsuario() : null;
        Long idReceptor = revision.getReceptorUsuario() != null ? revision.getReceptorUsuario().getIdUsuario() : null;
        revisionRepository.insertarRevision(
            idServicio,
            idRevisor,
            idReceptor,
            revision.getCalificacion(),
            revision.getComentario()
        );
        return REDIRECT_REVISIONES;
    }

    @GetMapping("/revisiones/{id}/edit")
    public String revisionEditarForm(@PathVariable("id") long id, Model model){
        Revision revision = revisionRepository.darRevision(id);
        if (revision != null){
            model.addAttribute("revision", revision);
            return "revisionEditar";
        } else {
            return "redirect:/revisiones";
        }
    }

    @PostMapping("/revisiones/{id}/edit/save")
    public String revisionEditarGuardar(@PathVariable("id") long id, @ModelAttribute Revision revision){
        Long idServicio = revision.getServicio() != null ? revision.getServicio().getIdServicio() : null;
        Long idRevisor  = revision.getRevisorUsuario() != null ? revision.getRevisorUsuario().getIdUsuario() : null;
        Long idReceptor = revision.getReceptorUsuario() != null ? revision.getReceptorUsuario().getIdUsuario() : null;
        revisionRepository.actualizarRevision(
            id,
            idServicio,
            idRevisor,
            idReceptor,
            revision.getCalificacion(),
            revision.getComentario()
        );
        return "redirect:/revisiones";
    }

    @GetMapping("/revisiones/{id}/delete")
    public String revisionEliminar(@PathVariable("id") long id){
        revisionRepository.eliminarRevision(id);
        return "redirect:/revisiones";
    }
}
