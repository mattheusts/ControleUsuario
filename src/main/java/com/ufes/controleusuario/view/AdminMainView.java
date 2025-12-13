package com.ufes.controleusuario.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminMainView extends JFrame {

    private JDesktopPane desktopPane;

    private JMenu mnuUsers;
    private JMenuItem mnuUserCreate;
    private JMenuItem mnuUserAuthorization;
    private JMenuItem mnuUserList;

    private JMenu mnuNotifications;
    private JMenuItem mnuNotificationSend;
    private JMenuItem mnuNotificationList;

    private JMenu mnuSystem;
    private JMenuItem mnuLogConfig;
    private JMenuItem mnuSystemRestore;
    private JMenuItem mnuLogout;
    private JMenuItem mnuExit;

    private JPanel pnlFooter;
    private JLabel lblUserName;
    private JLabel lblUserType;
    private JButton btnNotifications;

    public AdminMainView() {
        setTitle("Sistema de Gestão de Usuários - Painel Administrativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        initComponents();
        initMenuBar();
        initFooter();
    }

    private void initComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(45, 52, 64));
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        add(desktopPane, BorderLayout.CENTER);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(55, 63, 78));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        mnuUsers = createMenu("Usuários");
        mnuUserCreate = createMenuItem("Cadastrar Usuário", "Criar novo usuário no sistema");
        mnuUserAuthorization = createMenuItem("Autorizações Pendentes", "Gerenciar solicitações de acesso");
        mnuUserList = createMenuItem("Lista de Usuários", "Visualizar e gerenciar todos os usuários");

        mnuUsers.add(mnuUserCreate);
        mnuUsers.add(mnuUserAuthorization);
        mnuUsers.addSeparator();
        mnuUsers.add(mnuUserList);

        mnuNotifications = createMenu("Notificações");
        mnuNotificationSend = createMenuItem("Enviar Notificação", "Enviar notificações aos usuários");
        mnuNotificationList = createMenuItem("Minhas Notificações", "Ver suas notificações");

        mnuNotifications.add(mnuNotificationSend);
        mnuNotifications.add(mnuNotificationList);

        mnuSystem = createMenu("Sistema");
        mnuLogConfig = createMenuItem("Configurar Logs", "Definir formato de logs");
        mnuSystemRestore = createMenuItem("Restaurar Sistema", "Restaurar configurações iniciais");
        mnuLogout = createMenuItem("Sair da Conta", "Deslogar do sistema");
        mnuExit = createMenuItem("Encerrar Aplicação", "Fechar o sistema");

        mnuSystem.add(mnuLogConfig);
        mnuSystem.addSeparator();
        mnuSystem.add(mnuSystemRestore);
        mnuSystem.addSeparator();
        mnuSystem.add(mnuLogout);
        mnuSystem.add(mnuExit);

        menuBar.add(mnuUsers);
        menuBar.add(mnuNotifications);
        menuBar.add(mnuSystem);

        setJMenuBar(menuBar);
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(new Color(220, 220, 220));
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return menu;
    }

    private JMenuItem createMenuItem(String title, String tooltip) {
        JMenuItem item = new JMenuItem(title);
        item.setToolTipText(tooltip);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return item;
    }

    private void initFooter() {
        pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(new Color(38, 44, 54));
        pnlFooter.setBorder(new EmptyBorder(8, 15, 8, 15));

        JPanel pnlUserInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlUserInfo.setOpaque(false);

        lblUserName = new JLabel("Usuário: -");
        lblUserName.setForeground(new Color(180, 180, 180));
        lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 20));
        sep.setForeground(new Color(80, 80, 80));

        lblUserType = new JLabel("Tipo: -");
        lblUserType.setForeground(new Color(100, 200, 150));
        lblUserType.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlUserInfo.add(lblUserName);
        pnlUserInfo.add(sep);
        pnlUserInfo.add(lblUserType);

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlRight.setOpaque(false);

        btnNotifications = new JButton("🔔 Notificações: 0");
        btnNotifications.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNotifications.setBackground(new Color(70, 130, 180));
        btnNotifications.setForeground(Color.WHITE);
        btnNotifications.setFocusPainted(false);
        btnNotifications.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnNotifications.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        pnlRight.add(btnNotifications);

        pnlFooter.add(pnlUserInfo, BorderLayout.WEST);
        pnlFooter.add(pnlRight, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public void setUserName(String name) {
        lblUserName.setText("Usuário: " + name);
    }

    public void setUserType(String type) {
        lblUserType.setText("Tipo: " + type);
        if ("ADMIN".equalsIgnoreCase(type)) {
            lblUserType.setForeground(new Color(100, 200, 150));
        } else {
            lblUserType.setForeground(new Color(200, 180, 100));
        }
    }

    public void setNotificationCount(int count) {
        btnNotifications.setText("🔔 Notificações: " + count);
        if (count > 0) {
            btnNotifications.setBackground(new Color(220, 80, 60));
        } else {
            btnNotifications.setBackground(new Color(70, 130, 180));
        }
    }

    public void setUserMenuVisible(boolean visible) {
        mnuUsers.setVisible(visible);
    }

    public void setSystemRestoreVisible(boolean visible) {
        mnuSystemRestore.setVisible(visible);
    }

    public void setSystemRestoreEnabled(boolean enabled) {
        mnuSystemRestore.setEnabled(enabled);
    }

    public void setUserCreateListener(ActionListener listener) {
        mnuUserCreate.addActionListener(listener);
    }

    public void setUserAuthorizationListener(ActionListener listener) {
        mnuUserAuthorization.addActionListener(listener);
    }

    public void setUserListListener(ActionListener listener) {
        mnuUserList.addActionListener(listener);
    }

    public void setNotificationSendListener(ActionListener listener) {
        mnuNotificationSend.addActionListener(listener);
    }

    public void setNotificationListListener(ActionListener listener) {
        mnuNotificationList.addActionListener(listener);
    }

    public void setNotificationButtonListener(ActionListener listener) {
        btnNotifications.addActionListener(listener);
    }

    public void setLogConfigListener(ActionListener listener) {
        mnuLogConfig.addActionListener(listener);
    }

    public void setSystemRestoreListener(ActionListener listener) {
        mnuSystemRestore.addActionListener(listener);
    }

    public void setLogoutListener(ActionListener listener) {
        mnuLogout.addActionListener(listener);
    }

    public void setExitListener(ActionListener listener) {
        mnuExit.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmação",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public void addInternalFrame(JInternalFrame frame) {
        desktopPane.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        dispose();
    }
}
