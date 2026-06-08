# Business Analytics Module

## Purpose
This module turns the ride-sharing platform into a portfolio-ready analytics case study that can be presented for internships in Data Analytics, Business Analytics, and Data Science.

## Architecture Decision
The analytics layer uses a hybrid approach:

- MongoDB aggregates operational trip behavior from `viajes` and `servicios`.
- Oracle SQL aggregates financial and geographic metrics from `PAGO`, `SERVICIO`, `SERVICIO_PUNTO`, `PUNTO`, and `CIUDAD`.
- Spring Boot exposes the results through REST endpoints under `/api/analytics`.

## KPI Coverage
- Total rides
- Rides per day
- Revenue per day
- Average revenue per ride
- Most active drivers
- Most active customers
- Cancellation rate
- Average trip duration
- Peak demand hours
- Trips by city

## REST Endpoints
- `GET /api/analytics/dashboard`
- `GET /api/analytics/kpis`
- `GET /api/analytics/revenue`
- `GET /api/analytics/drivers`
- `GET /api/analytics/customers`
- `GET /api/analytics/operations`
- `GET /api/analytics/behavior`
- `GET /api/analytics/insights`

## Implementation Steps
1. Register the analytics package in the Spring Boot application.
2. Populate MongoDB `viajes` and `servicios` with enough history to support trend analysis.
3. Load the exported analytics CSV in Power BI or the notebook.
4. Validate the REST endpoints with a date range.
5. Use the dashboard specification to build the Power BI report.

## Portfolio Framing
This module demonstrates:

- Backend Engineering: API design, service orchestration, and data aggregation.
- Database Engineering: SQL and MongoDB query design.
- Business Analytics: KPI definition, dashboarding, and insight generation.
