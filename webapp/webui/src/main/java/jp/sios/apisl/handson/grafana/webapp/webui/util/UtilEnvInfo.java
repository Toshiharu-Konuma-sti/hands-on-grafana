package jp.sios.apisl.handson.grafana.webapp.webui.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 環境情報やリクエスト情報のログ出力を支援するユーティリティクラスです。.
 *
 * <p>主に以下の機能を提供します:
 * <ul>
 *   <li>リクエスト開始・終了時のURLログ出力</li>
 *   <li>クラス名・メソッド名のログ出力</li>
 *   <li>現在のリクエストURLの取得</li>
 * </ul>
 * ログ出力にはSLF4JのLoggerを利用しています。
 * </p>
 */
public class UtilEnvInfo {

  private static final Logger logger = LoggerFactory.getLogger(UtilEnvInfo.getClassName());

  public static void logStartRequest(HttpServletRequest request) {
    UtilEnvInfo.logRequestWithLabel("START", request);
  }

  public static void logFinishRequest(HttpServletRequest request) {
    UtilEnvInfo.logRequestWithLabel("FINISH", request);
  }

  private static void logRequestWithLabel(String label, HttpServletRequest request) {
    String url = UtilEnvInfo.getCurrentUrl(request);
    logger.info("### {} ### {} ###", label, url);
  }

  public static String getCurrentUrl(HttpServletRequest request) {
    String currentUrl = request.getRequestURL().toString();
    return currentUrl;
  }

  public static void logStartClassMethod() {
    String className = UtilEnvInfo.getClassName();
    String methodName = UtilEnvInfo.getMethodName();
    logger.info(">>> calling: {}#{}()", className, methodName);
  }

  private static String getClassName() {
    String className = Thread.currentThread().getStackTrace()[3].getClassName();
    return className;
  }

  private static String getMethodName() {
    String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
    return methodName;
  }

}
