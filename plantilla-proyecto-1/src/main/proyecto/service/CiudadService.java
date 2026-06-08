package uniandes.edu.co.proyecto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.Repositories.CiudadRepository;
import uniandes.edu.co.proyecto.modelo.Ciudad;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Transactional
    public Ciudad registrarCiudad(String nombre) {
        // RF1: Registrar una ciudad
        ciudadRepository.insertarCiudad(nombre);
        // Retornar la ciudad recién creada
        return ciudadRepository.darCiudadPorNombre(nombre);
    }

    @Transactional(readOnly = true)
    public boolean existeCiudad(String nombre) {
        return ciudadRepository.existeCiudadPorNombre(nombre);
    }
}