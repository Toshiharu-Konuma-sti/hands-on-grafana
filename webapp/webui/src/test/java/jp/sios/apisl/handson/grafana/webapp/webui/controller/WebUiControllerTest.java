package jp.sios.apisl.handson.grafana.webapp.webui.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;
import jp.sios.apisl.handson.grafana.webapp.webui.service.WebUiService;

class WebUiControllerTest {

  @Mock
  private WebUiService service;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private WebUiController controller;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() {
    // Arrange
    ModelAndView model = new ModelAndView();
    Optional<String> optSleep = Optional.of("1000");
    Optional<String> optLoop = Optional.of("5");
    Optional<String> optError = Optional.of("false");

    String mockDice = "6";
    JSONArray mockDiceList = new JSONArray("[1, 2, 3, 4, 5, 6]");
    String mockCurrentUrl = "http://localhost:8080";

    when(service.callRollDiceApi(optSleep, optLoop, optError)).thenReturn(mockDice);
    when(service.callListDiceApi()).thenReturn(mockDiceList);
    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.getCurrentUrl(request)).thenReturn(mockCurrentUrl);

    // Act
    ModelAndView result = controller.index(request, model, optSleep, optLoop, optError);

    // Assert
    assertEquals("index", result.getViewName());
    assertEquals(mockDice, result.getModel().get("dice"));
    assertEquals(mockDiceList, result.getModel().get("list"));
    assertEquals(mockCurrentUrl, result.getModel().get("cUrl"));

    verify(service).callRollDiceApi(optSleep, optLoop, optError);
    verify(service).callListDiceApi();
    verify(service).getCurrentUrl(request);
  }
/*
  @Test
  void testIndexWithEmptyParams() {
    ModelAndView model = new ModelAndView();
    Optional<String> optSleep = Optional.empty();
    Optional<String> optLoop = Optional.empty();
    Optional<String> optError = Optional.empty();

    String mockDice = "3";
    JSONArray mockDiceList = new JSONArray("[1,2,3]");
    String mockCurrentUrl = "http://localhost:8080";

    when(service.callRollDiceApi(optSleep, optLoop, optError)).thenReturn(mockDice);
    when(service.callListDiceApi()).thenReturn(mockDiceList);
    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.getCurrentUrl(request)).thenReturn(mockCurrentUrl);

    ModelAndView result = controller.index(request, model, optSleep, optLoop, optError);

    assertEquals("index", result.getViewName());
    assertEquals(mockDice, result.getModel().get("dice"));
    assertEquals(mockDiceList, result.getModel().get("list"));
    assertEquals(mockCurrentUrl, result.getModel().get("cUrl"));

    verify(service).callRollDiceApi(optSleep, optLoop, optError);
    verify(service).callListDiceApi();
    verify(service).getCurrentUrl(request);
  }

  @Test
  void testIndexWithNullModel() {
    Optional<String> optSleep = Optional.of("500");
    Optional<String> optLoop = Optional.of("2");
    Optional<String> optError = Optional.of("true");

    String mockDice = "1";
    JSONArray mockDiceList = new JSONArray("[1]");
    String mockCurrentUrl = "http://localhost:8080/test";

    when(service.callRollDiceApi(optSleep, optLoop, optError)).thenReturn(mockDice);
    when(service.callListDiceApi()).thenReturn(mockDiceList);
    when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
    when(service.getCurrentUrl(request)).thenReturn(mockCurrentUrl);

    // Since model is null, this will throw NullPointerException
    // We expect this behavior as per the current implementation
    assertThrows(NullPointerException.class, () -> {
      controller.index(request, null, optSleep, optLoop, optError);
    });
  }
*/
}

