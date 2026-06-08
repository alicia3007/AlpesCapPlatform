package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import uniandes.edu.co.proyecto.Repositories.*;
import uniandes.edu.co.proyecto.modelo.*;

import java.util.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.bson.types.ObjectId;

@Service
public class ServicioMongoService {

    @Autowired
    private ClienteMongoRepository clienteRepo;


    @Autowired
    private DisponibilidadMongoRepository disponibilidadRepo;

    @Autowired
    private ServicioMongoRepository servicioRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Solicita un servicio: calcula tarifa, asigna conductor disponible y guarda el servicio.
     * Reglas y supuestos:
     * - Busca conductores que tengan una disponibilidad para el día y hora actual.
     * - Omite conductores que ya tengan un servicio en estado ASIGNADO o EN_CURSO.
     * - No hay ubicación fija del conductor en el modelo; la selección de "cercanía" se
     *   omite si no existen coordenadas del conductor. Se prioriza capacidad del vehículo.
     */
    public ServicioMongo solicitarServicio(ServicioMongo solicitud, Integer cantidadPasajeros) {
        // validar cliente
        if (solicitud.getClienteId() == null || !clienteRepo.findById(solicitud.getClienteId()).isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        // calcular tarifa: distancia * tarifaPorKm * multiplicador por nivel
        double distanciaKm = haversine(
            solicitud.getOrigenLat() == null ? 0.0 : solicitud.getOrigenLat(),
            solicitud.getOrigenLng() == null ? 0.0 : solicitud.getOrigenLng(),
            solicitud.getDestinoLat() == null ? 0.0 : solicitud.getDestinoLat(),
            solicitud.getDestinoLng() == null ? 0.0 : solicitud.getDestinoLng()
        );
        double tarifaPorKm = 1000.0; // valor base por km (ajustable)
        double multiplicador = 1.0;

        // buscar conductores disponibles ahora
        String dia = nombreDiaSemana(new Date());
        String horaActual = horaActualHHmm();

        List<DisponibilidadMongo> candidatas = new ArrayList<>();
        List<DisponibilidadMongo> todas = disponibilidadRepo.findAll();
        for (DisponibilidadMongo d : todas) {
            if (d.getDiaSemana() == null) continue;
            if (!d.getDiaSemana().equalsIgnoreCase(dia)) continue;
            int inicio = toMinutes(d.getHoraInicio());
            int fin = toMinutes(d.getHoraFin());
            int ahora = toMinutes(horaActual);
            if (ahora >= inicio && ahora <= fin) {
                // candidato
                candidatas.add(d);
            }
        }

        // filtrar conductores que ya tienen servicio activo
        List<DisponibilidadMongo> filtradas = new ArrayList<>();
        for (DisponibilidadMongo d : candidatas) {
            boolean busy = false;
            List<String> estados = Arrays.asList("ASIGNADO", "EN_CURSO");
            List<Document> activos = mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(org.springframework.data.mongodb.core.query.Criteria.where("conductorId").is(new ObjectId(d.getConductorId())).and("estado").in(estados)), Document.class, "servicios");
            if (activos != null && !activos.isEmpty()) busy = true;
            if (!busy) filtradas.add(d);
        }

        // preparar documento base
        Document doc = new Document();
        doc.put("tipoServicio", solicitud.getTipoServicio() != null ? solicitud.getTipoServicio() : "TrayectoPasajeros");
        doc.put("clienteId", new ObjectId(solicitud.getClienteId()));

        if (filtradas.isEmpty()) {
            // no hay conductores disponibles => guardar PENDIENTE con campos mínimos
            // incluir conductorId como placeholder para cumplir el schema (ObjectId requerido)
            doc.put("conductorId", new ObjectId("000000000000000000000000"));
            doc.put("estado", "PENDIENTE");
            doc.put("fechaSolicitud", new Date());
            // puntoPartida / puntosLlegada
            Document partida = new Document();
            partida.put("nombre", "Origen");
            partida.put("direccion", solicitud.getOrigenAddress());
            partida.put("ciudad", "");
            Document coordsP = new Document();
            coordsP.put("latitud", solicitud.getOrigenLat());
            coordsP.put("longitud", solicitud.getOrigenLng());
            partida.put("coordenadas", coordsP);
            doc.put("puntoPartida", partida);

            List<Document> llegadas = new ArrayList<>();
            Document dest = new Document();
            dest.put("nombre", "Destino");
            dest.put("direccion", solicitud.getDestinoAddress());
            dest.put("ciudad", "");
            Document coordsD = new Document();
            coordsD.put("latitud", solicitud.getDestinoLat());
            coordsD.put("longitud", solicitud.getDestinoLng());
            dest.put("coordenadas", coordsD);
            llegadas.add(dest);
            doc.put("puntosLlegada", llegadas);

            doc.put("kilometros", distanciaKm);
            int duracionMinutos = Math.max(5, (int)Math.ceil(distanciaKm / 30.0 * 60.0));
            doc.put("duracion", duracionMinutos);
            doc.put("horaInicio", new Date());
            double tarifa = distanciaKm * tarifaPorKm * multiplicador;
            doc.put("costoTotal", tarifa);
            doc.put("comision", 0);
            doc.put("vehiculoPlaca", solicitud.getVehiculoPlaca() != null ? solicitud.getVehiculoPlaca() : "");
            doc.put("tarifaId", new ObjectId());
            mongoTemplate.insert(doc, "servicios");
            return null;
        }

        // seleccionar el mejor candidato: preferir vehículo con capacidad >= cantidadPasajeros
        DisponibilidadMongo elegido = null;
        for (DisponibilidadMongo d : filtradas) {
            if (d.getVehiculo() != null && d.getVehiculo().getCapacidad() != null && cantidadPasajeros != null) {
                if (d.getVehiculo().getCapacidad() >= cantidadPasajeros) { elegido = d; break; }
            }
        }
        if (elegido == null) elegido = filtradas.get(0);

        if (elegido.getVehiculo() != null && elegido.getVehiculo().getNivel() != null) {
            String nivel = elegido.getVehiculo().getNivel().toLowerCase();
            if (nivel.contains("confort")) multiplicador = 1.5;
            else if (nivel.contains("large") || nivel.contains("grande")) multiplicador = 2.0;
            else multiplicador = 1.0;
        }

        double tarifa = distanciaKm * tarifaPorKm * multiplicador;

        // completar documento con conductor y cálculos
        doc.put("conductorId", new ObjectId(elegido.getConductorId()));
        doc.put("vehiculoPlaca", elegido.getVehiculo() != null ? elegido.getVehiculo().getPlaca() : elegido.getVehiculoPlaca());
        doc.put("tarifaId", new ObjectId());

        Document partida = new Document();
        partida.put("nombre", "Origen");
        partida.put("direccion", solicitud.getOrigenAddress());
        partida.put("ciudad", "");
        Document coordsP = new Document();
        coordsP.put("latitud", solicitud.getOrigenLat());
        coordsP.put("longitud", solicitud.getOrigenLng());
        partida.put("coordenadas", coordsP);
        doc.put("puntoPartida", partida);

        List<Document> llegadas = new ArrayList<>();
        Document dest = new Document();
        dest.put("nombre", "Destino");
        dest.put("direccion", solicitud.getDestinoAddress());
        dest.put("ciudad", "");
        Document coordsD = new Document();
        coordsD.put("latitud", solicitud.getDestinoLat());
        coordsD.put("longitud", solicitud.getDestinoLng());
        dest.put("coordenadas", coordsD);
        llegadas.add(dest);
        doc.put("puntosLlegada", llegadas);

        doc.put("kilometros", distanciaKm);
        int duracionMinutos = Math.max(5, (int)Math.ceil(distanciaKm / 30.0 * 60.0));
        doc.put("duracion", duracionMinutos);
        doc.put("horaInicio", new Date());
        doc.put("comision", 3500.0);
        doc.put("costoTotal", tarifa + 3500.0);
        doc.put("estado", "ASIGNADO");
        doc.put("cantidadPasajeros", cantidadPasajeros != null ? cantidadPasajeros : 1);

        mongoTemplate.insert(doc, "servicios");

        return null;
    }

    private static int toMinutes(String hhmm) {
        if (hhmm == null) return 0;
        try {
            String[] p = hhmm.split(":");
            int h = Integer.parseInt(p[0]);
            int m = Integer.parseInt(p[1]);
            return h * 60 + m;
        } catch (Exception e) { return 0; }
    }

    private static String horaActualHHmm() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        return String.format("%02d:%02d", h, m);
    }

    private static String nombreDiaSemana(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int d = c.get(Calendar.DAY_OF_WEEK);
        switch (d) {
            case Calendar.MONDAY: return "Lunes";
            case Calendar.TUESDAY: return "Martes";
            case Calendar.WEDNESDAY: return "Miércoles";
            case Calendar.THURSDAY: return "Jueves";
            case Calendar.FRIDAY: return "Viernes";
            case Calendar.SATURDAY: return "Sábado";
            case Calendar.SUNDAY: return "Domingo";
            default: return "";
        }
    }

    // Haversine formula
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
