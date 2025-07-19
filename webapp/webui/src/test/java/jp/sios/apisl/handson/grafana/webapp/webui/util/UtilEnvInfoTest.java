package jp.sios.apisl.handson.grafana.webapp.webui.util;

import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilEnvInfoTest {

	@Test
	void testGetCurrentUrl() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		StringBuffer url = new StringBuffer("http://localhost/test/path");
		when(request.getRequestURL()).thenReturn(url);

		String result = UtilEnvInfo.getCurrentUrl(request);

		assertEquals("http://localhost/test/path", result);
	}

	@Test
	void testLogStartRequestDoesNotThrow() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/start"));

		assertDoesNotThrow(() -> UtilEnvInfo.logStartRequest(request));
	}

	@Test
	void testLogFinishRequestDoesNotThrow() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/finish"));

		assertDoesNotThrow(() -> UtilEnvInfo.logFinishRequest(request));
	}

	@Test
	void testLogStartClassMethodDoesNotThrow() {
		assertDoesNotThrow(UtilEnvInfo::logStartClassMethod);
	}

	@Test
	void testMyTest() {
		var obj = new UtilEnvInfo();
		obj.logStartClassMethod();
	}

	@Test
	void testLogRequestWithLabelDoesNotThrow() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/label"));
		var method = UtilEnvInfo.class.getDeclaredMethod("logRequestWithLabel", String.class, HttpServletRequest.class);
		method.setAccessible(true);
		assertDoesNotThrow(() -> method.invoke(null, "LABEL", request));
	}

	@Test
	void testGetCurrentUrlWithEmptyUrl() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer(""));
		String result = UtilEnvInfo.getCurrentUrl(request);
		assertEquals("", result);
	}

	@Test
	void testGetCurrentUrlWithNullRequest() {
		assertThrows(NullPointerException.class, () -> UtilEnvInfo.getCurrentUrl(null));
	}
}