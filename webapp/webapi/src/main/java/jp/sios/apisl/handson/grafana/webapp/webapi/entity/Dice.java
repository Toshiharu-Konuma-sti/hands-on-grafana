package jp.sios.apisl.handson.grafana.webapp.webapi.entity;

import java.time.LocalDateTime;

public record Dice(Integer id, Integer value, LocalDateTime updateAt) {
}
