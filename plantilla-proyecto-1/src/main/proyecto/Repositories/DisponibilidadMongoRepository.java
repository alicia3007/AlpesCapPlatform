package uniandes.edu.co.proyecto.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uniandes.edu.co.proyecto.modelo.DisponibilidadMongo;

import java.util.List;

public interface DisponibilidadMongoRepository extends MongoRepository<DisponibilidadMongo, String> {
    List<DisponibilidadMongo> findByConductorIdAndDiaSemana(String conductorId, String diaSemana);
}
