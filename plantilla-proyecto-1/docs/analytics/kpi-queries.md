# KPI Queries

## 1) Total rides
```sql
SELECT COUNT(*) AS total_rides
FROM VIAJES;
```

## 2) Rides per day
```sql
SELECT TRUNC(HORA_INICIO) AS ride_day, COUNT(*) AS rides
FROM VIAJES
GROUP BY TRUNC(HORA_INICIO)
ORDER BY ride_day;
```

## 3) Revenue per day
```sql
SELECT TRUNC(FECHA_PAGO) AS revenue_day, SUM(MONTO) AS revenue
FROM PAGO
GROUP BY TRUNC(FECHA_PAGO)
ORDER BY revenue_day;
```

## 4) Average revenue per ride
```sql
SELECT ROUND(SUM(MONTO) / NULLIF((SELECT COUNT(*) FROM VIAJES), 0), 2) AS average_revenue_per_ride
FROM PAGO;
```

## 5) Most active drivers
```sql
SELECT conductor_id, COUNT(*) AS rides
FROM VIAJES
GROUP BY conductor_id
ORDER BY rides DESC;
```

## 6) Most active customers
```sql
SELECT pasajero_id, COUNT(*) AS rides
FROM VIAJES
GROUP BY pasajero_id
ORDER BY rides DESC;
```

## 7) Cancellation rate
```sql
SELECT ROUND(
         100 * SUM(CASE WHEN ESTADO IN ('CANCELADO', 'CANCELLED', 'CANCELADA') THEN 1 ELSE 0 END)
         / NULLIF(COUNT(*), 0),
         2
       ) AS cancellation_rate
FROM SERVICIO;
```

## 8) Average trip duration
```sql
SELECT ROUND(AVG((hora_fin - hora_inicio) * 24 * 60), 2) AS average_trip_duration_minutes
FROM VIAJES
WHERE hora_inicio IS NOT NULL
  AND hora_fin IS NOT NULL;
```

## 9) Peak demand hours
```sql
SELECT TO_CHAR(hora_inicio, 'HH24') AS hour_of_day,
       COUNT(*) AS rides
FROM VIAJES
GROUP BY TO_CHAR(hora_inicio, 'HH24')
ORDER BY rides DESC;
```

## 10) Trips by city
```sql
SELECT c.nombre AS city, COUNT(*) AS trips
FROM SERVICIO s
JOIN SERVICIO_PUNTO sp ON s.idservicio = sp.idservicio
JOIN PUNTO p ON sp.idpunto = p.idpunto
JOIN CIUDAD c ON p.idciudad = c.idciudad
GROUP BY c.nombre
ORDER BY trips DESC;
```

## MongoDB aggregation examples
```javascript
// Rides per day
[
  { $match: { horaInicio: { $gte: ISODate('2026-05-01'), $lt: ISODate('2026-06-01') } } },
  { $project: { day: { $dateToString: { format: '%Y-%m-%d', date: '$horaInicio' } } } },
  { $group: { _id: '$day', rides: { $sum: 1 } } },
  { $sort: { _id: 1 } }
]

// Peak demand hours
[
  { $match: { horaInicio: { $gte: ISODate('2026-05-01'), $lt: ISODate('2026-06-01') } } },
  { $project: { hour: { $hour: '$horaInicio' } } },
  { $group: { _id: '$hour', rides: { $sum: 1 } } },
  { $sort: { rides: -1 } }
]
```
