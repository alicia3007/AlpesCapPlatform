package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import uniandes.edu.co.proyecto.Repositories.DisponibilidadMongoRepository;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;
import uniandes.edu.co.proyecto.modelo.DisponibilidadMongo;
import uniandes.edu.co.proyecto.modelo.ConductorMongo;

import java.util.List;

@Service
public class DisponibilidadMongoService {

    private final DisponibilidadMongoRepository disponibilidadRepo;
    private final ConductorMongoRepository conductorRepo;

    public DisponibilidadMongoService(DisponibilidadMongoRepository disponibilidadRepo, ConductorMongoRepository conductorRepo) {
        this.disponibilidadRepo = disponibilidadRepo;
        this.conductorRepo = conductorRepo;
    }

    public DisponibilidadMongo registrarDisponibilidad(DisponibilidadMongo disp) {
        return saveDisponibilidad(disp, false);
    }

    public DisponibilidadMongo actualizarDisponibilidad(DisponibilidadMongo disp) {
        return saveDisponibilidad(disp, true);
    }

    private DisponibilidadMongo saveDisponibilidad(DisponibilidadMongo disp, boolean isUpdate) {
        // validar campos básicos
        if (disp.getConductorId() == null || disp.getConductorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un conductor");
        }
        if (disp.getDiaSemana() == null || disp.getDiaSemana().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un día de la semana");
        }
        if (disp.getHoraInicio() == null || disp.getHoraFin() == null) {
            throw new IllegalArgumentException("Debe indicar hora inicio y fin");
        }

        // calcular tipoServicio si es Pasajeros
        String tipo = disp.getTipoServicio();
        if (tipo != null && tipo.equalsIgnoreCase("Pasajeros")) {
            // buscar conductor y vehículo para determinar nivel
            ConductorMongo conductor = conductorRepo.findById(disp.getConductorId()).orElse(null);
            if (conductor != null && disp.getVehiculoPlaca() != null) {
                ConductorMongo.Vehiculo veh = conductor.getVehiculos().stream()
                        .filter(v -> disp.getVehiculoPlaca().equals(v.getPlaca()))
                        .findFirst().orElse(null);
                String nivel = "Pasajeros - Estándar";
                if (veh != null && veh.getCapacidad() != null) {
                    int cap = veh.getCapacidad();
                    if (cap <= 4) nivel = "Pasajeros - Estándar";
                    else if (cap <= 7) nivel = "Pasajeros - Confort";
                    else nivel = "Pasajeros - Large";
                }
                disp.setTipoServicio(nivel);

                // Rellenar el objeto vehiculo embebido en la disponibilidad para mostrar detalles
                DisponibilidadMongo.Vehiculo vdoc = new DisponibilidadMongo.Vehiculo();
                if (veh != null) {
                    vdoc.setPlaca(veh.getPlaca());
                    String modeloStr = (veh.getMarca() != null ? veh.getMarca() : "") + (veh.getModelo() != null ? " " + veh.getModelo() : "");
                    vdoc.setModelo(modeloStr.trim());
                    vdoc.setCapacidad(veh.getCapacidad());
                    vdoc.setNivel(nivel.replace("Pasajeros - ", ""));
                } else {
                    vdoc.setPlaca(disp.getVehiculoPlaca());
                    vdoc.setModelo("");
                    vdoc.setCapacidad(null);
                    vdoc.setNivel(nivel.replace("Pasajeros - ", ""));
                }
                disp.setVehiculo(vdoc);
            } else {
                // si no hay vehículo seleccionado, dejar Pasajeros sin nivel e intentar rellenar vehiculo
                disp.setTipoServicio("Pasajeros - Estándar");
            }
        }

        // validar solapamientos: traer disponibilidades del mismo conductor y día
        List<DisponibilidadMongo> existentes = disponibilidadRepo.findByConductorIdAndDiaSemana(disp.getConductorId(), disp.getDiaSemana());
        int newStart = toMinutes(disp.getHoraInicio());
        int newEnd = toMinutes(disp.getHoraFin());
        if (newEnd <= newStart) {
            throw new IllegalArgumentException("La hora fin debe ser posterior a la hora inicio");
        }
        for (DisponibilidadMongo ex : existentes) {
            if (isUpdate && ex.getId() != null && ex.getId().equals(disp.getId())) continue; // permitir mismo registro
            int exStart = toMinutes(ex.getHoraInicio());
            int exEnd = toMinutes(ex.getHoraFin());
            // overlap if newStart < exEnd && newEnd > exStart
            if (newStart < exEnd && newEnd > exStart) {
                throw new IllegalArgumentException("Existe una disponibilidad solapada para ese conductor en el mismo día");
            }
        }

        return disponibilidadRepo.save(disp);
    }

    private int toMinutes(String hhmm) {
        try {
            String[] parts = hhmm.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            return h * 60 + m;
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de hora inválido: " + hhmm);
        }
    }
}
