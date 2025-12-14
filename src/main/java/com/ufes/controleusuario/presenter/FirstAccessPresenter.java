package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.IFirstAccessView;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class FirstAccessPresenter {
  private IFirstAccessView view;
  private IUserRepository userRepository;
  private ILoggerService logger;
  private IPasswordValidator passwordValidator;
  private Runnable onSuccess;

  public FirstAccessPresenter(IFirstAccessView view, IUserRepository userRepository, ILoggerService logger,
      IPasswordValidator passwordValidator, Runnable onSuccess) {
    this.view = view;
    this.userRepository = userRepository;
    this.logger = logger;
    this.passwordValidator = passwordValidator;
    this.onSuccess = onSuccess;
    this.view.setCadastrarListener(this::onCadastrar);
    this.view.setVisible(true);
  }

  private void onCadastrar(ActionEvent e) {
    String nome = view.getNome();
    String usuario = view.getUsuario();
    String senha = view.getSenha();
    if (nome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
      view.showMessage("Preencha todos os campos.");
      logger.log("ERRO", "Tentativa de cadastro com campos vazios (First Access).");
      return;
    }
    java.util.List<String> errors = passwordValidator.validate(senha);
    if (!errors.isEmpty()) {
      view.showMessage("Senha inválida:\n" + String.join("\n", errors));
      logger.log("ERRO", "Senha inválida no setup inicial.");
      return;
    }
    try {
      User newAdmin = new User(0, nome, usuario, senha, "ADMIN", "AUTORIZADO", LocalDate.now());
      userRepository.save(newAdmin);
      logger.log("CADASTRO", "Primeiro Administrador cadastrado: " + newAdmin.getNome() + ", (SYSTEM)");
      view.showMessage("Administrador cadastrado com sucesso!");
      view.close();
      if (onSuccess != null) {
        onSuccess.run();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      view.showMessage("Erro ao salvar no banco de dados.");
      logger.log("ERRO", "Falha ao salvar primeiro admin: " + ex.getMessage());
    }
  }
}
