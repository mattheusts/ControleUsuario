package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class SystemRestoreView extends JInternalFrame {
    private JPasswordField txtPassword1;
    private JPasswordField txtPassword2;
    private JButton btnRestore;
    private JButton btnCancel;
    private JLabel lblWarning;
    private JProgressBar progressBar;
    private JPanel formPanel;
    private JPanel progressPanel;
    public SystemRestoreView() {
        super("Restauração do Sistema", true, true, false, false);
        setSize(480, 380);
        setMinimumSize(new Dimension(450, 350));
        initComponents();
        setLocation(180, 100);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel warningPanel = new JPanel(new BorderLayout());
        warningPanel.setBackground(new Color(255, 240, 240));
        warningPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 100, 100), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        JLabel lblIcon = new JLabel("⚠️");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        warningPanel.add(lblIcon, BorderLayout.WEST);
        lblWarning = new JLabel("<html><b>ATENÇÃO: Operação Irreversível!</b><br>" +
                "Esta ação irá apagar TODOS os dados do sistema,<br>" +
                "incluindo usuários, notificações e configurações.</html>");
        lblWarning.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblWarning.setForeground(new Color(150, 50, 50));
        lblWarning.setBorder(new EmptyBorder(0, 15, 0, 0));
        warningPanel.add(lblWarning, BorderLayout.CENTER);
        mainPanel.add(warningPanel, BorderLayout.NORTH);
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Confirme sua identidade",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(70, 70, 70)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblPassword1 = new JLabel("Digite sua senha:");
        lblPassword1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblPassword1, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtPassword1 = new JPasswordField(20);
        txtPassword1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtPassword1, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPassword2 = new JLabel("Confirme sua senha:");
        lblPassword2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblPassword2, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtPassword2 = new JPasswordField(20);
        txtPassword2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtPassword2, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html><i>Você precisará confirmar esta ação em um diálogo adicional.</i></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(100, 100, 100));
        formPanel.add(lblInfo, gbc);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        progressPanel = new JPanel(new BorderLayout(10, 10));
        progressPanel.setBackground(new Color(250, 250, 250));
        progressPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        progressPanel.setVisible(false);
        JLabel lblRestoring = new JLabel("Restaurando o sistema...", JLabel.CENTER);
        lblRestoring.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRestoring.setForeground(new Color(70, 130, 180));
        progressPanel.add(lblRestoring, BorderLayout.NORTH);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(false);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        JLabel lblWait = new JLabel("Por favor, aguarde. A aplicação será reiniciada.", JLabel.CENTER);
        lblWait.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblWait.setForeground(new Color(100, 100, 100));
        progressPanel.add(lblWait, BorderLayout.SOUTH);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnCancel = createButton("Cancelar", new Color(100, 100, 100));
        btnRestore = createButton("🔄 Restaurar Sistema", new Color(180, 60, 60));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnRestore);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    public String getPassword1() {
        return new String(txtPassword1.getPassword());
    }
    public String getPassword2() {
        return new String(txtPassword2.getPassword());
    }
    public void setButtonsEnabled(boolean enabled) {
        btnRestore.setEnabled(enabled);
        btnCancel.setEnabled(enabled);
    }
    public void showProgressPanel() {
        formPanel.setVisible(false);
        progressPanel.setVisible(true);
        ((JPanel) getContentPane()).add(progressPanel, BorderLayout.CENTER);
        ((JPanel) getContentPane()).revalidate();
        ((JPanel) getContentPane()).repaint();
    }
    public void setRestoreListener(ActionListener listener) {
        btnRestore.addActionListener(listener);
    }
    public void setCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
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
        int result = JOptionPane.showConfirmDialog(this, message, "Confirmação Final",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    public void clearFields() {
        txtPassword1.setText("");
        txtPassword2.setText("");
        txtPassword1.requestFocus();
    }
    public void close() {
        dispose();
    }
}
