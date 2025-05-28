package jp.sios.apisl.handson.grafana.webapp.webui.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilEnvInfo
{

	private static final Logger logger = LoggerFactory.getLogger(UtilEnvInfo.getClassName());


	public static void logStartRequest(HttpServletRequest request)
	{
		UtilEnvInfo.logRequestWithLabel("START", request);
	}

	public static void logFinishRequest(HttpServletRequest request)
	{
		UtilEnvInfo.logRequestWithLabel("FINISH", request);
	}

	private static void logRequestWithLabel(String label, HttpServletRequest request)
	{
		String url = UtilEnvInfo.getCurrentUrl(request);
		logger.info("### {} ### {} ###", label, url);
	}

	public static String getCurrentUrl(HttpServletRequest request)
	{
		String currentUrl = request.getRequestURL().toString();
		return currentUrl;
	}

	public static void logStartClassMethod() {
		String className = UtilEnvInfo.getClassName();
		String methodName = UtilEnvInfo.getMethodName();
		logger.info(">>> calling: {}#{}()", className, methodName);
	}

	private static String getClassName()
	{
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		return className;
	}

	private static String getMethodName()
	{
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		return methodName;
	}


}
