package com.ufes.controleusuario.view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
public class LogConfigView extends JInternalFrame {
    private JRadioButton rbCSV;
    private JRadioButton rbJSONL;
    private ButtonGroup formatGroup;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblCurrentFormat;
    private JTextArea txtPreview;
    public LogConfigView() {
        super("Configuração de Logs", true, true, true, true);
        setSize(450, 380);
        setMinimumSize(new Dimension(400, 350));
        initComponents();
        setLocation(150, 80);
    }
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(250, 250, 250));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(250, 250, 250));
        JLabel lblTitle = new JLabel("Formato de Logs do Sistema");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(50, 50, 50));
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        lblCurrentFormat = new JLabel("Formato atual: -");
        lblCurrentFormat.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCurrentFormat.setForeground(new Color(100, 100, 100));
        lblCurrentFormat.setBorder(new EmptyBorder(5, 0, 0, 0));
        headerPanel.add(lblCurrentFormat, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        JPanel formatPanel = new JPanel(new BorderLayout(10, 10));
        formatPanel.setBackground(new Color(250, 250, 250));
        JPanel radioPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        radioPanel.setBackground(new Color(250, 250, 250));
        radioPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Selecione o formato",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(70, 70, 70)
        ));
        rbCSV = new JRadioButton("CSV (separado por ponto e vírgula)");
        rbCSV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rbCSV.setBackground(new Color(250, 250, 250));
        rbJSONL = new JRadioButton("JSONL (JSON Lines)");
        rbJSONL.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rbJSONL.setBackground(new Color(250, 250, 250));
        formatGroup = new ButtonGroup();
        formatGroup.add(rbCSV);
        formatGroup.add(rbJSONL);
        JPanel csvPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        csvPanel.setBackground(new Color(250, 250, 250));
        csvPanel.add(rbCSV);
        JPanel jsonlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jsonlPanel.setBackground(new Color(250, 250, 250));
        jsonlPanel.add(rbJSONL);
        radioPanel.add(csvPanel);
        radioPanel.add(jsonlPanel);
        formatPanel.add(radioPanel, BorderLayout.NORTH);
        JPanel previewPanel = new JPanel(new BorderLayout(5, 5));
        previewPanel.setBackground(new Color(250, 250, 250));
        previewPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel lblPreview = new JLabel("Exemplo de saída:");
        lblPreview.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPreview.setForeground(new Color(70, 70, 70));
        previewPanel.add(lblPreview, BorderLayout.NORTH);
        txtPreview = new JTextArea(4, 30);
        txtPreview.setEditable(false);
        txtPreview.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtPreview.setBackground(new Color(40, 44, 52));
        txtPreview.setForeground(new Color(200, 200, 200));
        txtPreview.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane scrollPreview = new JScrollPane(txtPreview);
        scrollPreview.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        previewPanel.add(scrollPreview, BorderLayout.CENTER);
        formatPanel.add(previewPanel, BorderLayout.CENTER);
        mainPanel.add(formatPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(250, 250, 250));
        btnCancel = createButton("Cancelar", new Color(100, 100, 100));
        btnSave = createButton("💾 Salvar", new Color(46, 125, 50));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnSave);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
        rbCSV.addActionListener(e -> updatePreview());
        rbJSONL.addActionListener(e -> updatePreview());
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
    private void updatePreview() {
        if (rbCSV.isSelected()) {
            txtPreview.setText("2024-01-15T10:30:45;LOGIN_SUCCESS;Usuário logado: admin\n" +
                               "2024-01-15T10:31:02;CREATE_USER;Novo usuário criado: joao");
        } else if (rbJSONL.isSelected()) {
            txtPreview.setText("{\"timestamp\": \"2024-01-15T10:30:45\", \"type\": \"LOGIN_SUCCESS\", \"message\": \"Usuário logado: admin\"}\n" +
                               "{\"timestamp\": \"2024-01-15T10:31:02\", \"type\": \"CREATE_USER\", \"message\": \"Novo usuário criado: joao\"}");
        }
    }
    public String getSelectedFormat() {
        if (rbCSV.isSelected()) {
            return "CSV";
        } else if (rbJSONL.isSelected()) {
            return "JSONL";
        }
        return null;
    }
    public void setCurrentFormat(String format) {
        lblCurrentFormat.setText("Formato atual: " + format);
        if ("CSV".equalsIgnoreCase(format)) {
            rbCSV.setSelected(true);
        } else {
            rbJSONL.setSelected(true);
        }
        updatePreview();
    }
    public void setSaveListener(ActionListener listener) {
        btnSave.addActionListener(listener);
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
    public void close() {
        dispose();
    }
}
