package uniandes.edu.co.proyecto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import uniandes.edu.co.proyecto.Repositories.ServicioRepository;

@Service
public class HistorialServiciosService {

    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Ejecuta RFC1 (histórico de servicios por usuario) en nivel READ_COMMITTED
     * realizando la consulta dos veces con una espera de 30 segundos para pruebas de concurrencia.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ResultadoDobleConsulta consultarHistorialReadCommitted(Long idUsuario) {
        List<Object[]> antes = servicioRepository.consultarHistorialServicios(idUsuario);
        esperar30s();
        List<Object[]> despues = servicioRepository.consultarHistorialServicios(idUsuario);
        return new ResultadoDobleConsulta(antes, despues);
    }

    /**
     * Ejecuta RFC1 (histórico de servicios por usuario) en nivel SERIALIZABLE
     * realizando la consulta dos veces con una espera de 30 segundos para pruebas de concurrencia.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true)
    public ResultadoDobleConsulta consultarHistorialSerializable(Long idUsuario) {
        List<Object[]> antes = servicioRepository.consultarHistorialServicios(idUsuario);
        esperar30s();
        List<Object[]> despues = servicioRepository.consultarHistorialServicios(idUsuario);
        return new ResultadoDobleConsulta(antes, despues);
    }

    private void esperar30s() {
        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pequeño DTO para exponer resultados antes/después.
     */
    public static class ResultadoDobleConsulta {
        private final List<Object[]> antes;
        private final List<Object[]> despues;

        public ResultadoDobleConsulta(List<Object[]> antes, List<Object[]> despues) {
            this.antes = antes;
            this.despues = despues;
        }

        public List<Object[]> getAntes() { return antes; }
        public List<Object[]> getDespues() { return despues; }
    }
}
