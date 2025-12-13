package com.ufes.controleusuario.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class MainView extends JFrame {
  private JDesktopPane desktopPane;
  private JLabel lblUser;
  private JLabel lblUserType;
  private JButton btnNotifications;
  private JMenuItem mnuExit;
  private JMenuItem mnuUsers;  
  public MainView() {
    setTitle("Sistema de Gestão de Usuários");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    initComponents();
  }
  private void initComponents() {
    desktopPane = new JDesktopPane();
    add(desktopPane, BorderLayout.CENTER);
    JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pnlFooter.setBorder(BorderFactory.createEtchedBorder());
    lblUser = new JLabel("Usuário: [Nome]");
    lblUserType = new JLabel("Tipo: [Tipo]");
    btnNotifications = new JButton("Notificações: 0");
    pnlFooter.add(lblUser);
    pnlFooter.add(new JSeparator(SwingConstants.VERTICAL));
    pnlFooter.add(lblUserType);
    pnlFooter.add(new JSeparator(SwingConstants.VERTICAL));
    pnlFooter.add(btnNotifications);
    add(pnlFooter, BorderLayout.SOUTH);
    JMenuBar menuBar = new JMenuBar();
    JMenu mnuArquivo = new JMenu("Arquivo");
    mnuExit = new JMenuItem("Sair");
    mnuArquivo.add(mnuExit);
    JMenu mnuAdmin = new JMenu("Administração");
    mnuUsers = new JMenuItem("Gerenciar Usuários");
    mnuAdmin.add(mnuUsers);
    menuBar.add(mnuArquivo);
    menuBar.add(mnuAdmin);
    setJMenuBar(menuBar);
  }
  public JDesktopPane getDesktopPane() {
    return desktopPane;
  }
  public void setUserName(String name) {
    lblUser.setText("Usuário: " + name);
  }
  public void setUserType(String type) {
    lblUserType.setText("Tipo: " + type);
  }
  public void setNotificationCount(int count) {
    btnNotifications.setText("Notificações: " + count);
  }
  public void setAdminMenuVisible(boolean visible) {
    getJMenuBar().getMenu(1).setVisible(visible);  
  }
  public void setExitListener(ActionListener listener) {
    mnuExit.addActionListener(listener);
  }
  public void setUsersMenuListener(ActionListener listener) {
    mnuUsers.addActionListener(listener);
  }
  public void setNotificationsListener(ActionListener listener) {
    btnNotifications.addActionListener(listener);
  }
}
