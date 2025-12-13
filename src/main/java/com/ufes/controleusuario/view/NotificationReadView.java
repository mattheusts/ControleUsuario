package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class NotificationReadView extends JInternalFrame {
    private JTextArea txtMessage;
    private JLabel lblDate;
    private JLabel lblStatus;
    private JButton btnMarkAsRead;
    private JButton btnClose;
    public NotificationReadView() {
        super("Leitura de Notificação", true, true, true, true);
        setSize(500, 400);
        setMinimumSize(new Dimension(450, 350));
        initComponents();
        setLocation(150, 80);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel headerPanel = new JPanel(new BorderLayout(10, 5));
        headerPanel.setBackground(new Color(250, 250, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblTitle = new JLabel("📩 Notificação");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        infoPanel.setBackground(new Color(250, 250, 250));
        lblDate = new JLabel("Data: -");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(new Color(127, 140, 141));
        lblStatus = new JLabel("Status: -");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(230, 126, 34));
        infoPanel.add(lblDate);
        infoPanel.add(new JSeparator(SwingConstants.VERTICAL));
        infoPanel.add(lblStatus);
        headerPanel.add(infoPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(250, 250, 250));
        contentPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            "Mensagem",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 73, 94)
        ));
        txtMessage = new JTextArea();
        txtMessage.setEditable(false);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessage.setBackground(new Color(255, 255, 255));
        txtMessage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JScrollPane scrollPane = new JScrollPane(txtMessage);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnMarkAsRead = createButton("✓ Marcar como Lida", new Color(46, 204, 113));
        btnClose = createButton("Fechar", new Color(149, 165, 166));
        buttonsPanel.add(btnMarkAsRead);
        buttonsPanel.add(btnClose);
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
    public void setMessage(String message) {
        txtMessage.setText(message);
        txtMessage.setCaretPosition(0);
    }
    public void setDate(String date) {
        lblDate.setText("Data: " + date);
    }
    public void setReadStatus(boolean isRead) {
        if (isRead) {
            lblStatus.setText("Status: Lida");
            lblStatus.setForeground(new Color(46, 204, 113));
            btnMarkAsRead.setEnabled(false);
            btnMarkAsRead.setBackground(new Color(189, 195, 199));
        } else {
            lblStatus.setText("Status: Não lida");
            lblStatus.setForeground(new Color(230, 126, 34));
            btnMarkAsRead.setEnabled(true);
            btnMarkAsRead.setBackground(new Color(46, 204, 113));
        }
    }
    public void setMarkAsReadListener(ActionListener listener) {
        btnMarkAsRead.addActionListener(listener);
    }
    public void setCloseListener(ActionListener listener) {
        btnClose.addActionListener(listener);
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
