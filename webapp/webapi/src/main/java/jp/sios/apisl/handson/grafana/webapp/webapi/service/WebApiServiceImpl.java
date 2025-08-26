package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.exception.HandsOnException;
import jp.sios.apisl.handson.grafana.webapp.webapi.util.UtilEnvInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebApiServiceImpl implements WebApiService {

  private static final String READ_FILE_PATH_IN_LOOP = "application.yml";
  private static final Logger logger = LoggerFactory.getLogger(WebApiServiceImpl.class);
  private final JdbcTemplate jdbcTemplate;

  // {{{ public WebApiServiceImpl(JdbcTemplate jdbcTemplate)
  public WebApiServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  // }}}

  // {{{ public ResponseEntity<Integer> rollDice(Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError)
  public ResponseEntity<Integer> rollDice(Optional<String> optSleep, Optional<String> optLoop, Optional<String> optError) {
    UtilEnvInfo.logStartClassMethod();
    logger.info("The received parameters are: sleep='{}', loop='{}' and error='{}'", optSleep, optLoop, optError);

    this.sleep(optSleep);
    this.loop(optLoop);
    try {
      this.error(optError);
    } catch(HandsOnException ex) {
      logger.error("The exception was happened with error(): '{}'", (Object[]) ex.getStackTrace());
      ResponseEntity<Integer> entity = new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
      return entity;
    }

    int value = this.roll();
    this.insertDice(value);
    ResponseEntity<Integer> entity = new ResponseEntity<>(value, HttpStatus.OK);

    return entity;
  }
  // }}}

  // {{{ private void sleep(Optional<String> optSleep)
  private void sleep(Optional<String> optSleep) {
    UtilEnvInfo.logStartClassMethod();

    if (optSleep.isPresent()) {
      try {
        int milliSecond = Integer.parseInt(optSleep.get());
        logger.warn("!!! The sleep is: {} ms !!!", milliSecond);
        try {
          Thread.sleep(milliSecond);
          logger.warn("!!! The sleep has finnished !!!");
        } catch(InterruptedException ex) {
          logger.error("The exception was happened with sleep(): '{}'", (Object[]) ex.getStackTrace());
        }
      } catch(NumberFormatException ex) {
        logger.error("The processing of sleep was skipped, because the value of parameter was not an integer: '{}'", optSleep.get());
      }
    }
    return;
  }
  // }}}

  // {{{ private void loop(Optional<String> optLoop)
  private void loop(Optional<String> optLoop) {
    UtilEnvInfo.logStartClassMethod();

    if (optLoop.isPresent()) {
      try {
        int loopCount = Integer.parseInt(optLoop.get());
        int interval = loopCount / 5;
        String line = null;
        logger.warn("!!! The loop is: {} count !!!", loopCount);
        for (int i = 1; i <= loopCount; i++) {

          line = this.readFile(WebApiServiceImpl.READ_FILE_PATH_IN_LOOP);
          
          if ((i != 0) && ((i % interval) == 0)) {
            logger.warn("The progress of loop is: {}/{} count", String.format("%,d", i), String.format("%,d", loopCount));
          }
        }
        logger.warn("!!! The loop has finnished !!! : The read text is: '{}'", line);
      } catch(NumberFormatException ex) {
        logger.error("The processing of loop was skipped, because the value of parameter was not an integer: '{}'", optLoop.get());
      }
    }
    return;
  }
  // }}}

  // {{{ private String readFile(String filePath)
  private String readFile(String filePath) {
    String line = null;
    try (InputStream inputStream = new ClassPathResource(filePath).getInputStream()) {
      logger.debug("Successfully loaded a file.");
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      line = reader.readLine();
      logger.debug("Read line: {}", line);
    } catch (IOException ex) {
      logger.error("Failed to load a file: '{}'", ex.getMessage());
    }
    
    return line;
  }
  // }}}

  // {{{ private void error(Optional<String> optError)
  private void error(Optional<String> optError) throws HandsOnException {
    UtilEnvInfo.logStartClassMethod();

    if (optError.isPresent()) {
      logger.error("!!! It received a direction to occur an exception: '{}' !!!", "HandsOnException");
      throw new HandsOnException("It received a direction to occur an exception.");
    }
    return;
  }
  // }}}

  // {{{ private int roll()
  private int roll() {
    UtilEnvInfo.logStartClassMethod();

    int value = this.getRandomNumber(1, 6);
    logger.info("The value of dice is: '{}'", value);

    return value;
  }
  // }}}

  // {{{ private int getRandomNumber(int min, int max)
  private int getRandomNumber(int min, int max) {
    UtilEnvInfo.logStartClassMethod();
    int number = ThreadLocalRandom.current().nextInt(min, max + 1);
    return number;
  }
  // }}}

  // {{{ private void insertDice(int value)
  private void insertDice(int value) {
    UtilEnvInfo.logStartClassMethod();

    String sql = "INSERT INTO dice(value) VALUES(?)";
    logger.info("The sql to execute is: '{}'. And the value to give is: '{}'", sql, value);
    int number = this.jdbcTemplate.update(sql, value);
    logger.info("The record count of the executed sql is: '{}'", number);

    return;
  }
  // }}}

  // {{{ public List<Dice> listDice()
  public List<Dice> listDice() {
    UtilEnvInfo.logStartClassMethod();

    String sql = "SELECT id, value, updated_at FROM dice ORDER BY id DESC;";
    logger.info("The sql to execute is '{}'", sql);

    List<Map<String, Object>> recordSets = this.jdbcTemplate.queryForList(sql);
    List<Dice> list = new ArrayList<>();

    for (Map<String, Object> recset : recordSets) {
      Dice dice = new Dice(
        ((Number)recset.get("id")).intValue(),
        ((Number)recset.get("value")).intValue(),
        ((LocalDateTime)recset.get("updated_at"))
      );
      list.add(dice);
    }
    logger.info("The record count of the executed sql is: '{}'", list.size());

    return list;
  }
  // }}}

}