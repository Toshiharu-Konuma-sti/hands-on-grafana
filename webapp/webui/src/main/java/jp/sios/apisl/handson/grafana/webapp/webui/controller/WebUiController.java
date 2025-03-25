package jp.sios.apisl.handson.grafana.webapp.webui.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jp.sios.apisl.handson.grafana.webapp.webui.service.WebUiService;
import jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo;

@Controller
public class WebUiController
{

	private static final Logger logger = LoggerFactory.getLogger(WebUiController.class);
	private final WebUiService service;

	// {{{ public WebUiController(WebUiService service)
	public WebUiController(WebUiService service)
	{
		this.service = service;
	}
	// }}}

	@GetMapping(value = {"/"})
	// {{{ public String index(HttpServletRequest request)
	public String index(HttpServletRequest request)
	{
		UtilEnvInfo.logStartRequest(request);
		UtilEnvInfo.logFinishRequest(request);
		return "redirect:/roll";
	}
	// }}}

	@RequestMapping(value = {"/roll"})
	// {{{ public ModelAndView index(...)
	public ModelAndView roll(
		HttpServletRequest request, ModelAndView model,
		@RequestParam("sleep") Optional<String> optSleep,
		@RequestParam("loop") Optional<String> optLoop,
		@RequestParam("code") Optional<String> optCode)
	{
		UtilEnvInfo.logStartRequest(request);
		UtilEnvInfo.logStartClassMethod();
		logger.info("The received request parameters are: sleep='{}', loop='{}' and code='{}'", optSleep, optLoop, optCode);

		String dice = this.service.callRollDiceApi(optSleep, optLoop, optCode);
		JSONArray diceList = this.service.callListDiceApi();
		String currentUrl = this.service.getCurrentUrl(request);

		model.addObject("dice", dice);
		model.addObject("list", diceList);
		model.addObject("cUrl", currentUrl);

		model.setViewName("roll");

		UtilEnvInfo.logFinishRequest(request);
		return model;
	}
	// }}}

	@RequestMapping(value = {"/list"})
	// {{{ public ModelAndView list(...)
	public ModelAndView list(
		HttpServletRequest request, ModelAndView model)
	{
		UtilEnvInfo.logStartRequest(request);
		UtilEnvInfo.logStartClassMethod();

		JSONArray diceList = this.service.callListDiceApi();
		String currentUrl = this.service.getCurrentUrl(request);

		int dice = 0;
		if (diceList.length() > 0) {
			JSONObject row = diceList.getJSONObject(0);
			dice = row.getInt("value");
		}

		model.addObject("dice", dice);
		model.addObject("list", diceList);
		model.addObject("cUrl", currentUrl);

		model.setViewName("list");

		UtilEnvInfo.logFinishRequest(request);
		return model;
	}
	// }}}


}
