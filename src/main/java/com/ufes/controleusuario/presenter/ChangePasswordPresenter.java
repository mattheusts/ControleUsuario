package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.ChangePasswordView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChangePasswordPresenter {
    private ChangePasswordView view;
    private IUserRepository userRepository;
    private ILoggerService logger;
    private IPasswordValidator passwordValidator;
    private User currentUser;

    public ChangePasswordPresenter(ChangePasswordView view,
            IUserRepository userRepository,
            ILoggerService logger,
            IPasswordValidator passwordValidator,
            User currentUser) {
        this.view = view;
        this.userRepository = userRepository;
        this.logger = logger;
        this.passwordValidator = passwordValidator;
        this.currentUser = currentUser;
        initListeners();
    }

    private void initListeners() {
        view.setSaveListener(this::onSave);
        view.setCancelListener(this::onCancel);
        view.setNewPasswordKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength();
            }
        });
    }

    private void onSave(ActionEvent e) {
        String currentPassword = view.getCurrentPassword();
        String newPassword = view.getNewPassword();
        String confirmPassword = view.getConfirmPassword();
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            view.showError("Preencha todos os campos.");
            logger.log("PASSWORD_CHANGE_FAIL", "Tentativa com campos vazios por: " + currentUser.getNome());
            return;
        }
        if (!currentPassword.equals(currentUser.getSenha())) {
            view.showError("Senha atual incorreta.");
            logger.log("PASSWORD_CHANGE_FAIL", "Senha atual incorreta para: " + currentUser.getNome());
            view.clearFields();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            view.showError("A nova senha e a confirmação não conferem.");
            logger.log("PASSWORD_CHANGE_FAIL", "Senhas não conferem para: " + currentUser.getNome());
            return;
        }
        if (newPassword.equals(currentPassword)) {
            view.showError("A nova senha deve ser diferente da senha atual.");
            return;
        }
        java.util.List<String> errors = passwordValidator.validate(newPassword);
        if (!errors.isEmpty()) {
            view.showError("Senha inválida:\n" + String.join("\n", errors));
            logger.log("PASSWORD_CHANGE_FAIL", "Nova senha inválida para: " + currentUser.getNome());
            return;
        }
        try {
            currentUser.setSenha(newPassword);
            userRepository.update(currentUser);
            logger.log("PASSWORD_CHANGE_SUCCESS", "Senha alterada com sucesso por: " + currentUser.getNome());
            view.showSuccess("Senha alterada com sucesso!");
            view.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao alterar senha: " + ex.getMessage());
            logger.log("PASSWORD_CHANGE_FAIL", "Erro ao alterar senha: " + ex.getMessage());
        }
    }

    private void onCancel(ActionEvent e) {
        view.close();
    }

    private void updatePasswordStrength() {
        String newPassword = view.getNewPassword();
        if (newPassword.isEmpty()) {
            view.setPasswordStrength(" ", Color.GRAY);
            return;
        }
        int strength = calculatePasswordStrength(newPassword);
        if (strength < 2) {
            view.setPasswordStrength("Fraca", new Color(231, 76, 60));
        } else if (strength < 4) {
            view.setPasswordStrength("Média", new Color(241, 196, 15));
        } else {
            view.setPasswordStrength("Forte", new Color(46, 204, 113));
        }
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8)
            strength++;
        if (password.length() >= 12)
            strength++;
        if (password.matches(".*[a-z].*"))
            strength++;
        if (password.matches(".*[A-Z].*"))
            strength++;
        if (password.matches(".*\\d.*"))
            strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
            strength++;
        return strength;
    }
}
