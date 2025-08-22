package jp.sios.apisl.handson.grafana.webapp.webui;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class ApplicationTest {

  @Test
  void contextLoads() {
  }

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

  @Test
  void mainRunsWithoutArguments() {
    String[] args = {};
    try (var mocked = mockStatic(SpringApplication.class)) {
      Application.main(args);
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

  @Test
  void mainRunsWithArguments() {
    String[] args = {"--spring.profiles.active=test"};
    try (var mocked = mockStatic(SpringApplication.class)) {
      Application.main(args);
      mocked.verify(() -> SpringApplication.run(Application.class, args));
    }
  }

}
