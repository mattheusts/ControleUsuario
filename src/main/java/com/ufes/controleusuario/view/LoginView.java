package com.ufes.controleusuario.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

  // Components with correct prefixes
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JButton btnLogin;
  private JButton btnRegister;
  private JLabel lblMessage;

  public LoginView() {
    setTitle("Sistema de Login");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300, 200);
    setLocationRelativeTo(null);
    initComponents();
  }

  private void initComponents() {
    setLayout(new GridLayout(4, 2));

    add(new JLabel("Usuário:"));
    txtUsername = new JTextField();
    add(txtUsername);

    add(new JLabel("Senha:"));
    txtPassword = new JPasswordField();
    add(txtPassword);

    lblMessage = new JLabel("");
    add(lblMessage);

    // Spacer
    add(new JLabel(""));

    btnLogin = new JButton("Entrar");
    add(btnLogin);

    btnRegister = new JButton("Cadastrar (Solicitar Acesso)");
    add(btnRegister);
  }

  public String getUsername() {
    return txtUsername.getText();
  }

  public String getPassword() {
    return new String(txtPassword.getPassword());
  }

  public void setLoginListener(ActionListener listener) {
    btnLogin.addActionListener(listener);
  }

  public void setRegisterListener(ActionListener listener) {
    btnRegister.addActionListener(listener);
  }

  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  public void setStatusMessage(String message) {
    lblMessage.setText(message);
  }
}
