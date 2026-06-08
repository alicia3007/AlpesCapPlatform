package uniandes.edu.co.proyecto.analytics;

import java.util.List;

public record AnalyticsDashboardResponse(
        Window window,
        List<KpiMetric> kpis,
        List<SeriesPoint> ridesPerDay,
        List<SeriesPoint> revenuePerDay,
        List<EntityMetric> mostActiveDrivers,
        List<EntityMetric> mostActiveCustomers,
        List<SeriesPoint> peakDemandHours,
        List<CityMetric> tripsByCity,
        List<Insight> insights
) {
    public record Window(String from, String to) {}

    public record KpiMetric(String key, String label, Double value, String unit, String description) {}

    public record SeriesPoint(String label, Double value) {}

    public record EntityMetric(String id, String name, Long value, Double share) {}

    public record CityMetric(String city, Long value, Double share) {}

    public record Insight(String title, String detail, String impact) {}
}