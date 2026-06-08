# Dashboard layout & data model

Summary
- Source of truth: the existing analytics module (MongoDB `viajes` + Oracle `PAGO`/`SERVICIO`), and the provided export `sample_analytics_export.csv` used as the demo dataset.
- Modeling approach: canonical fact table `Trips` loaded from `sample_analytics_export.csv` and light dimension tables (Date, City, Driver, Customer). A derived `Payments` table can be created from `Trips` where `revenue_cop` > 0 to emulate `PAGO`.

Tables (Power Query names)
- `Trips` (fact): trip_id, trip_date, city, zone, customer, driver, status, revenue_cop, duration_minutes, hour, trip_type
- `Date` (dim): Date, Year, Month, MonthName, Day, DayOfWeek, IsWeekend, FiscalMonth
- `City` (dim): City, Region (optional), CityRank
- `Driver` (dim): DriverID, DriverName, DriverCategory (derived: top driver flag)
- `Customer` (dim): CustomerID, CustomerName, RepeatCustomerFlag
- `Payments` (fact, optional): PaymentDate, AmountCOP, TripID, PaymentStatus

Relationships
- `Trips[trip_date]` (date) -> `Date[Date]` (1:*). Use truncated date (yyyy-mm-dd) on `Trips.trip_date`.
- `Trips[city]` -> `City[City]` (many-to-one).
- `Trips[driver]` -> `Driver[DriverName]` (many-to-one).
- `Trips[customer]` -> `Customer[CustomerName]` (many-to-one).
- `Payments[PaymentDate]` -> `Date[Date]` (if `Payments` used).

Power Query transformations (step-by-step)
1. Load `sample_analytics_export.csv` as `Trips`.
   - Use Locale = Spanish (Colombia) / UTF-8.
   - Change types: `trip_date` -> Date, `hour` -> Whole Number, `revenue_cop` -> Decimal Number, `duration_minutes` -> Decimal Number.
   - Trim and clean `city`, `driver`, `customer`, `status`, `trip_type`.
   - Create `trip_date_ymd` = Date.ToText([trip_date], "yyyy-MM-dd") if needed for text join.
2. Generate `Date` dimension from `Trips[trip_date]` (range):
   - `Date` = List.Distinct(Trips[trip_date]) -> List.Sort -> Table.FromList -> Add columns: Year = Date.Year(Date), Month = Date.Month(Date), MonthName = Date.ToText(...), Day = Date.Day(...), DayOfWeek = Date.DayOfWeekName(...), IsWeekend = Date.DayOfWeek(... ) >=5
3. Build `City` table: Group `Trips` by `city`, compute `TripsCount` and `RevenueSum`, add `CityRank` = Rank.Descending(RevenueSum).
4. Build `Driver` table: Group `Trips` by `driver`, compute `Rides`, `Revenue`, `AvgDuration`, set `TopDriverFlag` for top 10 drivers.
5. Build `Customer` table: Group `Trips` by `customer`, compute `Rides`, `RepeatCustomerFlag` = Rides > 1.
6. Build `Payments` (optional): Filter `Trips` where `revenue_cop` > 0 and map `trip_date` -> `PaymentDate`, `revenue_cop` -> `AmountCOP`, `trip_id` -> `TripID`.
7. Ensure keys and data types: Date -> Date, numeric measures -> Decimal/Whole Number.

PBIX design specification (high level)
- Theme: dark executive theme, accent color = teal for revenue.
- Page header: persistent date slicer (sync across pages) and KPI card row.
- Page footers: small textual insight box (one-liner) populated from calculated DAX insight measures.
- Export: add a `Export` button that opens data view or links to the CSV.

Notes
- Use the project analytics module as source of truth — keep KPI definitions exactly as implemented in `AnalyticsService` and `AnalyticsRepository`.
- When connecting to production, replace CSV `Trips` with a union of Mongo `viajes` and Oracle `PAGO`/`SERVICIO` sources using DirectQuery or scheduled refresh.
