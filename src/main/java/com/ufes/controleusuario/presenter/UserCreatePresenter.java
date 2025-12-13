package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.UserCreateView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

public class UserCreatePresenter {

    private UserCreateView view;
    private IUserRepository userRepository;
    private ILoggerService logger;
    private IPasswordValidator passwordValidator;
    private User currentAdmin;
    private Runnable onSuccess;

    public UserCreatePresenter(UserCreateView view,
                               IUserRepository userRepository,
                               ILoggerService logger,
                               IPasswordValidator passwordValidator,
                               User currentAdmin,
                               Runnable onSuccess) {
        this.view = view;
        this.userRepository = userRepository;
        this.logger = logger;
        this.passwordValidator = passwordValidator;
        this.currentAdmin = currentAdmin;
        this.onSuccess = onSuccess;

        initListeners();
        configurePermissions();
    }

    private void configurePermissions() {
        User firstAdmin = userRepository.findFirstAdmin();
        boolean isFirstAdmin = (firstAdmin != null && firstAdmin.getId() == currentAdmin.getId());

        if (!isFirstAdmin) {
            view.setAdminOptionVisible(false);
        }
    }

    private void initListeners() {
        view.setCadastrarListener(this::onCadastrar);
        view.setLimparListener(this::onLimpar);
        view.setCancelarListener(this::onCancelar);

        view.setSenhaKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updatePasswordStrength();
            }
        });
    }

    private void onCadastrar(ActionEvent e) {
        String nome = view.getNome();
        String username = view.getUsername();
        String senha = view.getSenha();
        String confirmaSenha = view.getConfirmaSenha();
        String perfil = view.getPerfil();

        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
            view.showError("Preencha todos os campos obrigatórios.");
            logger.log("CREATE_USER_FAIL", "Tentativa de cadastro com campos vazios por: " + currentAdmin.getNome());
            return;
        }

        if (!senha.equals(confirmaSenha)) {
            view.showError("As senhas não conferem.");
            logger.log("CREATE_USER_FAIL", "Senhas não conferem - tentativa por: " + currentAdmin.getNome());
            return;
        }

        if (!passwordValidator.validate(senha)) {
            view.showError("Senha inválida. A senha deve ter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas e números.");
            logger.log("CREATE_USER_FAIL", "Senha inválida para usuário: " + username);
            return;
        }

        if (userRepository.findByUsername(username) != null) {
            view.showError("Nome de usuário já existe. Escolha outro.");
            logger.log("CREATE_USER_FAIL", "Username duplicado: " + username);
            return;
        }

        try {
            User newUser = new User(
                0,
                nome,
                username,
                senha,
                perfil,
                "AUTORIZADO",
                LocalDate.now()
            );

            userRepository.save(newUser);

            logger.log("CREATE_USER_SUCCESS", "Usuário criado: " + username + " (Tipo: " + perfil + ") por: " + currentAdmin.getNome());
            view.showSuccess("Usuário cadastrado com sucesso!");
            view.clearFields();

            if (onSuccess != null) {
                onSuccess.run();
            }

            view.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Erro ao cadastrar usuário: " + ex.getMessage());
            logger.log("CREATE_USER_FAIL", "Erro ao salvar usuário: " + ex.getMessage());
        }
    }

    private void onLimpar(ActionEvent e) {
        view.clearFields();
    }

    private void onCancelar(ActionEvent e) {
        view.close();
    }

    private void updatePasswordStrength() {
        String senha = view.getSenha();

        if (senha.isEmpty()) {
            view.setPasswordStrength(" ", Color.GRAY);
            return;
        }

        int strength = calculatePasswordStrength(senha);

        if (strength < 2) {
            view.setPasswordStrength("Fraca", new Color(200, 50, 50));
        } else if (strength < 4) {
            view.setPasswordStrength("Média", new Color(200, 150, 50));
        } else {
            view.setPasswordStrength("Forte", new Color(50, 150, 50));
        }
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength++;

        return strength;
    }
}
