package uniandes.edu.co.proyecto.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uniandes.edu.co.proyecto.modelo.ServicioMongo;

import java.util.List;

public interface ServicioMongoRepository extends MongoRepository<ServicioMongo, String> {
    List<ServicioMongo> findByConductorIdAndEstadoIn(String conductorId, List<String> estados);
}
