package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.UserRegistrationView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

public class UserRegistrationPresenter {
  private UserRegistrationView view;
  private IUserRepository userRepository;
  private ILoggerService logger;
  private IPasswordValidator passwordValidator;

  public UserRegistrationPresenter(UserRegistrationView view,
      IUserRepository userRepository,
      ILoggerService logger,
      IPasswordValidator passwordValidator) {
    this.view = view;
    this.userRepository = userRepository;
    this.logger = logger;
    this.passwordValidator = passwordValidator;
    initListeners();
  }

  private void initListeners() {
    view.setCadastrarListener(this::onCadastrar);
    view.setCancelarListener(this::onCancelar);
    view.setSenhaKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        checkPasswordStrength();
      }
    });
  }

  private void checkPasswordStrength() {
    String password = view.getSenha();
    if (password.isEmpty()) {
      view.setPasswordStrength(" ", Color.BLACK);
      return;
    }
    java.util.List<String> errors = passwordValidator.validate(password);
    if (errors.isEmpty()) {
      view.setPasswordStrength("Senha Forte", new Color(46, 125, 50));
    } else {
      String errorMsg = "<html>" + String.join("<br>", errors) + "</html>";
      view.setPasswordStrength(errorMsg, new Color(180, 60, 60));
    }
  }

  private void onCadastrar(ActionEvent e) {
    String nome = view.getNome();
    String username = view.getUsername();
    String senha = view.getSenha();
    String confirmaSenha = view.getConfirmaSenha();
    if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
      view.showError("Preencha todos os campos obrigatórios.");
      return;
    }
    if (userRepository.findByUsername(username) != null) {
      view.showError("Nome de usuário já existe.");
      return;
    }
    if (!senha.equals(confirmaSenha)) {
      view.showError("As senhas não coincidem.");
      return;
    }
    java.util.List<String> errors = passwordValidator.validate(senha);
    if (!errors.isEmpty()) {
      view.showError("Senha inválida:\n" + String.join("\n", errors));
      return;
    }
    User newUser = new User(0, nome, username, senha, "PADRAO", "PENDENTE", LocalDate.now());
    try {
      userRepository.save(newUser);
      logger.log("REGISTRATION_REQUEST", "Novo usuário solicitou cadastro: " + username);
      view.showMessage("Solicitação de cadastro enviada com sucesso! Aguarde aprovação de um administrador.");
      view.close();
    } catch (Exception ex) {
      logger.log("ERROR", "Erro ao solicitar cadastro: " + ex.getMessage());
      view.showError("Erro ao salvar usuário: " + ex.getMessage());
    }
  }

  private void onCancelar(ActionEvent e) {
    view.close();
  }
}
