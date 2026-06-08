package uniandes.edu.co.proyecto.analytics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import uniandes.edu.co.proyecto.Repositories.ClienteMongoRepository;
import uniandes.edu.co.proyecto.Repositories.ConductorMongoRepository;
import uniandes.edu.co.proyecto.modelo.ClienteMongo;
import uniandes.edu.co.proyecto.modelo.ConductorMongo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class AnalyticsRepository {

    private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();

    @PersistenceContext
    private EntityManager entityManager;

    private final MongoTemplate mongoTemplate;
    private final ConductorMongoRepository conductorMongoRepository;
    private final ClienteMongoRepository clienteMongoRepository;

    public AnalyticsRepository(MongoTemplate mongoTemplate,
                               ConductorMongoRepository conductorMongoRepository,
                               ClienteMongoRepository clienteMongoRepository) {
        this.mongoTemplate = mongoTemplate;
        this.conductorMongoRepository = conductorMongoRepository;
        this.clienteMongoRepository = clienteMongoRepository;
    }

    public long countTrips(Date start, Date end) {
        return mongoTemplate.count(buildDateQuery("horaInicio", start, end), "viajes");
    }

    public List<AnalyticsDashboardResponse.SeriesPoint> loadRidesPerDay(Date start, Date end) {
        return aggregateDailyMongo("viajes", "horaInicio", false, start, end);
    }

    public List<AnalyticsDashboardResponse.SeriesPoint> loadRevenuePerDay(Date start, Date end) {
        String sql = """
                SELECT TO_CHAR(FECHA_PAGO, 'YYYY-MM-DD') AS dia,
                       NVL(SUM(MONTO), 0) AS total_revenue
                FROM PAGO
                WHERE (:startDate IS NULL OR FECHA_PAGO >= :startDate)
                  AND (:endDate IS NULL OR FECHA_PAGO < :endDate)
                GROUP BY TO_CHAR(FECHA_PAGO, 'YYYY-MM-DD')
                ORDER BY dia
                """;
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> rows = entityManager.createNativeQuery(sql)
                    .setParameter("startDate", toTimestamp(start))
                    .setParameter("endDate", toTimestamp(end))
                    .getResultList();
            if (rows.isEmpty()) {
                return aggregateDailyMongo("viajes", "horaFin", true, start, end);
            }
            List<AnalyticsDashboardResponse.SeriesPoint> points = new ArrayList<>();
            for (Object[] row : rows) {
                points.add(new AnalyticsDashboardResponse.SeriesPoint(row[0].toString(), toDouble(row[1])));
            }
            return points;
        } catch (RuntimeException exception) {
            return aggregateDailyMongo("viajes", "horaFin", true, start, end);
        }
    }

    public double loadTotalRevenue(Date start, Date end) {
        try {
            String sql = """
                    SELECT NVL(SUM(MONTO), 0)
                    FROM PAGO
                    WHERE (:startDate IS NULL OR FECHA_PAGO >= :startDate)
                      AND (:endDate IS NULL OR FECHA_PAGO < :endDate)
                    """;
            Number result = (Number) entityManager.createNativeQuery(sql)
                    .setParameter("startDate", toTimestamp(start))
                    .setParameter("endDate", toTimestamp(end))
                    .getSingleResult();
            double total = result != null ? result.doubleValue() : 0d;
            if (total > 0d) {
                return total;
            }
        } catch (RuntimeException ignored) {
            // fallback below
        }
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument("horaInicio", start, end)));
        pipeline.add(new Document("$group", new Document("_id", null).append("total", new Document("$sum", "$costoTotal"))));
        List<Document> rows = mongoTemplate.getCollection("viajes").aggregate(pipeline).into(new ArrayList<>());
        if (rows.isEmpty()) {
            return 0d;
        }
        return toDouble(rows.get(0).get("total"));
    }

    public List<AnalyticsDashboardResponse.EntityMetric> loadMostActiveDrivers(Date start, Date end, int limit) {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument("horaInicio", start, end)));
        pipeline.add(new Document("$match", new Document("conductorId", new Document("$ne", null))));
        pipeline.add(new Document("$group", new Document("_id", "$conductorId").append("value", new Document("$sum", 1))));
        pipeline.add(new Document("$sort", new Document("value", -1)));
        pipeline.add(new Document("$limit", limit));
        return enrichPeople(mongoTemplate.getCollection("viajes").aggregate(pipeline).into(new ArrayList<>()), true);
    }

    public List<AnalyticsDashboardResponse.EntityMetric> loadMostActiveCustomers(Date start, Date end, int limit) {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument("horaInicio", start, end)));
        pipeline.add(new Document("$match", new Document("pasajeroId", new Document("$ne", null))));
        pipeline.add(new Document("$group", new Document("_id", "$pasajeroId").append("value", new Document("$sum", 1))));
        pipeline.add(new Document("$sort", new Document("value", -1)));
        pipeline.add(new Document("$limit", limit));
        return enrichPeople(mongoTemplate.getCollection("viajes").aggregate(pipeline).into(new ArrayList<>()), false);
    }

    public double loadCancellationRate(Date start, Date end) {
        try {
            long total = countServices(start, end);
            if (total == 0) {
                return 0d;
            }
            long cancelled = countCancelledServices(start, end);
            return roundTwoDecimals((cancelled * 100d) / total);
        } catch (RuntimeException exception) {
            long total = countMongoServices(start, end);
            if (total == 0) {
                return 0d;
            }
            long cancelled = countMongoCancelledServices(start, end);
            return roundTwoDecimals((cancelled * 100d) / total);
        }
    }

    public double loadAverageTripDuration(Date start, Date end) {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument("horaInicio", start, end)));
        pipeline.add(new Document("$match", new Document("horaInicio", new Document("$ne", null)).append("horaFin", new Document("$ne", null))));
        pipeline.add(new Document("$project", new Document("durationMinutes",
                new Document("$divide", List.of(new Document("$subtract", List.of("$horaFin", "$horaInicio")), 60000d)))));
        pipeline.add(new Document("$group", new Document("_id", null).append("avgDuration", new Document("$avg", "$durationMinutes"))));
        List<Document> rows = mongoTemplate.getCollection("viajes").aggregate(pipeline).into(new ArrayList<>());
        if (rows.isEmpty()) {
            return 0d;
        }
        return roundTwoDecimals(toDouble(rows.get(0).get("avgDuration")));
    }

    public List<AnalyticsDashboardResponse.SeriesPoint> loadPeakDemandHours(Date start, Date end) {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument("horaInicio", start, end)));
        pipeline.add(new Document("$project", new Document("hour", new Document("$hour", "$horaInicio"))));
        pipeline.add(new Document("$group", new Document("_id", "$hour").append("value", new Document("$sum", 1))));
        pipeline.add(new Document("$sort", new Document("_id", 1)));
        List<Document> rows = mongoTemplate.getCollection("viajes").aggregate(pipeline).into(new ArrayList<>());
        List<AnalyticsDashboardResponse.SeriesPoint> points = new ArrayList<>();
        for (Document row : rows) {
            points.add(new AnalyticsDashboardResponse.SeriesPoint(String.format("%02d:00", ((Number) row.get("_id")).intValue()), toDouble(row.get("value"))));
        }
        return points;
    }

    public List<AnalyticsDashboardResponse.CityMetric> loadTripsByCity(Date start, Date end) {
        try {
            String sql = """
                    SELECT ciudad, COUNT(*) AS total_trips
                    FROM (
                        SELECT c.NOMBRE AS ciudad
                        FROM SERVICIO s
                        JOIN SERVICIO_PUNTO sp ON s.IDSERVICIO = sp.IDSERVICIO
                        JOIN PUNTO p ON sp.IDPUNTO = p.IDPUNTO
                        JOIN CIUDAD c ON p.IDCIUDAD = c.IDCIUDAD
                        WHERE (:startDate IS NULL OR s.HORA_INICIO >= :startDate)
                          AND (:endDate IS NULL OR s.HORA_INICIO < :endDate)
                    )
                    GROUP BY ciudad
                    ORDER BY total_trips DESC
                    """;
            @SuppressWarnings("unchecked")
            List<Object[]> rows = entityManager.createNativeQuery(sql)
                    .setParameter("startDate", toTimestamp(start))
                    .setParameter("endDate", toTimestamp(end))
                    .getResultList();
            long total = 0L;
            for (Object[] row : rows) {
                total += ((Number) row[1]).longValue();
            }
            List<AnalyticsDashboardResponse.CityMetric> metrics = new ArrayList<>();
            for (Object[] row : rows) {
                String city = row[0] != null ? row[0].toString() : "Sin ciudad";
                long value = ((Number) row[1]).longValue();
                double share = total == 0 ? 0d : roundTwoDecimals((value * 100d) / total);
                metrics.add(new AnalyticsDashboardResponse.CityMetric(city, value, share));
            }
            return metrics;
        } catch (RuntimeException exception) {
            return List.of();
        }
    }

    public long countServices(Date start, Date end) {
        String sql = "SELECT COUNT(*) FROM SERVICIO WHERE (:startDate IS NULL OR HORA_INICIO >= :startDate) AND (:endDate IS NULL OR HORA_INICIO < :endDate)";
        Number result = (Number) entityManager.createNativeQuery(sql)
                .setParameter("startDate", toTimestamp(start))
                .setParameter("endDate", toTimestamp(end))
                .getSingleResult();
        return result.longValue();
    }

    public long countCancelledServices(Date start, Date end) {
        String sql = "SELECT COUNT(*) FROM SERVICIO WHERE (:startDate IS NULL OR HORA_INICIO >= :startDate) AND (:endDate IS NULL OR HORA_INICIO < :endDate) AND ESTADO IN ('CANCELADO', 'CANCELLED', 'CANCELADA')";
        Number result = (Number) entityManager.createNativeQuery(sql)
                .setParameter("startDate", toTimestamp(start))
                .setParameter("endDate", toTimestamp(end))
                .getSingleResult();
        return result.longValue();
    }

    private long countMongoServices(Date start, Date end) {
        Query query = new Query();
        if (start != null || end != null) {
            Criteria criteria = Criteria.where("fechaSolicitud");
            if (start != null) {
                criteria = criteria.gte(start);
            }
            if (end != null) {
                criteria = criteria.lt(end);
            }
            query.addCriteria(criteria);
        }
        return mongoTemplate.count(query, "servicios");
    }

    private long countMongoCancelledServices(Date start, Date end) {
        Query query = new Query();
        Criteria criteria = Criteria.where("estado").in("CANCELADO", "CANCELLED", "CANCELADA");
        if (start != null || end != null) {
            Criteria dateCriteria = Criteria.where("fechaSolicitud");
            if (start != null) {
                dateCriteria = dateCriteria.gte(start);
            }
            if (end != null) {
                dateCriteria = dateCriteria.lt(end);
            }
            query.addCriteria(dateCriteria);
        }
        query.addCriteria(criteria);
        return mongoTemplate.count(query, "servicios");
    }

    public Date toStartOfDay(LocalDate date) {
        return date == null ? null : Date.from(date.atStartOfDay(SYSTEM_ZONE).toInstant());
    }

    public Date toEndExclusive(LocalDate date) {
        return date == null ? null : Date.from(date.plusDays(1).atStartOfDay(SYSTEM_ZONE).toInstant());
    }

    private List<AnalyticsDashboardResponse.SeriesPoint> aggregateDailyMongo(String collectionName, String dateField, boolean revenue, Date start, Date end) {
        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$match", buildMongoRangeDocument(dateField, start, end)));
        pipeline.add(new Document("$project", new Document("day", new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$" + dateField)))
                .append("value", revenue ? "$costoTotal" : 1)));
        pipeline.add(new Document("$group", new Document("_id", "$day").append("value", revenue ? new Document("$sum", "$value") : new Document("$sum", "$value"))));
        pipeline.add(new Document("$sort", new Document("_id", 1)));
        List<Document> rows = mongoTemplate.getCollection(collectionName).aggregate(pipeline).into(new ArrayList<>());
        List<AnalyticsDashboardResponse.SeriesPoint> series = new ArrayList<>();
        for (Document row : rows) {
            series.add(new AnalyticsDashboardResponse.SeriesPoint(row.getString("_id"), toDouble(row.get("value"))));
        }
        return series;
    }

    private List<AnalyticsDashboardResponse.EntityMetric> enrichPeople(List<Document> rows, boolean driver) {
        long total = 0L;
        for (Document row : rows) {
            total += ((Number) row.get("value")).longValue();
        }
        List<AnalyticsDashboardResponse.EntityMetric> metrics = new ArrayList<>();
        for (Document row : rows) {
            String id = row.get("_id") != null ? row.get("_id").toString() : null;
            long count = ((Number) row.get("value")).longValue();
            String name = resolveName(id, driver);
            double share = total == 0 ? 0d : roundTwoDecimals((count * 100d) / total);
            metrics.add(new AnalyticsDashboardResponse.EntityMetric(id, name, count, share));
        }
        return metrics;
    }

    private String resolveName(String id, boolean driver) {
        if (id == null) {
            return "Sin identificador";
        }
        if (driver) {
            ConductorMongo conductor = conductorMongoRepository.findById(id).orElse(null);
            if (conductor != null && conductor.getNombre() != null && !conductor.getNombre().isBlank()) {
                return conductor.getNombre();
            }
            return conductor != null && conductor.getCedula() != null ? conductor.getCedula() : id;
        }
        ClienteMongo customer = clienteMongoRepository.findById(id).orElse(null);
        if (customer != null && customer.getNombre() != null && !customer.getNombre().isBlank()) {
            return customer.getNombre();
        }
        return customer != null && customer.getCedula() != null ? customer.getCedula() : id;
    }

    private Query buildDateQuery(String field, Date start, Date end) {
        Query query = new Query();
        Criteria criteria = Criteria.where(field);
        if (start != null) {
            criteria = criteria.gte(start);
        }
        if (end != null) {
            criteria = criteria.lt(end);
        }
        if (start != null || end != null) {
            query.addCriteria(criteria);
        }
        return query;
    }

    private Document buildMongoRangeDocument(String field, Date start, Date end) {
        Document range = new Document();
        if (start != null) {
            range.put("$gte", start);
        }
        if (end != null) {
            range.put("$lt", end);
        }
        Document match = new Document();
        if (!range.isEmpty()) {
            match.put(field, range);
        }
        return match;
    }

    private Timestamp toTimestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    private double toDouble(Object value) {
        if (value == null) {
            return 0d;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(value.toString());
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }
}