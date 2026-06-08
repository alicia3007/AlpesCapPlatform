# DAX measures & calculated columns

All DAX below assumes the imported `Trips` table with cleaned columns and a `Date` dimension table linked on `Trips[trip_date] -> Date[Date]`.

Calculated columns (Power BI / DAX)
- `Trips[TripDateKey]` = DATEVALUE(Trips[trip_date])
- `Trips[IsCompleted]` = IF( Trips[status] = "COMPLETED", 1, 0 )
- `Trips[IsCancelled]` = IF( Trips[status] IN {"CANCELLED","CANCELADO","CANCELADA"}, 1, 0 )

Core measures
- Total Rides (completed)
  Total Rides =
  CALCULATE(
    COUNTROWS(Trips),
    Trips[IsCompleted] = 1
  )

- Total Revenue (COP)
  Total Revenue = SUM(Trips[revenue_cop])

- Revenue per day (average)
  Revenue per Day =
  VAR Days = DISTINCTCOUNT(Date[Date])
  RETURN DIVIDE([Total Revenue], Days, 0)

- Average revenue per ride
  Average Revenue per Ride = DIVIDE([Total Revenue], [Total Rides], 0)

- Cancellation rate (%)
  Cancellation Rate (%) =
  VAR Cancelled = CALCULATE(COUNTROWS(Trips), Trips[IsCancelled] = 1)
  VAR Total = COUNTROWS(Trips)
  RETURN IF(Total = 0, 0, DIVIDE(Cancelled, Total, 0) * 100)

- Average trip duration (minutes)
  Average Trip Duration (min) =
  CALCULATE(
    AVERAGE(Trips[duration_minutes]),
    Trips[IsCompleted] = 1
  )

- Rides per day (dynamic)
  Rides Per Day =
  CALCULATE(
    COUNTROWS(Trips),
    Trips[IsCompleted] = 1,
    ALLEXCEPT(Date, Date[Date])
  )

- Peak demand hour (measure returning hour with max rides)
  Peak Demand Hour =
  VAR HoursTable =
    SUMMARIZE(
      Trips,
      Trips[hour],
      "Rides", CALCULATE(COUNTROWS(Trips), Trips[IsCompleted] = 1)
    )
  VAR TopHour = TOPN(1, HoursTable, [Rides], DESC)
  RETURN CONCATENATEX(TopHour, Trips[hour], ":00", ", ")

- Repeat customer share (% of rides by repeat customers)
  Repeat Customer Rides (%) =
  VAR RidesByRepeat = CALCULATE(COUNTROWS(Trips), RELATED(Customer[RepeatCustomerFlag]) = TRUE())
  RETURN IF([Total Rides] = 0, 0, DIVIDE(RidesByRepeat, [Total Rides], 0) * 100)

Helper measures
- Trips Count (all statuses)
  Trips Count = COUNTROWS(Trips)

- Average revenue per city
  Avg Revenue per City = AVERAGEX(VALUES(City[City]), CALCULATE([Total Revenue]))

Notes & implementation hints
- Use `CALCULATE` filters to restrict measures to completed trips when appropriate (revenue, duration, rides).
- For revenue in production, prefer `Payments` table (DirectQuery) if it exists; for demo use `Trips[revenue_cop]`.
- Keep measure names concise and consistent with `AnalyticsService` KPI keys (e.g., `Total rides` -> `Total Rides`).
