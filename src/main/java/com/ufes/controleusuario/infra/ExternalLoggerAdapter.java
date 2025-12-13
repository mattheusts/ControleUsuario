package com.ufes.controleusuario.infra;

import com.ufes.controleusuario.service.ILoggerService;
import com.matheus.log.ILog;
import com.matheus.log.LogFactory;
import java.time.LocalDateTime;

public class ExternalLoggerAdapter implements ILoggerService {
    private ILog logInstance;
    private String format = "CSV";
    private static final String LOG_FILE_CSV = "log.csv";
    private static final String LOG_FILE_JSON = "log.json";

    public ExternalLoggerAdapter() {
        this.logInstance = LogFactory.createLog(LOG_FILE_CSV);
    }

    public void setFormat(String format) {
        if ("JSONL".equalsIgnoreCase(format) || "JSON".equalsIgnoreCase(format)) {
            this.format = "JSONL";
            this.logInstance = LogFactory.createLog(LOG_FILE_JSON);
        } else {
            this.format = "CSV";
            this.logInstance = LogFactory.createLog(LOG_FILE_CSV);
        }
    }

    public String getFormat() {
        return format;
    }

    @Override
    public void log(String type, String message) {
        String timestamp = LocalDateTime.now().toString();
        String logEntry;
        if ("JSONL".equals(format)) {
            logEntry = "{\"timestamp\": \"" + timestamp + "\", \"type\": \"" + type + "\", \"message\": \"" + message
                    + "\"}";
        } else {
            logEntry = timestamp + ";" + type + ";" + message;
        }
        logInstance.escrever(logEntry);
    }
}
