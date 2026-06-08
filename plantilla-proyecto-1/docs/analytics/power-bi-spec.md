# Power BI Dashboard Specification

## Page 1: Executive Summary
Purpose: deliver a quick C-level snapshot.

Visuals:
- KPI cards for total rides, revenue, average revenue per ride, cancellation rate, and average trip duration.
- Line chart for rides per day.
- Line chart for revenue per day.
- Top 5 city bar chart.
- Top 10 driver ranking table.

Narrative:
- Shows whether the platform is growing, how efficient trips are, and where demand concentrates.

## Page 2: Revenue Analysis
Purpose: show monetization behavior.

Visuals:
- Line chart for revenue per day.
- Clustered bar chart for revenue by city.
- Histogram for trip revenue distribution.
- KPI card for average revenue per ride.
- Slicer for date range.

Key question:
- Which cities and time periods produce the strongest revenue?

## Page 3: Customer Behavior
Purpose: understand retention and ride patterns.

Visuals:
- Top customers table.
- Rides per day line chart.
- Peak demand hour column chart.
- Stacked bar chart by city and trip status.
- Repeat customer share metric.

Key question:
- Which customers and time windows deserve retention campaigns?

## Page 4: Driver Performance
Purpose: measure supply efficiency.

Visuals:
- Top drivers ranking table.
- Rides per driver bar chart.
- Share-of-activity donut chart.
- Scatter plot of trips versus average trip duration.
- Conditional formatting for high and low performers.

Key question:
- Who should receive incentives, coaching, or workload balancing?

## Page 5: Operational Metrics
Purpose: monitor service quality.

Visuals:
- Cancellation rate KPI card.
- Average trip duration KPI card.
- Hourly demand column chart.
- Trips by city map or filled map.
- Trend comparison lines for rides and cancellations.

Key question:
- Where are operational bottlenecks and what needs more supply?

## Suggested Filters
- Date range
- City
- Driver
- Customer segment
- Trip status

## Design Notes
- Use a dark executive theme with high-contrast cards and one accent color for revenue.
- Keep KPI cards at the top of every page.
- Use the same date slicer across all pages for consistency.
- Add a short textual insight box on every page.
