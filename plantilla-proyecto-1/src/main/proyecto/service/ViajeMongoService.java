package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.bson.types.ObjectId;

import uniandes.edu.co.proyecto.Repositories.ViajeMongoRepository;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;
import uniandes.edu.co.proyecto.Repositories.ClienteMongoRepository;

import java.util.*;

@Service
public class ViajeMongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ViajeMongoRepository viajeRepo;

    @Autowired
    private ConductorMongoRepository conductorRepo;

    @Autowired
    private ClienteMongoRepository clienteRepo;

    /**
     * Registra un viaje a partir de un servicio existente en la colección `servicios`.
     * Inserta un documento en `viajes` y actualiza los documentos de conductor y pasajero
     * añadiendo un resumen en el array `viajes`.
     *
     * @param servicioId id del servicio (string) en la colección `servicios`
     * @return id del viaje creado (String) o null si no pudo crearse
     */
    public String registrarViajeDesdeServicio(String servicioId) {
        Document serv = mongoTemplate.findById(new ObjectId(servicioId), Document.class, "servicios");
        if (serv == null) throw new IllegalArgumentException("Servicio no encontrado: " + servicioId);

        // construir documento de viaje
        Document viaje = new Document();
        viaje.put("pasajeroId", serv.get("clienteId"));
        viaje.put("conductorId", serv.get("conductorId"));
        viaje.put("servicioId", serv.get("_id"));
        // Normalizar tipoServicio para cumplir el enum definido en la validación de Atlas
        String rawTipo = serv.getString("tipoServicio");
        String tipoNorm = "Pasajeros - Estándar";
        if (rawTipo != null) {
            String rt = rawTipo.trim();
            if (rt.equalsIgnoreCase("Comida")) {
                tipoNorm = "Comida";
            } else if (rt.equalsIgnoreCase("Mercancías") || rt.equalsIgnoreCase("Mercancias")) {
                tipoNorm = "Mercancías";
            } else if (rt.equalsIgnoreCase("Pasajeros - Estándar") || rt.equalsIgnoreCase("Pasajeros - Confort") || rt.equalsIgnoreCase("Pasajeros - Large")) {
                tipoNorm = rt;
            } else if (rt.toLowerCase().contains("confort")) {
                tipoNorm = "Pasajeros - Confort";
            } else if (rt.toLowerCase().contains("large") || rt.toLowerCase().contains("grande")) {
                tipoNorm = "Pasajeros - Large";
            } else if (rt.toLowerCase().contains("trayecto") || rt.toLowerCase().contains("pasajero")) {
                tipoNorm = "Pasajeros - Estándar";
            } else {
                // fallback: keep the raw value (may fail validation) or default to Estándar
                tipoNorm = rt;
            }
        }
        viaje.put("tipoServicio", tipoNorm);

        // intentar obtener vehículo (placa/modelo/nivel)
        String placa = serv.getString("vehiculoPlaca");
        Document veh = new Document();
        if (placa != null) {
            veh.put("placa", placa);
            // intentar obtener modelo/nivel consultando conductor
            Object conductorIdObj = serv.get("conductorId");
            String conductorIdStr = conductorIdObj != null ? conductorIdObj.toString() : null;
            if (conductorIdStr != null) {
                // buscar conductor Mongo
                try {
                    Document condDoc = mongoTemplate.findById(new ObjectId(conductorIdStr), Document.class, "conductores");
                    if (condDoc != null) {
                        java.util.List<Document> vehs = (java.util.List<Document>) condDoc.get("vehiculos");
                        if (vehs != null) {
                            for (Document vdoc : vehs) {
                                if (placa.equals(vdoc.getString("placa"))) {
                                    String marca = vdoc.getString("marca") != null ? vdoc.getString("marca") : "";
                                    String modeloVal = vdoc.get("modelo") != null ? vdoc.get("modelo").toString() : null;
                                    veh.put("modelo", (marca + (modeloVal != null ? " " + modeloVal : "")).trim());
                                    if (vdoc.get("capacidad") != null) {
                                        int cap = Integer.parseInt(vdoc.get("capacidad").toString());
                                        if (cap <= 4) veh.put("nivel", "Estándar");
                                        else if (cap <= 7) veh.put("nivel", "Confort");
                                        else veh.put("nivel", "Large");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }
        }
        // Asegurar campos requeridos por el schema en Atlas
        if (!veh.containsKey("placa")) veh.put("placa", placa != null ? placa : "N/A");
        if (!veh.containsKey("modelo")) veh.put("modelo", "N/A");
        if (!veh.containsKey("nivel")) {
            // intentar derivar nivel desde el servicio si existe
            String nivelServ = serv.getString("nivelAplicado");
            if (nivelServ != null) veh.put("nivel", nivelServ);
            else veh.put("nivel", "Estándar");
        }
        viaje.put("vehiculo", veh);

        // horaInicio/horaFin: usar campos existentes si hay, sino hora actual como fin
        Object hIni = serv.get("horaInicio");
        Date horaInicio = null;
        if (hIni instanceof Date) horaInicio = (Date) hIni;
        else if (hIni != null) {
            try { horaInicio = new Date(hIni.toString()); } catch (Exception ignored) {}
        }
        Date horaFin = new Date();
        viaje.put("horaInicio", horaInicio != null ? horaInicio : horaFin);
        viaje.put("horaFin", horaFin);

        // distancia y costo
        Object km = serv.get("kilometros");
        Double distancia = null;
        if (km != null) {
            try { distancia = Double.parseDouble(km.toString()); } catch (Exception ignored) {}
        }
        viaje.put("distanciaKm", distancia);

        Object costo = serv.get("costoTotal");
        Double costoTotal = null;
        if (costo != null) {
            try { costoTotal = Double.parseDouble(costo.toString()); } catch (Exception ignored) {}
        }
        viaje.put("costoTotal", costoTotal);

        // insertar en viajes
        Document inserted = mongoTemplate.insert(viaje, "viajes");

        // actualizar conductor y pasajero: push resumen en array 'viajes'
        Document resumen = new Document();
        resumen.put("viajeId", inserted.get("_id"));
        resumen.put("servicioId", serv.get("_id"));
        resumen.put("horaInicio", viaje.get("horaInicio"));
        resumen.put("horaFin", viaje.get("horaFin"));
        resumen.put("distanciaKm", viaje.get("distanciaKm"));
        resumen.put("costoTotal", viaje.get("costoTotal"));

        // push to conductor
        Object conductorIdObj = serv.get("conductorId");
        if (conductorIdObj != null) {
            try {
                mongoTemplate.getCollection("conductores").updateOne(new org.bson.Document("_id", conductorIdObj), new org.bson.Document("$push", new org.bson.Document("viajes", resumen)));
            } catch (Exception ignored) {}
        }

        // push to pasajero (cliente)
        Object clienteIdObj = serv.get("clienteId");
        if (clienteIdObj != null) {
            try {
                mongoTemplate.getCollection("clientes").updateOne(new org.bson.Document("_id", clienteIdObj), new org.bson.Document("$push", new org.bson.Document("viajes", resumen)));
            } catch (Exception ignored) {}
        }

        return inserted.get("_id").toString();
    }
}
