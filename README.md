# Grafana LGTM Playground

[![GitHub License](https://img.shields.io/github/license/Toshiharu-Konuma-sti/hands-on-grafana?style=flat-square)](https://github.com/Toshiharu-Konuma-sti/hands-on-grafana/blob/main/LICENSE)
[![GitHub last commit](https://img.shields.io/github/last-commit/Toshiharu-Konuma-sti/hands-on-grafana?style=flat-square)](https://github.com/Toshiharu-Konuma-sti/hands-on-grafana/commits)
[![Docker](https://img.shields.io/badge/Docker-Container-blue?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)
[![Grafana](https://img.shields.io/badge/Grafana-LGTM-F46800?style=flat-square&logo=grafana&logoColor=white)](https://grafana.com/)

> **Practical resources and Docker configurations for hands-on learning of the Grafana Observability Stack (Loki, Grafana, Tempo, Mimir).**

## 📖 Overview

This repository is a comprehensive starter kit designed for hands-on learning of the **Grafana LGTM Stack**.
By leveraging Docker Compose, you can instantly spin up a full observability environment on your local machine to experiment with log aggregation, distributed tracing, and metrics collection.



### 🚀 Tech Stack (The LGTM Stack)

- **L**oki: Log aggregation system (like Prometheus, but for logs).
- **G**rafana: The open observability platform for visualization and dashboards.
- **T**empo: High-volume, minimal dependency distributed tracing backend.
- **M**imir: Long-term storage for Prometheus metrics.
- **Application**: Sample application instrumented with OpenTelemetry to generate telemetry data.