package com.ufes.controleusuario.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserCreateView extends JInternalFrame {

    private JTextField txtNome;
    private JTextField txtUsername;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmaSenha;
    private JComboBox<String> cmbPerfil;
    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnCancelar;
    private JLabel lblPasswordStrength;

    public UserCreateView() {
        super("Cadastrar Novo Usuário", true, true, true, true);
        setSize(450, 380);
        setMinimumSize(new Dimension(400, 350));

        initComponents();
        setLocation(50, 50);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(createLabel("Nome:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtNome = new JTextField(20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(createLabel("Nome de Usuário:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(createLabel("Senha:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(createLabel("Confirmar Senha:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtConfirmaSenha = new JPasswordField(20);
        txtConfirmaSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(txtConfirmaSenha, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        lblPasswordStrength = new JLabel(" ");
        lblPasswordStrength.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        formPanel.add(lblPasswordStrength, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0;
        formPanel.add(createLabel("Perfil Inicial:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        cmbPerfil = new JComboBox<>(new String[]{"PADRAO", "ADMIN"});
        cmbPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbPerfil.setSelectedIndex(0);
        formPanel.add(cmbPerfil, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));

        btnCadastrar = createButton("Cadastrar", new Color(46, 125, 50));
        btnLimpar = createButton("Limpar", new Color(100, 100, 100));
        btnCancelar = createButton("Cancelar", new Color(180, 60, 60));

        buttonsPanel.add(btnLimpar);
        buttonsPanel.add(btnCancelar);
        buttonsPanel.add(btnCadastrar);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public String getNome() {
        return txtNome.getText().trim();
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getSenha() {
        return new String(txtSenha.getPassword());
    }

    public String getConfirmaSenha() {
        return new String(txtConfirmaSenha.getPassword());
    }

    public String getPerfil() {
        return (String) cmbPerfil.getSelectedItem();
    }

    public void setPerfilEnabled(boolean enabled) {
        cmbPerfil.setEnabled(enabled);
    }

    public void setAdminOptionVisible(boolean visible) {
        if (!visible) {
            cmbPerfil.removeItem("ADMIN");
        }
    }

    public void setPasswordStrength(String message, Color color) {
        lblPasswordStrength.setText(message);
        lblPasswordStrength.setForeground(color);
    }

    public void setCadastrarListener(ActionListener listener) {
        btnCadastrar.addActionListener(listener);
    }

    public void setLimparListener(ActionListener listener) {
        btnLimpar.addActionListener(listener);
    }

    public void setCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public void setSenhaKeyListener(java.awt.event.KeyListener listener) {
        txtSenha.addKeyListener(listener);
    }

    public void clearFields() {
        txtNome.setText("");
        txtUsername.setText("");
        txtSenha.setText("");
        txtConfirmaSenha.setText("");
        cmbPerfil.setSelectedIndex(0);
        lblPasswordStrength.setText(" ");
        txtNome.requestFocus();
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
