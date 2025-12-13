package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class UserRegistrationView extends JDialog {
  private JTextField txtNome;
  private JTextField txtUsername;
  private JPasswordField txtSenha;
  private JPasswordField txtConfirmaSenha;
  private JButton btnCadastrar;
  private JButton btnCancelar;
  private JLabel lblPasswordStrength;
  public UserRegistrationView(Frame owner) {
    super(owner, "Solicitação de Cadastro", true);
    setSize(400, 320);
    setResizable(false);
    initComponents();
    setLocationRelativeTo(owner);
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
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    formPanel.add(createLabel("Nome:"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    txtNome = new JTextField(20);
    formPanel.add(txtNome, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    formPanel.add(createLabel("Usuário:"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    txtUsername = new JTextField(20);
    formPanel.add(txtUsername, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0;
    formPanel.add(createLabel("Senha:"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    txtSenha = new JPasswordField(20);
    formPanel.add(txtSenha, gbc);
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.weightx = 0;
    formPanel.add(createLabel("Confirmar:"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    txtConfirmaSenha = new JPasswordField(20);
    formPanel.add(txtConfirmaSenha, gbc);
    gbc.gridx = 1;
    gbc.gridy = 4;
    lblPasswordStrength = new JLabel(" ");
    lblPasswordStrength.setFont(new Font("Segoe UI", Font.ITALIC, 11));
    formPanel.add(lblPasswordStrength, gbc);
    mainPanel.add(formPanel, BorderLayout.CENTER);
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    buttonsPanel.setBackground(new Color(250, 250, 250));
    btnCadastrar = new JButton("Solicitar Cadastro");
    btnCancelar = new JButton("Cancelar");
    buttonsPanel.add(btnCancelar);
    buttonsPanel.add(btnCadastrar);
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    setContentPane(mainPanel);
  }
  private JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 12));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setOpaque(true);
    button.setBorderPainted(false);
    return button;
  }
  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
    return label;
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
  public void setCadastrarListener(ActionListener listener) {
    btnCadastrar.addActionListener(listener);
  }
  public void setCancelarListener(ActionListener listener) {
    btnCancelar.addActionListener(listener);
  }
  public void setSenhaKeyListener(java.awt.event.KeyListener listener) {
    txtSenha.addKeyListener(listener);
  }
  public void setPasswordStrength(String message, Color color) {
    lblPasswordStrength.setText(message);
    lblPasswordStrength.setForeground(color);
  }
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
  }
  public void close() {
    dispose();
  }
}
