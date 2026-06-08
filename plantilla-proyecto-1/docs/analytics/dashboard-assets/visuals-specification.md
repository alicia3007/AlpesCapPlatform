# Visuals specification (per page)

This file describes each Power BI page defined in `power-bi-spec.md`. For every visual it lists: visualization type, fields used, DAX measures required, and the business purpose.

Page: Executive Summary
- Layout: Top row = KPI cards; middle = two trend lines side-by-side; right column = Top 5 city bar + Top 10 drivers table; footer = insight box

1) KPI cards (Total Rides, Total Revenue, Average Revenue per Ride, Cancellation Rate, Average Trip Duration)
   - Visualization type: Card (five separate cards)
   - Fields / Measures: `Total Rides`, `Total Revenue`, `Average Revenue per Ride`, `Cancellation Rate (%)`, `Average Trip Duration (min)`
   - Business purpose: fast C-level health check; these align 1:1 with `AnalyticsService` KPIs.

2) Rides per day
   - Visualization type: Line chart
   - Fields: Axis = `Date[Date]`, Values = `Rides Per Day` (measure) OR COUNT of `Trips` filtered to completed
   - DAX: `Rides Per Day` measure
   - Business purpose: shows traffic trend and growth velocity.

3) Revenue per day
   - Visualization type: Line chart
   - Fields: Axis = `Date[Date]`, Values = `Total Revenue` (or Daily Revenue aggregated)
   - DAX: `Total Revenue` (use Date axis to render daily aggregation)
   - Business purpose: monetization trend and correlation with rides.

4) Top 5 city (by trips or revenue)
   - Visualization type: Bar chart (horizontal)
   - Fields: `City[City]` (axis), Values = `Trips Count` or `Total Revenue`
   - Business purpose: identify geographic concentration and prioritize expansion.

5) Top 10 driver ranking
   - Visualization type: Table
   - Fields: `Driver[DriverName]`, `Driver[Rides]`, `Driver[Revenue]`, `Driver[AvgDuration]`, `Driver[TopDriverFlag]`
   - DAX: none beyond driver aggregates (computed in PQ or using measures)
   - Business purpose: identify high-performers and retention targets.

Page: Revenue Analytics
1) Revenue per day
   - Visualization type: Line chart
   - Fields: Axis = `Date[Date]`, Values = `Total Revenue`
   - Business purpose: detect revenue seasonality and anomalies.

2) Revenue by city
   - Visualization type: Clustered bar chart (city by revenue)
   - Fields: Axis = `City[City]`, Legend = none or `trip_type`, Values = `Total Revenue`
   - Business purpose: compare monetization across cities.

3) Trip revenue distribution
   - Visualization type: Histogram (or column chart binned by revenue)
   - Fields: Value = `Trips[revenue_cop]` (use binning)
   - Business purpose: reveal ticket-size distribution and outliers.

4) KPI card: Average revenue per ride
   - Visualization type: Card
   - Fields: `Average Revenue per Ride`

Page: Customer Analytics
1) Top customers table
   - Visualization type: Table
   - Fields: `Customer[CustomerName]`, `Customer[Rides]`, `Customer[Revenue]`, `Customer[RepeatCustomerFlag]`
   - Business purpose: select loyalty candidates.

2) Rides per day (filtered by customer)
   - Visualization type: Line chart
   - Fields: Axis = `Date[Date]`, Values = `Rides Per Day`, Legend = `Customer[RepeatCustomerFlag]` optional

3) Peak demand hour
   - Visualization type: Column chart
   - Fields: Axis = `Trips[hour]`, Values = COUNT of `Trips` (completed)
   - Business purpose: schedule campaigns in high-opportunity windows.

4) Stacked bar by city and trip status
   - Visualization type: Stacked bar chart
   - Fields: Axis = `City[City]`, Legend = `Trips[status]`, Values = COUNTROWS(Trips)
   - Business purpose: compare cancellation or failed-trip shares across geos.

5) Repeat customer share metric
   - Visualization type: Card + donut chart (share)
   - Fields: `Repeat Customer Rides (%)`
   - Business purpose: shows retention effectiveness.

Page: Driver Performance
1) Top drivers ranking table
   - Visualization type: Table
   - Fields: `Driver[DriverName]`, `Driver[Rides]`, `Driver[Revenue]`, `Driver[Share]` (share = driver rides / total rides)

2) Rides per driver bar chart
   - Visualization type: Bar chart
   - Fields: Axis = `Driver[DriverName]`, Values = `Driver[Rides]` (top N slicer)

3) Share-of-activity donut chart
   - Visualization type: Donut chart
   - Fields: Legend = `Driver[DriverName]` (top 10), Values = `Driver[Rides]`

4) Trips vs average duration scatter
   - Visualization type: Scatter chart
   - Fields: X = `Driver[Rides]`, Y = `Driver[AvgDuration]`, Size = `Driver[Revenue]`, Details = `Driver[DriverName]`
   - Business purpose: identify drivers with high volume but long trips (operational risk) or low efficiency.

Page: Operational Metrics
1) Cancellation rate card
   - Visualization type: Card
   - Fields: `Cancellation Rate (%)`

2) Average trip duration card
   - Visualization type: Card
   - Fields: `Average Trip Duration (min)`

3) Hourly demand column chart
   - Visualization type: Column chart
   - Fields: Axis = `Trips[hour]`, Values = COUNTROWS(Trips filtered to completed)

4) Trips by city map
   - Visualization type: Filled map (or basic map) — requires geocoding; fallback to bar chart if not available
   - Fields: Location = `City[City]`, Size = `Trips Count` or `Total Revenue`

5) Trend comparison lines (rides vs cancellations)
   - Visualization type: Line chart with two measures
   - Fields: Axis = `Date[Date]`, Values = `Rides Per Day`, `Cancellation Count` or `Cancellation Rate`

Suggested filters & sync
- Date range: sync slicer across pages (single selection)
- City: page-level or report-level
- Driver: page filter for driver performance
- Trip status: quick filter on operational page

Exporting design
- Include one page-level button to export `Trips` table to CSV for interview evidence.
