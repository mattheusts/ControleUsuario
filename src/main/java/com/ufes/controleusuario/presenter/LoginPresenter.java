package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.view.LoginView;
import java.awt.event.ActionEvent;

public class LoginPresenter {
  private LoginView view;
  private IUserRepository userRepository;
  private ILoggerService logger;

  private java.util.function.Consumer<User> onLoginSuccess;

  public LoginPresenter(LoginView view, IUserRepository userRepository, ILoggerService logger,
      java.util.function.Consumer<User> onLoginSuccess) {
    this.view = view;
    this.userRepository = userRepository;
    this.logger = logger;
    this.onLoginSuccess = onLoginSuccess;

    this.view.setLoginListener(this::onLogin);
    this.view.setRegisterListener(this::onRegister);

    this.view.setVisible(true);
  }

  private void onLogin(ActionEvent e) {
    String username = view.getUsername();
    String password = view.getPassword();

    User user = userRepository.findByUsername(username);

    if (user != null && user.getSenha().equals(password)) { // In real app, verify hash
      if (!user.isAuthorized()) {
        view.showMessage("Usuário ainda não autorizado pelo administrador.");
        logger.log("LOGIN_FAIL", "Tentativa de login não autorizada: " + username);
        return;
      }
      view.showMessage("Login realizado com sucesso! Bem-vindo " + user.getNome());
      logger.log("LOGIN_SUCCESS", "Usuário logado: " + username);
      view.dispose(); // Close login
      if (onLoginSuccess != null) {
        onLoginSuccess.accept(user);
      }
    } else {
      view.showMessage("Credenciais inválidas.");
      logger.log("LOGIN_FAIL", "Credenciais inválidas para: " + username);
    }
  }

  private void onRegister(ActionEvent e) {
    view.showMessage("A funcionalidade de cadastro abriria aqui.");
  }
}
