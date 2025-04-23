package jp.sios.apisl.handson.grafana.webapp.webapi.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.service.WebApiService;



class WebApiControllerTest {

	@Mock
	private WebApiService service;

	@Mock
	private HttpServletRequest request;

	@InjectMocks
	private WebApiController controller;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRollDice() {
		// Arrange
		String mockCurrentUrl = "http://localhost:8080";
		Optional<String> optSleep = Optional.of("1000");
		Optional<String> optLoop = Optional.of("5");
		Optional<String> optError = Optional.empty();
		ResponseEntity<Integer> mockResponse = ResponseEntity.ok(42);

		when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
		when(service.rollDice(optSleep, optLoop, optError)).thenReturn(mockResponse);

		// Act
		ResponseEntity<Integer> response = controller.rollDice(request, optSleep, optLoop, optError);

		// Assert
		assertNotNull(response);
		assertEquals(42, response.getBody());
		verify(service, times(1)).rollDice(optSleep, optLoop, optError);
	}

	@Test
	void testListDice() {
		// Arrange
		String mockCurrentUrl = "http://localhost:8080";
		List<Dice> mockDiceList = List.of(new Dice(3, 1, LocalDateTime.of(2025, 3, 1, 12, 34, 56)), new Dice(2, 3, LocalDateTime.of(2025, 2, 1, 12, 34, 56)), new Dice(1, 5, LocalDateTime.of(2025, 1, 1, 12, 34, 56)));

		when(request.getRequestURL()).thenReturn(new StringBuffer(mockCurrentUrl));
		when(service.listDice()).thenReturn(mockDiceList);

		// Act
		List<Dice> result = controller.listDice(request);

		// Assert
		assertNotNull(result);
		assertEquals(3, result.size());
		verify(service, times(1)).listDice();
	}
}