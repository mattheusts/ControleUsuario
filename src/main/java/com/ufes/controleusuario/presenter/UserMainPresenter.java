package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.repository.INotificationRepository;
import com.ufes.controleusuario.repository.IUserRepository;
import com.ufes.controleusuario.service.ILoggerService;
import com.ufes.controleusuario.service.IPasswordValidator;
import com.ufes.controleusuario.view.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
public class UserMainPresenter {
    private UserMainView view;
    private User currentUser;
    private IUserRepository userRepository;
    private INotificationRepository notificationRepository;
    private ILoggerService logger;
    private IPasswordValidator passwordValidator;
    private UserNotificationListPresenter notificationListPresenter;
    private ChangePasswordPresenter changePasswordPresenter;
    private Runnable onLogout;
    public UserMainPresenter(UserMainView view, User currentUser,
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
        if (currentUser.isAdmin()) {
            view.showError("Acesso negado. Administradores devem usar o painel administrativo.");
            view.close();
            return;
        }
        initView();
        initListeners();
        updateNotificationCount();
        logger.log("ACCESS", "Usuário acessou o sistema: " + currentUser.getNome());
        this.view.setVisible(true);
        this.view.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    private void initView() {
        view.setUserName(currentUser.getNome());
        view.setUserType("Usuário");
    }
    private void initListeners() {
        view.setMyNotificationsListener(this::onMyNotifications);
        view.setNotificationButtonListener(this::onMyNotifications);
        view.setChangePasswordListener(this::onChangePassword);
        view.setLogoutListener(this::onLogout);
        view.setExitListener(this::onExit);
    }
    private void onMyNotifications(ActionEvent e) {
        UserNotificationListView listView = new UserNotificationListView();
        notificationListPresenter = new UserNotificationListPresenter(
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
    private void onChangePassword(ActionEvent e) {
        ChangePasswordView changeView = new ChangePasswordView();
        changePasswordPresenter = new ChangePasswordPresenter(
            changeView,
            userRepository,
            logger,
            passwordValidator,
            currentUser
        );
        view.addInternalFrame(changeView);
        changeView.setVisible(true);
        logger.log("NAVIGATION", "Usuário abriu tela de alteração de senha");
    }
    private void onLogout(ActionEvent e) {
        if (view.showConfirm("Deseja realmente sair da sua conta?")) {
            logger.log("LOGOUT", "Usuário deslogou: " + currentUser.getNome());
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
    public User getCurrentUser() {
        return currentUser;
    }
}
