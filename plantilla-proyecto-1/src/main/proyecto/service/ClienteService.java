package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.Repositories.ClienteRepository;
import uniandes.edu.co.proyecto.modelo.Cliente;
import uniandes.edu.co.proyecto.exception.NegocioException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente registrarCliente(String nombre, Long cedula, String correo, Long celular) {
        // RF2: Registrar un usuario de servicios
        if (clienteRepository.existeClientePorCedula(cedula) > 0) {
            throw new NegocioException("Ya existe un usuario con esta cédula");
        }
        
        if (clienteRepository.existeClientePorCorreo(correo) > 0) {
            throw new NegocioException("Ya existe un usuario con este correo");
        }

        clienteRepository.insertarUsuario(nombre, correo, celular, cedula);
        clienteRepository.insertarClientePorCedula(cedula);
        Cliente cliente = clienteRepository.darClientePorCedula(cedula);
        if (cliente == null) {
            throw new NegocioException("Error al registrar el cliente");
        }
        return cliente;
    }

    @Transactional(readOnly = true)
    public boolean validarCliente(Long cedula) {
        return clienteRepository.existeClientePorCedula(cedula) > 0;
    }
}