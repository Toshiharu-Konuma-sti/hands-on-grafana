package jp.sios.apisl.handson.grafana.webapp.webui.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo;
import jp.sios.apisl.handson.grafana.webapp.webui.service.WebUiService;

@Service
public class WebUiServiceImpl implements WebUiService
{

	private static final Logger logger = LoggerFactory.getLogger(WebUiServiceImpl.class);
	private final RestClient restClient;

	@Value("${handson.webapi.host}")
	private String webapiHost;

	// {{{ public WebUiServiceImpl(RestClient restClient)
	public WebUiServiceImpl(RestClient restClient)
	{
		this.restClient = restClient;
	}
	// }}}

	// {{{ public String callRollDiceApi(Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError)
	public String callRollDiceApi(Optional<String> optSleep, Optional<String> optLoop,  Optional<String> optError)
	{
		UtilEnvInfo.logStartClassMethod();
		logger.info("The received request parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

		String path = "/api/dice/v1/roll";
		List<String> paramList = new ArrayList<String>();
		optSleep.ifPresent(sleep -> paramList.add("sleep=" + sleep));
		optLoop.ifPresent(loop -> paramList.add("loop=" + loop));
		optError.ifPresent(error -> paramList.add("error=" + error));
		if (paramList.size() > 0) {
			path += "?" + String.join("&", paramList);
		}

		String body = this.callApi(path, "0");

		return body;
	}
	// }}}

	// {{{ public JSONArray callListDiceApi()
	public JSONArray callListDiceApi()
	{
		UtilEnvInfo.logStartClassMethod();

		String path = "/api/dice/v1/list";
		String body = this.callApi(path, "");

		JSONArray jsonList = new JSONArray(body);
		logger.info("The object converted to json is {}", jsonList);

		return jsonList;
	}
	// }}}

	// {{{ private String callApi(String path, String body)
	private String callApi(String path, String body)
	{
		UtilEnvInfo.logStartClassMethod();

		String url = "http://" + this.webapiHost + path;
		logger.info("The URL to call the API is: '{}'", url);

		try {
			body = this.restClient.get()
				.uri(url)
				.retrieve()
				.body(String.class);
			logger.info("The value recieved from the rolldice api is: '{}'", body);
		}
		catch (HttpClientErrorException | HttpServerErrorException ex) {
			logger.error("!!! Could not get a response from the API, because an exception was happened: '{}' !!!", (Object[]) ex.getStackTrace());
		}

		return body;
	}
	// }}}

	// {{{ public String getCurrentUrl(HttpServletRequest request)
	public String getCurrentUrl(HttpServletRequest request)
	{
		UtilEnvInfo.logStartClassMethod();

		String currentUrl = UtilEnvInfo.getCurrentUrl(request);
		logger.info("The current URL is: {}", currentUrl);

		return currentUrl;
	}
	// }}}

}
