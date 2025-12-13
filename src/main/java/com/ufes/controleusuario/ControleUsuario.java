package com.ufes.controleusuario;
import com.ufes.controleusuario.infra.ExternalLoggerAdapter;
import com.ufes.controleusuario.infra.MockPasswordValidator;
import com.ufes.controleusuario.infra.SQLiteConnection;
import com.ufes.controleusuario.repository.NotificationRepositorySQLite;
import com.ufes.controleusuario.repository.UserRepositorySQLite;
import com.ufes.controleusuario.presenter.AdminMainPresenter;
import com.ufes.controleusuario.presenter.FirstAccessPresenter;
import com.ufes.controleusuario.presenter.LoginPresenter;
import com.ufes.controleusuario.presenter.UserMainPresenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.view.AdminMainView;
import com.ufes.controleusuario.view.FirstAccessView;
import com.ufes.controleusuario.view.LoginView;
import com.ufes.controleusuario.view.UserMainView;
import javax.swing.SwingUtilities;
public class ControleUsuario {
    private static UserRepositorySQLite userRepo;
    private static NotificationRepositorySQLite notificationRepo;
    private static ExternalLoggerAdapter logger;
    private static MockPasswordValidator passwordValidator;
    public static void main(String[] args) {
        try {
            SQLiteConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        SwingUtilities.invokeLater(() -> {
            initializeServices();
            if (!userRepo.hasUsers()) {
                new FirstAccessPresenter(new FirstAccessView(), userRepo, logger, passwordValidator, () -> {
                    openLogin();
                });
            } else {
                openLogin();
            }
        });
    }
    private static void initializeServices() {
        userRepo = new UserRepositorySQLite();
        notificationRepo = new NotificationRepositorySQLite();
        logger = new ExternalLoggerAdapter();
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
            openAdminPanel(user);
        } else {
            openUserPanel(user);
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
                openLogin();
            }
        );
    }
    private static void openUserPanel(User user) {
        UserMainView userView = new UserMainView();
        new UserMainPresenter(
            userView,
            user,
            userRepo,
            notificationRepo,
            logger,
            passwordValidator,
            () -> {
                openLogin();
            }
        );
    }
}
