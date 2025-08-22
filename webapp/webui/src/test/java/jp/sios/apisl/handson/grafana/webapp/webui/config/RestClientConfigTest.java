package jp.sios.apisl.handson.grafana.webapp.webui.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RestClientConfigTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testRestClientBeanExists() {
    // Verify that the RestClient bean is created and available in the application context
    RestClient restClient = applicationContext.getBean(RestClient.class);
    assertNotNull(restClient, "RestClient bean should not be null");
  }
}