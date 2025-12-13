package com.ufes.controleusuario.presenter;
import com.ufes.controleusuario.model.User;
import com.ufes.controleusuario.view.MainView;
import javax.swing.JFrame;
public class MainPresenter {
  private MainView view;
  private User currentUser;
  public MainPresenter(User user) {
    this.currentUser = user;
    this.view = new MainView();
    initView();
    this.view.setVisible(true);
    this.view.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }
  private void initView() {
    view.setUserName(currentUser.getNome());
    view.setUserType(currentUser.getTipo());
    view.setNotificationCount(0);  
    if (!currentUser.isAdmin()) {
      view.setAdminMenuVisible(false);
    }
    view.setExitListener(e -> System.exit(0));
    view.setUsersMenuListener(e -> openUserManagement());
    view.setNotificationsListener(e -> openNotifications());
  }
  private void openUserManagement() {
    System.out.println("Opening User Management...");
  }
  private void openNotifications() {
    System.out.println("Opening Notifications...");
  }
}
