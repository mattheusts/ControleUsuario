package com.ufes.controleusuario.repository;
import com.ufes.controleusuario.infra.SQLiteConnection;
import com.ufes.controleusuario.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class UserRepositorySQLite implements IUserRepository {
  @Override
  public void save(User user) {
    String sql = "INSERT INTO users(nome, usuario, senha, tipo, status, data_cadastro) VALUES(?, ?, ?, ?, ?, ?)";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, user.getNome());
      pstmt.setString(2, user.getUsuario());
      pstmt.setString(3, user.getSenha());
      pstmt.setString(4, user.getTipo());
      pstmt.setString(5, user.getStatus());
      pstmt.setString(6, user.getDataCadastro() != null ? user.getDataCadastro().toString() : null);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void update(User user) {
    String sql = "UPDATE users SET nome = ?, usuario = ?, senha = ?, tipo = ?, status = ?, data_cadastro = ? WHERE id = ?";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, user.getNome());
      pstmt.setString(2, user.getUsuario());
      pstmt.setString(3, user.getSenha());
      pstmt.setString(4, user.getTipo());
      pstmt.setString(5, user.getStatus());
      pstmt.setString(6, user.getDataCadastro() != null ? user.getDataCadastro().toString() : null);
      pstmt.setInt(7, user.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void delete(User user) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, user.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  @Override
  public User findByUsername(String username) {
    String sql = "SELECT * FROM users WHERE usuario = ?";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, username);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        String dateStr = rs.getString("data_cadastro");
        java.time.LocalDate date = dateStr != null ? java.time.LocalDate.parse(dateStr) : null;
        return new User(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("usuario"),
            rs.getString("senha"),
            rs.getString("tipo"),
            rs.getString("status"),
            date);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  @Override
  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        String dateStr = rs.getString("data_cadastro");
        java.time.LocalDate date = dateStr != null ? java.time.LocalDate.parse(dateStr) : null;
        users.add(new User(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("usuario"),
            rs.getString("senha"),
            rs.getString("tipo"),
            rs.getString("status"),
            date));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }
  @Override
  public int countAdmins() {
    String sql = "SELECT COUNT(*) FROM users WHERE tipo = 'ADMIN'";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }
  @Override
  public boolean hasUsers() {
    String sql = "SELECT 1 FROM users LIMIT 1";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  @Override
  public User findById(int id) {
    String sql = "SELECT * FROM users WHERE id = ?";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        String dateStr = rs.getString("data_cadastro");
        java.time.LocalDate date = dateStr != null ? java.time.LocalDate.parse(dateStr) : null;
        return new User(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("usuario"),
            rs.getString("senha"),
            rs.getString("tipo"),
            rs.getString("status"),
            date);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  @Override
  public List<User> findByStatus(String status) {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users WHERE status = ?";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, status);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String dateStr = rs.getString("data_cadastro");
        java.time.LocalDate date = dateStr != null ? java.time.LocalDate.parse(dateStr) : null;
        users.add(new User(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("usuario"),
            rs.getString("senha"),
            rs.getString("tipo"),
            rs.getString("status"),
            date));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }
  @Override
  public List<User> findAuthorizedUsers() {
    return findByStatus("AUTORIZADO");
  }
  @Override
  public User findFirstAdmin() {
    String sql = "SELECT * FROM users WHERE tipo = 'ADMIN' ORDER BY id ASC LIMIT 1";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      if (rs.next()) {
        String dateStr = rs.getString("data_cadastro");
        java.time.LocalDate date = dateStr != null ? java.time.LocalDate.parse(dateStr) : null;
        return new User(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("usuario"),
            rs.getString("senha"),
            rs.getString("tipo"),
            rs.getString("status"),
            date);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  @Override
  public void deleteAll() {
    String sql = "DELETE FROM users";
    try (Connection conn = SQLiteConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
