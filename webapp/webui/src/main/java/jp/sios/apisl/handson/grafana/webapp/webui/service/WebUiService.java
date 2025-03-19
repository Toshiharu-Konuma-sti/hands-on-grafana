package jp.sios.apisl.handson.grafana.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;

public interface WebUiService
{
	public String callRollDiceApi(Optional<String> optDelay, Optional<String> optCode);
	public JSONArray callListDiceApi();
	public String getCurrentUrl(HttpServletRequest request);
}
