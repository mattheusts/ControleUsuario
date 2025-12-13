package com.ufes.controleusuario.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FirstAccessView extends JFrame implements IFirstAccessView {

  private JTextField txtNome;
  private JTextField txtUsuario;
  private JPasswordField txtSenha;
  private JButton btnCadastrar;

  public FirstAccessView() {
    setTitle("Cadastro Inicial - Primeiro Administrador");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Or DISPOSE based on flow, but usually Exit if they close this.
    setSize(400, 300);
    setLocationRelativeTo(null);
    initComponents();
  }

  private void initComponents() {
    setLayout(new GridLayout(5, 2, 10, 10));

    // Add some padding
    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    add(new JLabel("Nome:"));
    txtNome = new JTextField();
    add(txtNome);

    add(new JLabel("Usuário:"));
    txtUsuario = new JTextField();
    add(txtUsuario);

    add(new JLabel("Senha:"));
    txtSenha = new JPasswordField();
    add(txtSenha);

    add(new JLabel("")); // Spacer

    btnCadastrar = new JButton("Cadastrar");
    add(btnCadastrar);
  }

  @Override
  public String getNome() {
    return txtNome.getText();
  }

  @Override
  public String getUsuario() {
    return txtUsuario.getText();
  }

  @Override
  public String getSenha() {
    return new String(txtSenha.getPassword());
  }

  @Override
  public void setCadastrarListener(ActionListener listener) {
    btnCadastrar.addActionListener(listener);
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void close() {
    this.dispose();
  }
}
