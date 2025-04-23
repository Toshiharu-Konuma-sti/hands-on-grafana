package jp.sios.apisl.handson.grafana.webapp.webapi.entity;

import java.time.LocalDateTime;

public record Dice(int id, int value, LocalDateTime updateAt) {
}
