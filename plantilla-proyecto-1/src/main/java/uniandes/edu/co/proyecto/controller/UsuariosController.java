package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import uniandes.edu.co.proyecto.Repositories.UsuarioRepository;
import uniandes.edu.co.proyecto.modelo.Usuario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UsuariosController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public String usuarios(Model model){
        model.addAttribute("usuarios", usuarioRepository.darUsuarios());
        return "usuarios";
    }
    
    @GetMapping("/usuarios/new")
    public String usuarioFrom(Model model){
        model.addAttribute("usuario", new Usuario());
        return "usuarioNuevo";
    }
    
    @PostMapping("/usuarios/new/save")
    public String usuarioGuardar(@ModelAttribute Usuario usuario){
        usuarioRepository.insertarUsuario(usuario.getNombre(),usuario.getCorreo(),usuario.getCelular(),usuario.getCedula());
        return "redirect:/usuarios";
    }

    @GetMapping("usuarios/{id}/edit")
    public String usuarioEditarForm(@PathVariable("id") long id, Model model){
        Usuario usuario = usuarioRepository.darUsuario(id);
        if (usuario != null){
            model.addAttribute("usuario", usuario);
            return "usuarioEditar";
        } else {
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/usuarios/{id}/edit/save")
    public String usuarioEditarGuardar(@PathVariable("id") long id, @ModelAttribute Usuario usuario){
        usuarioRepository.actualizarUsuario(id,usuario.getNombre(),usuario.getCorreo(),usuario.getCelular(),usuario.getCedula());
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/{id}/delete")
    public String usuarioEliminar(@PathVariable("id") long id){
        usuarioRepository.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}
