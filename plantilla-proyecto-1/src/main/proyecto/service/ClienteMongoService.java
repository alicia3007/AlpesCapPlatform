package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import uniandes.edu.co.proyecto.Repositories.ClienteMongoRepository;
import uniandes.edu.co.proyecto.modelo.ClienteMongo;

@Service
public class ClienteMongoService {

    private final ClienteMongoRepository clienteMongoRepository;

    public ClienteMongoService(ClienteMongoRepository clienteMongoRepository) {
        this.clienteMongoRepository = clienteMongoRepository;
    }

    public ClienteMongo registrarCliente(ClienteMongo cliente) {
        if (cliente.getCedula() != null && clienteMongoRepository.existsByCedula(cliente.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con esta cédula");
        }
        if (cliente.getCorreo() != null && clienteMongoRepository.existsByCorreo(cliente.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con este correo");
        }
        // Asegurarse de que el campo 'tipo' esté definido y no sea cadena vacía
        if (cliente.getTipo() == null || cliente.getTipo().trim().isEmpty()) {
            cliente.setTipo("cliente");
        }
        return clienteMongoRepository.save(cliente);
    }
}
