package com.ufes.controleusuario.presenter;

import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class AdminMainPresenter {

    private AdminMainView view;
    private User currentUser;
    private User firstAdmin;
    private boolean isFirstAdmin;

    private IUserRepository userRepository;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private IPasswordValidator passwordValidator;

    private UserCreatePresenter userCreatePresenter;
    private UserAuthorizationPresenter userAuthorizationPresenter;
    private UserListPresenter userListPresenter;
    private NotificationSendPresenter notificationSendPresenter;
    private NotificationListPresenter notificationListPresenter;
    private LogConfigPresenter logConfigPresenter;
    private SystemRestorePresenter systemRestorePresenter;

    private Runnable onLogout;

    public AdminMainPresenter(AdminMainView view, User currentUser,
                              IUserRepository userRepository,
                              INotificationRepository notificationRepository,
                              ILoggerService logger,
                              IPasswordValidator passwordValidator,
                              Runnable onLogout) {
        this.view = view;
        this.currentUser = currentUser;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.logger = logger;
        this.passwordValidator = passwordValidator;
        this.onLogout = onLogout;

        this.firstAdmin = userRepository.findFirstAdmin();
        this.isFirstAdmin = (firstAdmin != null && firstAdmin.getId() == currentUser.getId());

        initView();
        initListeners();
        updateNotificationCount();

        logger.log("ACCESS", "Administrador acessou o painel: " + currentUser.getNome());

        this.view.setVisible(true);
        this.view.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initView() {
        view.setUserName(currentUser.getNome());
        view.setUserType(currentUser.isAdmin() ? "Administrador" : "Usuário");

        if (!currentUser.isAdmin()) {
            view.setUserMenuVisible(false);
            view.showError("Acesso negado. Apenas administradores podem acessar esta área.");
            view.close();
            return;
        }

        view.setSystemRestoreEnabled(isFirstAdmin);
        if (!isFirstAdmin) {
            view.setSystemRestoreVisible(false);
        }
    }

    private void initListeners() {
        view.setUserCreateListener(this::onUserCreate);
        view.setUserAuthorizationListener(this::onUserAuthorization);
        view.setUserListListener(this::onUserList);

        view.setNotificationSendListener(this::onNotificationSend);
        view.setNotificationListListener(this::onNotificationList);
        view.setNotificationButtonListener(this::onNotificationList);

        view.setLogConfigListener(this::onLogConfig);
        view.setSystemRestoreListener(this::onSystemRestore);
        view.setLogoutListener(this::onLogout);
        view.setExitListener(this::onExit);
    }

    private void onUserCreate(ActionEvent e) {
        UserCreateView createView = new UserCreateView();
        userCreatePresenter = new UserCreatePresenter(
            createView,
            userRepository,
            logger,
            passwordValidator,
            currentUser,
            this::onUserCreated
        );
        view.addInternalFrame(createView);
        createView.setVisible(true);
        logger.log("NAVIGATION", "Administrador abriu tela de cadastro de usuário");
    }

    private void onUserCreated() {
        view.showSuccess("Usuário cadastrado com sucesso!");
        updateNotificationCount();
    }

    private void onUserAuthorization(ActionEvent e) {
        UserAuthorizationView authView = new UserAuthorizationView();
        userAuthorizationPresenter = new UserAuthorizationPresenter(
            authView,
            userRepository,
            logger,
            currentUser,
            this::refreshViews
        );
        view.addInternalFrame(authView);
        authView.setVisible(true);
        logger.log("NAVIGATION", "Administrador abriu tela de autorização de usuários");
    }

    private void onUserList(ActionEvent e) {
        UserListView listView = new UserListView();
        userListPresenter = new UserListPresenter(
            listView,
            userRepository,
            notificationRepository,
            logger,
            currentUser,
            isFirstAdmin,
            this::refreshViews
        );
        view.addInternalFrame(listView);
        listView.setVisible(true);
        logger.log("NAVIGATION", "Administrador abriu lista de usuários");
    }

    private void onNotificationSend(ActionEvent e) {
        NotificationSendView sendView = new NotificationSendView();
        notificationSendPresenter = new NotificationSendPresenter(
            sendView,
            userRepository,
            notificationRepository,
            logger,
            currentUser,
            this::onNotificationSent
        );
        view.addInternalFrame(sendView);
        sendView.setVisible(true);
        logger.log("NAVIGATION", "Administrador abriu tela de envio de notificações");
    }

    private void onNotificationSent() {
        view.showSuccess("Notificação enviada com sucesso!");
    }

    private void onNotificationList(ActionEvent e) {
        NotificationListView listView = new NotificationListView();
        notificationListPresenter = new NotificationListPresenter(
            listView,
            notificationRepository,
            logger,
            currentUser,
            this::updateNotificationCount
        );
        view.addInternalFrame(listView);
        listView.setVisible(true);
        logger.log("NAVIGATION", "Usuário abriu lista de notificações");
    }

    private void onLogConfig(ActionEvent e) {
        LogConfigView configView = new LogConfigView();
        logConfigPresenter = new LogConfigPresenter(
            configView,
            logger,
            currentUser
        );
        view.addInternalFrame(configView);
        configView.setVisible(true);
        logger.log("NAVIGATION", "Administrador abriu configuração de logs");
    }

    private void onSystemRestore(ActionEvent e) {
        if (!isFirstAdmin) {
            view.showError("Apenas o primeiro administrador pode restaurar o sistema.");
            logger.log("ACCESS_DENIED", "Tentativa de restauração negada para: " + currentUser.getNome());
            return;
        }

        SystemRestoreView restoreView = new SystemRestoreView();
        systemRestorePresenter = new SystemRestorePresenter(
            restoreView,
            userRepository,
            notificationRepository,
            logger,
            currentUser,
            this::onSystemRestored
        );
        view.addInternalFrame(restoreView);
        restoreView.setVisible(true);
        logger.log("NAVIGATION", "Primeiro administrador abriu tela de restauração do sistema");
    }

    private void onSystemRestored() {
        logger.log("SYSTEM", "Sistema restaurado pelo administrador: " + currentUser.getNome());
        view.showMessage("Sistema restaurado. A aplicação será reiniciada.");
        restartApplication();
    }

    private void onLogout(ActionEvent e) {
        if (view.showConfirm("Deseja realmente sair da sua conta?")) {
            logger.log("LOGOUT", "Administrador deslogou: " + currentUser.getNome());
            view.close();
            if (onLogout != null) {
                onLogout.run();
            }
        }
    }

    private void onExit(ActionEvent e) {
        if (view.showConfirm("Deseja encerrar a aplicação?")) {
            logger.log("EXIT", "Aplicação encerrada por: " + currentUser.getNome());
            System.exit(0);
        }
    }

    private void updateNotificationCount() {
        int unreadCount = notificationRepository.countUnreadByUserId(currentUser.getId());
        view.setNotificationCount(unreadCount);
    }

    private void refreshViews() {
        updateNotificationCount();
    }

    private void restartApplication() {
        view.close();

        try {
            String java = System.getProperty("java.home") + "/bin/java";
            String classpath = System.getProperty("java.class.path");
            String mainClass = "com.ufes.controleusuario.ControleUsuario";

            ProcessBuilder builder = new ProcessBuilder(java, "-cp", classpath, mainClass);
            builder.start();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isCurrentUserFirstAdmin() {
        return isFirstAdmin;
    }
}
