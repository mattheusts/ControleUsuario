package com.ufes.controleusuario.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {
  private static final String URL = "jdbc:sqlite:sistema.db";
  private static Connection connection;

  private SQLiteConnection() {
  }

  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      try {
        // Ensure db file is created in root
        connection = DriverManager.getConnection(URL);
        initializeTables();
      } catch (SQLException e) {
        throw new RuntimeException("Error connecting to database", e);
      }
    }
    return connection;
  }

  private static void initializeTables() {
    String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "nome TEXT NOT NULL, " +
        "usuario TEXT UNIQUE NOT NULL, " +
        "senha TEXT NOT NULL, " +
        "tipo TEXT NOT NULL, " +
        "status TEXT NOT NULL, " +
        "data_cadastro TEXT)"; // Storing date as TEXT (ISO 8601)

    String notificationsTable = "CREATE TABLE IF NOT EXISTS notifications (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "user_id INTEGER NOT NULL, " +
        "message TEXT NOT NULL, " +
        "read INTEGER DEFAULT 0, " +
        "created_at TEXT NOT NULL, " +
        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";

    try (Statement stmt = connection.createStatement()) {
      stmt.execute(usersTable);
      stmt.execute(notificationsTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
