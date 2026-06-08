package uniandes.edu.co.proyecto.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uniandes.edu.co.proyecto.modelo.ClienteMongo;

import java.util.Optional;

public interface ClienteMongoRepository extends MongoRepository<ClienteMongo, String> {
    boolean existsByCedula(String cedula);
    boolean existsByCorreo(String correo);
    Optional<ClienteMongo> findByCedula(String cedula);
}
