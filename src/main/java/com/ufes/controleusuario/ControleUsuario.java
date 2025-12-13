package com.ufes.controleusuario;

import com.ufes.controleusuario.infra.LoggerService;
import com.ufes.controleusuario.infra.MockPasswordValidator;
import com.ufes.controleusuario.infra.SQLiteConnection;
import com.ufes.controleusuario.repository.NotificationRepositorySQLite;
import com.ufes.controleusuario.repository.UserRepositorySQLite;
import com.ufes.controleusuario.presenter.AdminMainPresenter;
import com.ufes.controleusuario.presenter.FirstAccessPresenter;
import com.ufes.controleusuario.presenter.LoginPresenter;
import com.ufes.controleusuario.presenter.MainPresenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.view.AdminMainView;
import com.ufes.controleusuario.view.FirstAccessView;
import com.ufes.controleusuario.view.LoginView;
import javax.swing.SwingUtilities;

public class ControleUsuario {

    private static UserRepositorySQLite userRepo;
    private static NotificationRepositorySQLite notificationRepo;
    private static LoggerService logger;
    private static MockPasswordValidator passwordValidator;

    public static void main(String[] args) {
        // Ensure DB is ready
        try {
            SQLiteConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            initializeServices();

            if (!userRepo.hasUsers()) {
                // Base vazia -> Cadastro do Primeiro Administrador
                new FirstAccessPresenter(new FirstAccessView(), userRepo, logger, passwordValidator, () -> {
                    // Callback: Sucesso no cadastro -> Abre Login
                    openLogin();
                });
            } else {
                // Já tem usuários -> Login
                openLogin();
            }
        });
    }

    private static void initializeServices() {
        userRepo = new UserRepositorySQLite();
        notificationRepo = new NotificationRepositorySQLite();
        logger = new LoggerService();
        passwordValidator = new MockPasswordValidator();
        logger.setFormat("CSV");
    }

    private static void openLogin() {
        LoginView view = new LoginView();
        new LoginPresenter(view, userRepo, logger, (user) -> {
            openMainScreen(user);
        });
    }

    private static void openMainScreen(User user) {
        if (user.isAdmin()) {
            // Administrador -> Abre tela de Admin
            openAdminPanel(user);
        } else {
            // Usuário comum -> Abre tela principal padrão
            new MainPresenter(user);
        }
    }

    private static void openAdminPanel(User user) {
        AdminMainView adminView = new AdminMainView();
        new AdminMainPresenter(
            adminView,
            user,
            userRepo,
            notificationRepo,
            logger,
            passwordValidator,
            () -> {
                // Callback de logout -> Reabre login
                openLogin();
            }
        );
    }
}
