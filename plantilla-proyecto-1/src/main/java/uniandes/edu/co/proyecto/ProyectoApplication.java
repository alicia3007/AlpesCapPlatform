package uniandes.edu.co.proyecto;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uniandes.edu.co.proyecto.Repositories.UsuarioRepository;

@SpringBootApplication
public class ProyectoApplication {

	@Autowired
	private UsuarioRepository usuarioRepository;
	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

	// @Override
	// public void run(String ... arg){
	// 	Collection<uniandes.edu.co.proyecto.modelo.Usuario> usuarios = usuarioRepository.darUsuarios();
	// 	for(uniandes.edu.co.proyecto.modelo.Usuario u : usuarios){
	// 		System.out.println(u);
	// 	}
	// }

}
