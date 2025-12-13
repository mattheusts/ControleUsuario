package com.ufes.controleusuario.repository;

import com.ufes.controleusuario.model.User;
import java.util.List;

public interface IUserRepository {
  void save(User user);

  void update(User user);

  void delete(User user);

  User findById(int id);

  User findByUsername(String username);

  List<User> findAll();

  List<User> findByStatus(String status);

  List<User> findAuthorizedUsers();

  int countAdmins();

  boolean hasUsers();

  User findFirstAdmin();

  void deleteAll();
}
