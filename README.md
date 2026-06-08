
# AlpesCab Platform

## Descripción General

AlpesCab Platform es una plataforma de movilidad desarrollada bajo una arquitectura de microservicios orientada a la gestión de viajes, conductores y clientes. El sistema integra tecnologías relacionales y NoSQL para soportar operaciones transaccionales, análisis de datos y visualización de métricas de negocio.

Además de las funcionalidades operativas, el proyecto incorpora una capa de **Business Analytics** que permite monitorear indicadores clave de desempeño (KPIs), comportamiento de usuarios, rendimiento de conductores y métricas financieras mediante APIs analíticas preparadas para herramientas de visualización como Power BI.

---

## Objetivos del Proyecto

* Gestionar viajes, conductores y clientes.
* Implementar una arquitectura escalable basada en servicios.
* Integrar bases de datos Oracle y MongoDB.
* Generar métricas operativas y financieras para la toma de decisiones.
* Exponer información analítica mediante APIs REST.
* Facilitar la construcción de dashboards empresariales en Power BI.

---

## Tecnologías Utilizadas

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Maven
* REST APIs

### Bases de Datos

* Oracle Database
* MongoDB Atlas

### Herramientas

* Git
* GitHub
* Docker
* Postman
* Power BI

---

## Arquitectura

El sistema utiliza una arquitectura multicapa compuesta por:

* Controladores REST
* Servicios de negocio
* Repositorios de persistencia
* Módulo de Analytics
* Integración Oracle-MongoDB

Esta estructura permite separar claramente la lógica de negocio, acceso a datos y procesamiento analítico.

---

## Funcionalidades Principales

### Gestión Operativa

* Registro de clientes.
* Administración de conductores.
* Gestión de viajes.
* Consulta de historial de servicios.
* Persistencia híbrida Oracle-MongoDB.

### Business Analytics

El sistema incorpora un módulo analítico que calcula y expone métricas para el análisis del negocio.

#### KPIs Generales

* Total de viajes.
* Ingresos totales.
* Ingreso promedio por viaje.
* Tasa de cancelación.
* Duración promedio de los viajes.

#### Análisis de Conductores

* Conductores con mayor número de viajes.
* Conductores con mayores ingresos generados.
* Distribución de actividad por conductor.

#### Análisis de Clientes

* Clientes más activos.
* Frecuencia de uso.
* Segmentación por comportamiento.

#### Análisis Operacional

* Horas de mayor demanda.
* Distribución de viajes por ciudad.
* Tendencias de utilización de la plataforma.

#### Insights Empresariales

* Detección de patrones de uso.
* Indicadores para toma de decisiones.
* Preparación de datos para visualización ejecutiva.

---

## Endpoints Analíticos

| Endpoint                  | Descripción                |
| ------------------------- | -------------------------- |
| /api/analytics/dashboard  | Resumen ejecutivo          |
| /api/analytics/kpis       | KPIs principales           |
| /api/analytics/revenue    | Métricas financieras       |
| /api/analytics/drivers    | Rendimiento de conductores |
| /api/analytics/customers  | Comportamiento de clientes |
| /api/analytics/operations | Métricas operativas        |
| /api/analytics/behavior   | Análisis de comportamiento |
| /api/analytics/insights   | Insights empresariales     |

---

## Integración con Power BI

El proyecto fue diseñado para integrarse con Power BI mediante exportaciones analíticas y APIs REST.

Los datos permiten construir dashboards orientados a:

* Executive Summary
* Revenue Analysis
* Driver Performance
* Customer Behavior
* Operational Metrics

Esta integración facilita la exploración visual de indicadores clave y la generación de reportes para la toma de decisiones.

---

## Aprendizajes Obtenidos

Durante el desarrollo del proyecto se aplicaron conceptos de:

* Diseño e implementación de APIs REST.
* Arquitecturas empresariales con Spring Boot.
* Integración de bases de datos SQL y NoSQL.
* Diseño de KPIs de negocio.
* Business Intelligence.
* Data Analytics.
* Modelado y agregación de datos.
* Preparación de datos para herramientas de visualización.

