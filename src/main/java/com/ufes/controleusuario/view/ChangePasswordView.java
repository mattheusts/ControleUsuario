package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class ChangePasswordView extends JInternalFrame {
    private JPasswordField txtCurrentPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblPasswordStrength;
    public ChangePasswordView() {
        super("Alterar Senha", true, true, true, true);
        setSize(450, 350);
        setMinimumSize(new Dimension(400, 320));
        initComponents();
        setLocation(120, 70);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        JLabel lblTitle = new JLabel("🔐 Alteração de Senha");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            "Preencha os campos",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 73, 94)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(createLabel("Senha Atual:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtCurrentPassword = new JPasswordField(20);
        txtCurrentPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtCurrentPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(createLabel("Nova Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtNewPassword = new JPasswordField(20);
        txtNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtNewPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(createLabel("Confirmar Nova Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtConfirmPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        lblPasswordStrength = new JLabel(" ");
        lblPasswordStrength.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        formPanel.add(lblPasswordStrength, gbc);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnCancel = createButton("Cancelar", new Color(149, 165, 166));
        btnSave = createButton("💾 Salvar", new Color(46, 204, 113));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnSave);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    public String getCurrentPassword() {
        return new String(txtCurrentPassword.getPassword());
    }
    public String getNewPassword() {
        return new String(txtNewPassword.getPassword());
    }
    public String getConfirmPassword() {
        return new String(txtConfirmPassword.getPassword());
    }
    public void setPasswordStrength(String message, Color color) {
        lblPasswordStrength.setText(message);
        lblPasswordStrength.setForeground(color);
    }
    public void setSaveListener(ActionListener listener) {
        btnSave.addActionListener(listener);
    }
    public void setCancelListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }
    public void setNewPasswordKeyListener(java.awt.event.KeyListener listener) {
        txtNewPassword.addKeyListener(listener);
    }
    public void clearFields() {
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
        lblPasswordStrength.setText(" ");
        txtCurrentPassword.requestFocus();
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
    public void close() {
        dispose();
    }
}
