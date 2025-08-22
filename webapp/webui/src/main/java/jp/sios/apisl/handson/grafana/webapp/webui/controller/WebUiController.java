package jp.sios.apisl.handson.grafana.webapp.webui.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = {"/"})
    // {{{ public ModelAndView index(...)
    public ModelAndView index(
        HttpServletRequest request, ModelAndView model,
        @RequestParam("sleep") Optional<String> optSleep,
        @RequestParam("loop") Optional<String> optLoop,
        @RequestParam("error") Optional<String> optError)
    {
        UtilEnvInfo.logStartRequest(request);
        UtilEnvInfo.logStartClassMethod();
        logger.info("The received request parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

        String dice = this.service.callRollDiceApi(optSleep, optLoop, optError);
        JSONArray diceList = this.service.callListDiceApi();
        String currentUrl = this.service.getCurrentUrl(request);

        model.addObject("dice", dice);
        model.addObject("list", diceList);
        model.addObject("cUrl", currentUrl);

        model.setViewName("index");

        UtilEnvInfo.logFinishRequest(request);
        return model;
    }
    // }}}

}
