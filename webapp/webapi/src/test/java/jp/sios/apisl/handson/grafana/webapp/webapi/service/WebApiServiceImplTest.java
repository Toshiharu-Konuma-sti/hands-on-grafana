package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;

class WebApiServiceImplTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private WebApiServiceImpl webApiService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRollDice_Success() {
		Optional<String> optSleep = Optional.empty();
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.empty();

		ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

		assertEquals(200, response.getStatusCodeValue());
		assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
	}

	@Test
	void testRollDice_WithError() {
		Optional<String> optSleep = Optional.empty();
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.of("true");

		ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals(0, response.getBody());
	}

	@Test
	void testRollDice_WithSleep() {
		Optional<String> optSleep = Optional.of("100");
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.empty();

		ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

		assertEquals(200, response.getStatusCodeValue());
		assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
	}

	@Test
	void testRollDice_WithInvalidSleep() {
		Optional<String> optSleep = Optional.of("invalid");
		Optional<String> optLoop = Optional.empty();
		Optional<String> optError = Optional.empty();

		ResponseEntity<Integer> response = webApiService.rollDice(optSleep, optLoop, optError);

		assertEquals(200, response.getStatusCodeValue());
		assertTrue(response.getBody() >= 1 && response.getBody() <= 6);
	}

	@Test
	void testListDice() {
		List<Map<String, Object>> mockResult = new ArrayList<>();
		Map<String, Object> record = new HashMap<>();
		record.put("id", 1);
		record.put("value", 5);
		record.put("updated_at", LocalDateTime.now());
		mockResult.add(record);

		when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

		List<Dice> diceList = webApiService.listDice();

		assertEquals(1, diceList.size());
//		assertEquals(5, diceList.get(0).getValue());
	}

	@Test
	void testInsertDice() {
		when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

		webApiService.rollDice(Optional.empty(), Optional.empty(), Optional.empty());

		verify(jdbcTemplate, atLeastOnce()).update(anyString(), anyInt());
	}
}