package jp.sios.apisl.handson.grafana.webapp.webapi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class UtilEnvInfoTest {

  @Test
  void testUtilEnvInfoBeanExists() {
    var utilEnvInfo = new UtilEnvInfo();
    assertNotNull(utilEnvInfo, "UtilEnvInfo bean should not be null");
  }

}