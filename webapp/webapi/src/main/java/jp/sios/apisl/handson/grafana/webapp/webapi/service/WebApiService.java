package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;

public interface WebApiService
{
	public ResponseEntity<Integer> rollDice(Optional<String> optDelay, Optional<String> optCode);
	public List<Dice> listDice();
}
