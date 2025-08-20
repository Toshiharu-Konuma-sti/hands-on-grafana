package jp.sios.apisl.handson.grafana.webapp.webui.service;

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

	@Value("${handson.webapp.webapi.host}")
	private String webapiHost = "localhost";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		webUiService = new WebUiServiceImpl(this.restClient);
	}

	@Test
	void testCallRollDiceApi() {
		String testUrl = "http://null/api/dice/v1/roll";
		String testResponse = "1";

		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		mockServer.expect(requestTo(testUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body(testResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.empty();
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.empty();

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals(testResponse, response);
	}

	@Test
	void testCallRollDiceApi_SleepAndLoop() {

		String testUrl = "http://null/api/dice/v1/roll?sleep=1000&loop=5";
		String testResponse = "2";

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
	void testCallRollDiceApi_AllParamsPresent() {
		String testUrl = "http://null/api/dice/v1/roll?sleep=100&loop=2&error=test";
		String testResponse = "5";

		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		mockServer.expect(requestTo(testUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body(testResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.of("100");
		Optional<String> optLoop = Optional.of("2");
		Optional<String> optError = Optional.of("test");

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals(testResponse, response);
	}

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


	@Test
	void testCallRollDiceApi_OnlySleep() {
		String testUrl = "http://null/api/dice/v1/roll?sleep=500";
		String testResponse = "2";

		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		mockServer.expect(requestTo(testUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body(testResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.of("500");
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.empty();

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals(testResponse, response);
	}

	@Test
	void testCallRollDiceApi_OnlyError() {
		String testUrl = "http://null/api/dice/v1/roll?error=fail";
		String testResponse = "error";

		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		mockServer.expect(requestTo(testUrl))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess().body(testResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.empty();
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.of("fail");

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertNotNull(response);
		assertEquals(testResponse, response);
	}

	@Test
	void testGetCurrentUrl_WithQueryString() {
		// Mock UtilEnvInfo.getCurrentUrl to return expected value
		String expectedUrl = "http://localhost:8080/test?param=value";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);

		// Use reflection or a static mock for UtilEnvInfo if possible
		// Here, we simulate the static method
		try (var mocked = mockStatic(jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.class)) {
			mocked.when(() -> jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.getCurrentUrl(mockRequest))
					.thenReturn(expectedUrl);

			String result = webUiService.getCurrentUrl(mockRequest);

			assertNotNull(result);
			assertEquals(expectedUrl, result);
		}
	}

	@Test
	void testGetCurrentUrl_WithoutQueryString() {
		String expectedUrl = "http://localhost:8080/test";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);

		try (var mocked = mockStatic(jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.class)) {
			mocked.when(() -> jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.getCurrentUrl(mockRequest))
					.thenReturn(expectedUrl);

			String result = webUiService.getCurrentUrl(mockRequest);

			assertNotNull(result);
			assertEquals(expectedUrl, result);
		}
	}

	@Test
	void testGetCurrentUrl_ReturnsExpectedUrl() {
		String expectedUrl = "http://localhost:8080/sample";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);

		try (var mocked = mockStatic(jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.class)) {
			mocked.when(() -> jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.getCurrentUrl(mockRequest))
					.thenReturn(expectedUrl);

			String result = webUiService.getCurrentUrl(mockRequest);

			assertNotNull(result);
			assertEquals(expectedUrl, result);
		}
	}

	@Test
	void testGetCurrentUrl_NullReturned() {
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);

		try (var mocked = mockStatic(jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.class)) {
			mocked.when(() -> jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.getCurrentUrl(mockRequest))
					.thenReturn(null);

			String result = webUiService.getCurrentUrl(mockRequest);

			assertNull(result);
		}
	}

	@Test
	void testCallRollDiceApi_ConstructsCorrectUrl() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		String expectedUrl = "http://null/api/dice/v1/roll?sleep=123&loop=456&error=testError";
		String expectedResponse = "7";
		mockServer.expect(requestTo(expectedUrl))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(expectedResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		Optional<String> optSleep = Optional.of("123");
		Optional<String> optLoop = Optional.of("456");
		Optional<String> optError = Optional.of("testError");

		String response = webUiService.callRollDiceApi(optSleep, optLoop, optError);

		assertEquals(expectedResponse, response);
	}

	@Test
	void testCallListDiceApi_ReturnsJSONArray() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		String expectedUrl = "http://null/api/dice/v1/list";
		String jsonResponse = "[{\"id\":1,\"value\":2},{\"id\":2,\"value\":5}]";
		mockServer.expect(requestTo(expectedUrl))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body(jsonResponse));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		JSONArray result = webUiService.callListDiceApi();

		assertNotNull(result);
		assertEquals(2, result.length());
		assertEquals(2, result.getJSONObject(0).getInt("value"));
		assertEquals(5, result.getJSONObject(1).getInt("value"));
	}

	@Test
	void testCallApi_ReturnsDefaultOnException() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		String expectedUrl = "http://null/api/dice/v1/roll";
		mockServer.expect(requestTo(expectedUrl))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withServerError());
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		String response = webUiService.callRollDiceApi(Optional.empty(), Optional.empty(), Optional.empty());

		assertEquals("0", response);
	}

	@Test
	void testCallRollDiceApi_ParameterCombinations() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();

		// Only sleep
		String urlSleep = "http://null/api/dice/v1/roll?sleep=10";
		mockServer.expect(requestTo(urlSleep))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("1"));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);
		assertEquals("1", webUiService.callRollDiceApi(Optional.of("10"), Optional.empty(), Optional.empty()));

		// Only loop
		RestClient.Builder restClientBuilder2 = RestClient.builder();
		MockRestServiceServer mockServer2 = MockRestServiceServer.bindTo(restClientBuilder2).build();
		String urlLoop = "http://null/api/dice/v1/roll?loop=20";
		mockServer2.expect(requestTo(urlLoop))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("2"));
		this.restClient = restClientBuilder2.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);
		assertEquals("2", webUiService.callRollDiceApi(Optional.empty(), Optional.of("20"), Optional.empty()));

		// Only error
		RestClient.Builder restClientBuilder3 = RestClient.builder();
		MockRestServiceServer mockServer3 = MockRestServiceServer.bindTo(restClientBuilder3).build();
		String urlError = "http://null/api/dice/v1/roll?error=err";
		mockServer3.expect(requestTo(urlError))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("3"));
		this.restClient = restClientBuilder3.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);
		assertEquals("3", webUiService.callRollDiceApi(Optional.empty(), Optional.empty(), Optional.of("err")));
	}

	@Test
	void testCallRollDiceApi_EmptyOptionalParams() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
		String expectedUrl = "http://null/api/dice/v1/roll";
		mockServer.expect(requestTo(expectedUrl))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess().body("9"));
		this.restClient = restClientBuilder.build();
		this.webUiService = new WebUiServiceImpl(this.restClient);

		String response = webUiService.callRollDiceApi(Optional.empty(), Optional.empty(), Optional.empty());

		assertEquals("9", response);
	}

	@Test
	void testGetCurrentUrl_DelegatesToUtilEnvInfo() {
		String expectedUrl = "http://localhost/test";
		HttpServletRequest mockRequest = mock(HttpServletRequest.class);

		try (var mocked = mockStatic(jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.class)) {
			mocked.when(() -> jp.sios.apisl.handson.grafana.webapp.webui.util.UtilEnvInfo.getCurrentUrl(mockRequest))
				.thenReturn(expectedUrl);

			String result = webUiService.getCurrentUrl(mockRequest);

			assertEquals(expectedUrl, result);
		}
	}
*/
}
