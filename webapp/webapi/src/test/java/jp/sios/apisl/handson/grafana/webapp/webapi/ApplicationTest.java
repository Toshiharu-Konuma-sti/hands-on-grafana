package jp.sios.apisl.handson.grafana.webapp.webapi;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

public class ApplicationTest {

  @Test
  void testMainRunsSpringApplication() {
    // Arrange
    String[] args = new String[] {};
    try (var mocked = mockStatic(SpringApplication.class)) {
      // Act
      Application.main(args);
      // Assert
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }
}