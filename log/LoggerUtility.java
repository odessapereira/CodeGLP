package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtility {
    private static final String TEXT_LOG_CONFIG = "src/log/log4j-text.properties";

    public static Logger getLogger(Class<?> logClass) {
        PropertyConfigurator.configure(TEXT_LOG_CONFIG);
        return Logger.getLogger(logClass);
    }
}
