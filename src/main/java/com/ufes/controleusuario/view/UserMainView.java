package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class UserMainView extends JFrame {
    private JDesktopPane desktopPane;
    private JMenu mnuAccount;
    private JMenuItem mnuChangePassword;
    private JMenuItem mnuLogout;
    private JMenuItem mnuExit;
    private JMenu mnuNotifications;
    private JMenuItem mnuMyNotifications;
    private JPanel pnlFooter;
    private JLabel lblUserName;
    private JLabel lblUserType;
    private JButton btnNotifications;
    public UserMainView() {
        setTitle("Sistema de Gestão de Usuários - Área do Usuário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 550));
        initComponents();
        initMenuBar();
        initFooter();
    }
    private void initComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(52, 73, 94));
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        add(desktopPane, BorderLayout.CENTER);
    }
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(44, 62, 80));
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        mnuNotifications = createMenu("Notificações");
        mnuMyNotifications = createMenuItem("Minhas Notificações", "Ver suas notificações");
        mnuNotifications.add(mnuMyNotifications);
        mnuAccount = createMenu("Conta");
        mnuChangePassword = createMenuItem("Alterar Senha", "Modificar sua senha de acesso");
        mnuLogout = createMenuItem("Sair da Conta", "Deslogar do sistema");
        mnuExit = createMenuItem("Encerrar Aplicação", "Fechar o sistema");
        mnuAccount.add(mnuChangePassword);
        mnuAccount.addSeparator();
        mnuAccount.add(mnuLogout);
        mnuAccount.add(mnuExit);
        menuBar.add(mnuNotifications);
        menuBar.add(mnuAccount);
        setJMenuBar(menuBar);
    }
    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(new Color(236, 240, 241));
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
        pnlFooter.setBackground(new Color(44, 62, 80));
        pnlFooter.setBorder(new EmptyBorder(8, 15, 8, 15));
        JPanel pnlUserInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlUserInfo.setOpaque(false);
        lblUserName = new JLabel("Usuário: -");
        lblUserName.setForeground(new Color(189, 195, 199));
        lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 20));
        sep.setForeground(new Color(127, 140, 141));
        lblUserType = new JLabel("Tipo: Usuário");
        lblUserType.setForeground(new Color(241, 196, 15));
        lblUserType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlUserInfo.add(lblUserName);
        pnlUserInfo.add(sep);
        pnlUserInfo.add(lblUserType);
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlRight.setOpaque(false);
        btnNotifications = new JButton("🔔 Notificações: 0");
        btnNotifications.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNotifications.setBackground(new Color(52, 152, 219));
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
    }
    public void setNotificationCount(int count) {
        btnNotifications.setText("🔔 Notificações: " + count);
        if (count > 0) {
            btnNotifications.setBackground(new Color(231, 76, 60));
        } else {
            btnNotifications.setBackground(new Color(52, 152, 219));
        }
    }
    public void setMyNotificationsListener(ActionListener listener) {
        mnuMyNotifications.addActionListener(listener);
    }
    public void setNotificationButtonListener(ActionListener listener) {
        btnNotifications.addActionListener(listener);
    }
    public void setChangePasswordListener(ActionListener listener) {
        mnuChangePassword.addActionListener(listener);
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
