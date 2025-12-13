package com.ufes.controleusuario.infra;

import com.ufes.controleusuario.service.ILoggerService;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class LoggerService implements ILoggerService {
  private String format = "JSONL";

  public void setFormat(String format) {
    if ("JSONL".equalsIgnoreCase(format)) {
      this.format = "JSONL";
    } else {
      this.format = "CSV";
    }
  }

  public String getFormat() {
    return format;
  }

  @Override
  public void log(String type, String message) {
    String logFile = "log." + (format.equalsIgnoreCase("CSV") ? "csv" : "jsonl");
    try (FileWriter fw = new FileWriter(logFile, true);
        PrintWriter out = new PrintWriter(fw)) {

      if ("CSV".equalsIgnoreCase(format)) {
        out.println(LocalDateTime.now() + ";" + type + ";" + message);
      } else {
        out.println("{\"timestamp\": \"" + LocalDateTime.now() + "\", \"type\": \"" + type + "\", \"message\": \""
            + message + "\"}");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
