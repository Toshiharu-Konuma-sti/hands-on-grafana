package jp.sios.apisl.handson.grafana.webapp.webui.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

class WebUiServiceImplTest {

	@Mock
	private RestClient restClient;

	@Mock
	private HttpServletRequest request;

	private WebUiServiceImpl webUiService;

	@Value("${handson.webapi.host}")
	private String webapiHost = "localhost";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		webUiService = new WebUiServiceImpl(this.restClient);
	}

	@Test
	void testCallRollDiceApi() {

		String testUrl = "http://null/api/dice/v1/roll?sleep=1000&loop=5";
		String testResponse = "4";

        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo(testUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body(testResponse));
        this.restClient = restClientBuilder.build();
        this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.of("1000");
		Optional<String> optLoop = Optional.of("5");
		Optional<String> optError = Optional.empty();

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals(testResponse, response);
	}

	@Test
	void testCallListDiceApi() {

		String testUrl = "http://null/api/dice/v1/list";
		String testResponse = "[{\"id\":2,\"value\":6,\"updateAt\":\"2025-04-01T13:00:00\"},{\"id\":1,\"value\":3,\"updateAt\":\"2025-04-01T12:00:00\"}]";

        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo(testUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body(testResponse));
        this.restClient = restClientBuilder.build();
        this.webUiService = new WebUiServiceImpl(this.restClient);

		JSONArray response = webUiService.callListDiceApi();

		assertNotNull(response);
		assertEquals(2, response.length());
		assertEquals(6, response.getJSONObject(0).getInt("value"));
	}
/*
	@Test
	void testGetCurrentUrl() {
		when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/test"));
		when(request.getQueryString()).thenReturn("param=value");

		String currentUrl = webUiService.getCurrentUrl(request);

		assertNotNull(currentUrl);
		assertEquals("http://localhost:8080/test?param=value", currentUrl);
	}
*/

	@Test
	void testCallApiHandlesException() {
//		when(restClient.get().uri(anyString()).retrieve().body(String.class)).thenThrow(new RuntimeException("API error"));
//		String response = webUiService.callRollDiceApi(Optional.empty(), Optional.empty(), Optional.empty());

		String testUrl = "http://null/api/dice/v1/roll?sleep=1000&loop=5";
		String testResponse = "4";

		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		mockServer.expect(requestTo(testUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withServerError().body(testResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.of("1000");
		Optional<String> optLoop = Optional.of("5");
		Optional<String> optError = Optional.empty();

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals("0", response); // Default body value
	}
}