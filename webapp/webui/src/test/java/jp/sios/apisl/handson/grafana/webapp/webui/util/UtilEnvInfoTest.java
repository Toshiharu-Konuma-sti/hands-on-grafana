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
	void testGetClassNameReturnsThisClass() throws Exception {
		// Use reflection to access private static method getClassName
		var method = UtilEnvInfo.class.getDeclaredMethod("getClassName");
		method.setAccessible(true);
		String className = (String) method.invoke(null);
		// Should return the test class name since it's called from here
		assertEquals(this.getClass().getName(), className);
	}

	@Test
	void testGetMethodNameReturnsTestMethod() throws Exception {
		// Use reflection to access private static method getMethodName
		var method = UtilEnvInfo.class.getDeclaredMethod("getMethodName");
		method.setAccessible(true);
		String methodName = (String) method.invoke(null);
		// Should return the name of this test method
		assertEquals("testGetMethodNameReturnsTestMethod", methodName);
	}

	@Test
	void testMyTest() {
		var obj = new UtilEnvInfo();
		obj.logStartClassMethod();
	}
}