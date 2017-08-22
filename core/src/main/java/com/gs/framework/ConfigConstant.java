package com.gs.framework;

/**
 * 相关配置项常量
 *
 * @author gao shan
 * @since 1.0.0
 */
public interface ConfigConstant {

    String CONFIG_FILE = "application.properties";

    String JDBC_DRIVER = "gs.framework.jdbc.driver";
    String JDBC_URL = "gs.framework.jdbc.url";
    String JDBC_USERNAME = "gs.framework.jdbc.username";
    String JDBC_PASSWORD = "gs.framework.jdbc.password";

    String APP_BASE_PACKAGE = "gs.framework.app.base_package";
    String APP_JSP_PATH = "gs.framework.app.jsp_path";
    String APP_ASSET_PATH = "gs.framework.app.asset_path";
    String APP_UPLOAD_LIMIT = "gs.framework.app.upload_limit";
}
