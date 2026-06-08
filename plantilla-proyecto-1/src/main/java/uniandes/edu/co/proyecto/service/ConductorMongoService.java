package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;
import uniandes.edu.co.proyecto.modelo.ConductorMongo;

@Service
public class ConductorMongoService {

    private final ConductorMongoRepository conductorMongoRepository;

    public ConductorMongoService(ConductorMongoRepository conductorMongoRepository) {
        this.conductorMongoRepository = conductorMongoRepository;
    }

    public ConductorMongo registrarConductor(ConductorMongo conductor) {
        if (conductor.getCedula() != null && conductorMongoRepository.existsByCedula(conductor.getCedula())) {
            throw new IllegalArgumentException("Ya existe un conductor con esta cédula");
        }
        if (conductor.getCorreo() != null && conductorMongoRepository.existsByCorreo(conductor.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un conductor con este correo");
        }

        if (conductor.getTipo() == null || conductor.getTipo().trim().isEmpty()) {
            conductor.setTipo("conductor");
        }

        // Normalizar valores numéricos si vienen vacíos
        if (conductor.getTotalComisiones() == null) conductor.setTotalComisiones(0L);
        if (conductor.getCalificacionPromedio() == null) conductor.setCalificacionPromedio(0.0);
        if (conductor.getTotalRevisiones() == null) conductor.setTotalRevisiones(0);

        return conductorMongoRepository.save(conductor);
    }
}
