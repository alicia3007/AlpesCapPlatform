package uniandes.edu.co.proyecto.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uniandes.edu.co.proyecto.modelo.ViajeMongo;

public interface ViajeMongoRepository extends MongoRepository<ViajeMongo, String> {
}
