package uniandes.edu.co.proyecto.analytics;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public AnalyticsDashboardResponse buildDashboard(LocalDate from, LocalDate to) {
        LocalDate effectiveTo = to != null ? to : LocalDate.now();
        LocalDate effectiveFrom = from != null ? from : effectiveTo.minusDays(90);

        var start = analyticsRepository.toStartOfDay(effectiveFrom);
        var end = analyticsRepository.toEndExclusive(effectiveTo);

        long totalRides = analyticsRepository.countTrips(start, end);
        double totalRevenue = analyticsRepository.loadTotalRevenue(start, end);
        long daySpan = Math.max(1, ChronoUnit.DAYS.between(effectiveFrom, effectiveTo) + 1);
        double averageRevenuePerRide = totalRides > 0 ? roundTwoDecimals(totalRevenue / totalRides) : 0d;
        double cancellationRate = analyticsRepository.loadCancellationRate(start, end);
        double averageTripDurationMinutes = analyticsRepository.loadAverageTripDuration(start, end);

        List<AnalyticsDashboardResponse.KpiMetric> kpis = new ArrayList<>();
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("total_rides", "Total rides", (double) totalRides, "rides", "Completed trips in the selected window."));
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("rides_per_day", "Rides per day", roundTwoDecimals(totalRides / (double) daySpan), "rides/day", "Average daily trip volume."));
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("revenue_per_day", "Revenue per day", roundTwoDecimals(totalRevenue / daySpan), "COP/day", "Average daily revenue."));
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("average_revenue_per_ride", "Average revenue per ride", averageRevenuePerRide, "COP/ride", "Ticket size proxy."));
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("cancellation_rate", "Cancellation rate", cancellationRate, "%", "Canceled service requests / total service requests."));
        kpis.add(new AnalyticsDashboardResponse.KpiMetric("average_trip_duration", "Average trip duration", averageTripDurationMinutes, "minutes", "Mean trip duration."));

        List<AnalyticsDashboardResponse.SeriesPoint> ridesPerDay = analyticsRepository.loadRidesPerDay(start, end);
        List<AnalyticsDashboardResponse.SeriesPoint> revenuePerDay = analyticsRepository.loadRevenuePerDay(start, end);
        List<AnalyticsDashboardResponse.EntityMetric> mostActiveDrivers = analyticsRepository.loadMostActiveDrivers(start, end, 10);
        List<AnalyticsDashboardResponse.EntityMetric> mostActiveCustomers = analyticsRepository.loadMostActiveCustomers(start, end, 10);
        List<AnalyticsDashboardResponse.SeriesPoint> peakDemandHours = analyticsRepository.loadPeakDemandHours(start, end);
        List<AnalyticsDashboardResponse.CityMetric> tripsByCity = analyticsRepository.loadTripsByCity(start, end);

        return new AnalyticsDashboardResponse(
                new AnalyticsDashboardResponse.Window(effectiveFrom.toString(), effectiveTo.toString()),
                kpis,
                ridesPerDay,
                revenuePerDay,
                mostActiveDrivers,
                mostActiveCustomers,
                peakDemandHours,
                tripsByCity,
                buildInsights(kpis, peakDemandHours, tripsByCity, mostActiveDrivers, mostActiveCustomers, totalRevenue)
        );
    }

    public List<AnalyticsDashboardResponse.KpiMetric> getKpis(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).kpis();
    }

    public List<AnalyticsDashboardResponse.SeriesPoint> getRevenueTrend(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).revenuePerDay();
    }

    public List<AnalyticsDashboardResponse.EntityMetric> getDriverRanking(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).mostActiveDrivers();
    }

    public List<AnalyticsDashboardResponse.EntityMetric> getCustomerRanking(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).mostActiveCustomers();
    }

    public List<AnalyticsDashboardResponse.SeriesPoint> getDemandTrend(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).ridesPerDay();
    }

    public List<AnalyticsDashboardResponse.CityMetric> getTripsByCity(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).tripsByCity();
    }

    public List<AnalyticsDashboardResponse.Insight> getInsights(LocalDate from, LocalDate to) {
        return buildDashboard(from, to).insights();
    }

    public AnalyticsDashboardResponse getOperationsSnapshot(LocalDate from, LocalDate to) {
        return buildDashboard(from, to);
    }

    public AnalyticsDashboardResponse getBehaviorSnapshot(LocalDate from, LocalDate to) {
        return buildDashboard(from, to);
    }

    private List<AnalyticsDashboardResponse.Insight> buildInsights(List<AnalyticsDashboardResponse.KpiMetric> kpis,
                                                                    List<AnalyticsDashboardResponse.SeriesPoint> peakDemandHours,
                                                                    List<AnalyticsDashboardResponse.CityMetric> tripsByCity,
                                                                    List<AnalyticsDashboardResponse.EntityMetric> mostActiveDrivers,
                                                                    List<AnalyticsDashboardResponse.EntityMetric> mostActiveCustomers,
                                                                    double totalRevenue) {
        double totalRides = metricValue(kpis, "total_rides");
        double averageRevenue = metricValue(kpis, "average_revenue_per_ride");
        double cancellationRate = metricValue(kpis, "cancellation_rate");
        double avgDuration = metricValue(kpis, "average_trip_duration");

        List<AnalyticsDashboardResponse.Insight> insights = new ArrayList<>();
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Demand baseline",
                String.format("The selected window contains %.0f rides, which is the baseline for every ratio in the dashboard.", totalRides),
                "Use it to compare traffic growth month over month."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Revenue efficiency",
                String.format("Average revenue per ride is %.2f COP and should be tracked alongside conversion and commission rules.", averageRevenue),
                "This metric helps validate pricing and commission settings."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Cancellation control",
                String.format("Cancellation rate stands at %.2f%%, a strong indicator of dispatch quality and user experience.", cancellationRate),
                "If it rises, inspect ETA promises and supply balance."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Ride duration",
                String.format("Average trip duration is %.2f minutes, useful for measuring traffic pressure and route efficiency.", avgDuration),
                "Longer durations during peak hours may justify more drivers."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Peak period",
                peakDemandHours.isEmpty() ? "No peak demand data was detected." : "The busiest hour is " + peakDemandHours.stream().max((a, b) -> Double.compare(a.value(), b.value())).map(AnalyticsDashboardResponse.SeriesPoint::label).orElse("N/A") + ", which is where fleet positioning should be strongest.",
                "Plan staffing and surge incentives around this hour."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "City concentration",
                tripsByCity.isEmpty() ? "No city breakdown was returned." : "The top city concentrates the majority of rides, which supports geo-targeted growth plans.",
                "Prioritize marketing and operational coverage in high-demand cities."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Driver concentration",
                mostActiveDrivers.isEmpty() ? "No driver ranking was returned." : "The top drivers capture a measurable share of trips, so retention of high performers matters.",
                "Use incentives and capacity balancing to avoid overdependence."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Customer retention",
                mostActiveCustomers.isEmpty() ? "No customer ranking was returned." : "Repeat riders are visible in the top customer list, which is the starting point for loyalty actions.",
                "Target loyalty campaigns and referral offers to those customers."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Business scale",
                String.format("Revenue visibility across the selected period provides a clear financial lens for the analytics layer. Total revenue reached %.2f COP.", totalRevenue),
                "This is a portfolio-ready KPI for business analytics interviews."));
        insights.add(new AnalyticsDashboardResponse.Insight(
                "Hybrid stack value",
                "The module combines MongoDB aggregations with SQL native queries, which is a realistic analytics architecture for a ride-sharing platform.",
                "It demonstrates backend, database, and BI capabilities in one module."));
        return insights;
    }

    private double metricValue(List<AnalyticsDashboardResponse.KpiMetric> kpis, String key) {
        return kpis.stream()
                .filter(metric -> key.equals(metric.key()))
                .map(AnalyticsDashboardResponse.KpiMetric::value)
                .findFirst()
                .orElse(0d);
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }
}