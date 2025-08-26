package jp.sios.apisl.handson.grafana.webapp.webapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.service.WebApiService;
import jp.sios.apisl.handson.grafana.webapp.webapi.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dice/v1")
public class WebApiController {

  private static final Logger logger = LoggerFactory.getLogger(WebApiController.class);
  private final WebApiService service;

  // {{{ public WebApiController(WebApiService service)
  public WebApiController(WebApiService service) {
    this.service = service;
  }
  // }}}

  @GetMapping(value = {"/roll"})
  // {{{ public ResponseEntity<Integer> rollDice(...)
  public ResponseEntity<Integer> rollDice(
    HttpServletRequest request,
    @RequestParam("sleep") Optional<String> optSleep,
    @RequestParam("loop") Optional<String> optLoop,
    @RequestParam("error") Optional<String> optError) {
    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();
    logger.info("The received parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

    ResponseEntity<Integer> entity = service.rollDice(optSleep, optLoop, optError);

    UtilEnvInfo.logFinishRequest(request);
    return entity;
  }
  // }}}

  @GetMapping(value = {"/list"})
  // {{{ public List<Dice> listDice(HttpServletRequest request)
  public List<Dice> listDice(HttpServletRequest request) {
    UtilEnvInfo.logStartRequest(request);
    UtilEnvInfo.logStartClassMethod();

    List<Dice> list = service.listDice();

    UtilEnvInfo.logFinishRequest(request);
    return list;
  }
  // }}}

}