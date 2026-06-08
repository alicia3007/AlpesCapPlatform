package uniandes.edu.co.proyecto.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uniandes.edu.co.proyecto.modelo.ConductorMongo;

import java.util.Optional;

public interface ConductorMongoRepository extends MongoRepository<ConductorMongo, String> {
    boolean existsByCedula(String cedula);
    boolean existsByCorreo(String correo);
    Optional<ConductorMongo> findByCedula(String cedula);
}
