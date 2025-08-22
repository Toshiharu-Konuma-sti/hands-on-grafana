package jp.sios.apisl.handson.grafana.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;

public interface WebUiService
{
    public String callRollDiceApi(Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError);
    public JSONArray callListDiceApi();
    public String getCurrentUrl(HttpServletRequest request);
}
