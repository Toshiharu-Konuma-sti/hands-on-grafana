package jp.sios.apisl.handson.grafana.webapp.webapi.service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import jp.sios.apisl.handson.grafana.webapp.webapi.entity.Dice;
import jp.sios.apisl.handson.grafana.webapp.webapi.util.UtilEnvInfo;

@Service
public class WebApiServiceImpl implements WebApiService
{

	private static final Logger logger = LoggerFactory.getLogger(WebApiServiceImpl.class);
	private final JdbcTemplate jdbcTemplate;

	// {{{ public WebApiServiceImpl(JdbcTemplate jdbcTemplate)
	public WebApiServiceImpl(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}
	// }}}

	// {{{ public ResponseEntity<Integer> rollDice(Optional<String> optDelay, Optional<String> optCode)
	public ResponseEntity<Integer> rollDice(Optional<String> optDelay, Optional<String> optCode)
	{
		UtilEnvInfo.logStartClassMethod();
		logger.info("The received parameters are: delay='{}', code='{}'", optDelay, optCode);

		this.delay(optDelay);
		HttpStatusCode httpStatus = this.makeHttpStatus(optCode);
		int value = 0;
		if (httpStatus == HttpStatus.OK) {
			value = this.roll(httpStatus);
			this.insertDice(value);
		}
		ResponseEntity entity = new ResponseEntity<>(value, httpStatus);

		return entity;
	}
	// }}}

	// {{{ private void delay(Optional<String> optDelay)
	private void delay(Optional<String> optDelay)
	{
		UtilEnvInfo.logStartClassMethod();

		if (optDelay.isPresent()) {
			try {
				int delay = Integer.parseInt(optDelay.get());
				logger.warn("!!! The delay is: {} ms !!!", delay);
				try {
					Thread.sleep(delay);
					logger.warn("!!! The delay has finnished !!!");
				} 
				catch(InterruptedException ex) {
					logger.error("The exception was happened with sleep(): '{}'", ex);
				}
			}
			catch(NumberFormatException ex) {
				logger.error("The processing of delay was skipped, because the value of parameter was not a number: '{}'", optDelay.get());
			}
		}
		return;
	}
	// }}}

	// {{{ private HttpStatusCode makeHttpStatus(Optional<String> optCode)
	private HttpStatusCode makeHttpStatus(Optional<String> optCode)
	{
		UtilEnvInfo.logStartClassMethod();

		HttpStatusCode httpStatus = HttpStatus.OK;

		if (optCode.isPresent()) {
			String code = optCode.get();
			logger.info("The code in parameter is: '{}'", code);
			if (code.equals("4")) {
				httpStatus = HttpStatus.FORBIDDEN;
			}
			else if (code.equals("5")) {
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			if (httpStatus == HttpStatus.OK) {
				logger.warn("The retruning an intended http status code was skipped, because a code is without regulations: '{}'", code);
			} else {
				logger.error("!!! The intended http status code will be occured: '{}' !!!", httpStatus);
			}
		} else {
			logger.info("The returned http status code will be: '{}'", httpStatus);
		}

		return httpStatus;
	}
	// }}}

	// {{{ private int roll(HttpStatusCode httpStatus)
	private int roll(HttpStatusCode httpStatus)
	{
		UtilEnvInfo.logStartClassMethod();

		int value = this.getRandomNumber(1, 6);
		logger.info("The value of dice is: '{}'", value);

		return value;
	}
	// }}}

	// {{{ private int getRandomNumber(int min, int max)
	private int getRandomNumber(int min, int max)
	{
		UtilEnvInfo.logStartClassMethod();
		int number = ThreadLocalRandom.current().nextInt(min, max + 1);
		return number;
	}
	// }}}

	// {{{ private void insertDice(int value)
	private void insertDice(int value)
	{
		UtilEnvInfo.logStartClassMethod();

		String sql = "INSERT INTO dice(value) VALUES(?)";
		logger.info("The sql to execute is: '{}'. And the value to give is: '{}'", sql, value);
		int number = this.jdbcTemplate.update(sql, value);
		logger.info("The record count of the executed sql is: '{}'", number);

		return;
	}
	// }}}

	// {{{ public List<Dice> listDice()
	public List<Dice> listDice()
	{
		UtilEnvInfo.logStartClassMethod();

		String sql = "SELECT id, value, updated_at FROM dice ORDER BY id DESC;";
		logger.info("The sql to execute is '{}'", sql);
		List<Dice> list = this.jdbcTemplate.query(
			sql, (rs, rowNum) -> new Dice(
				rs.getInt("id"),
				rs.getInt("value"),
				rs.getObject("updated_at", LocalDateTime.class)
			)
		);
		logger.info("The record count of the executed sql is: '{}'", list.size());

		return list;
	}
	// }}}

}













