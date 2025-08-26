package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import org.springframework.http.ResponseEntity;

public interface WebApiService {
  public ResponseEntity<Integer> rollDice(Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError);
  public List<Dice> listDice();
}
