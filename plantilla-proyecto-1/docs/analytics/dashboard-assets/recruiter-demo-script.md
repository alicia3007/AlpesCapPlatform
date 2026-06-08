# Recruiter demo script

Goal
- A 3–5 minute walkthrough to demonstrate the product-thinking, analytics rigour, and technical execution of the Business Analytics module.

Opening (30s)
- Problem statement: "We help a ride-sharing operator understand demand, revenue, and operational health so they can grow safely and allocate supply where it earns most."
- Data sources: operational events in MongoDB (`viajes`, `servicios`) and financial records in Oracle (`PAGO`). For the demo we use the exported `sample_analytics_export.csv` that models these sources.

KPIs monitored (30s)
- Total rides — platform volume baseline.
- Revenue (total & per-day) — monetization lens.
- Average revenue per ride — ticket size and pricing health.
- Cancellation rate — service quality indicator.
- Average trip duration — traffic and routing proxy.
- Peak demand hours & trips by city — where and when to allocate drivers.

Walkthrough (2-3 minutes)
1) Executive Summary: show KPI cards, point to trends for rides and revenue, call out top city and top drivers. Mention how each KPI maps to code: `AnalyticsService` kpis (e.g., `total_rides`, `average_revenue_per_ride`).
2) Revenue page: filter by date range and city, show revenue per day and city breakdown; explain how revenue distribution informs pricing and city expansion.
3) Customer page: show top customers and repeat-customer share; explain which customers to target for retention and how the metric is computed (group by `customer` → repeat = rides > 1).
4) Driver page: show ranking, rides vs duration scatter; explain operational actions (incentives, coaching, reallocation) derived from these visuals.
5) Operational page: show cancellation rate and hourly demand; explain how this supports real-time decisions for surge pricing and driver rebalancing.

Why this demonstrates Business Analytics skills (30s)
- End-to-end: data extraction (Mongo/SQL), transformation (Power Query), modeling (dimensional model), and analytics (DAX + visuals).
- Product impact: ties backend engineering (aggregations in repo) to measurable business outcomes (revenue, retention, service quality).
- Interview-ready artifacts: documented KPIs, DAX measures, PQ steps, and a reproducible PBIX design spec.

Closing (10s)
- Offer to run a short ad-hoc analysis live (e.g., show top drivers in last 30 days) and point to `docs/analytics` for reproducibility.
